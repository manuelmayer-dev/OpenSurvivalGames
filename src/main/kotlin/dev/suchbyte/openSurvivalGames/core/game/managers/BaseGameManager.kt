package dev.suchbyte.openSurvivalGames.core.game.managers

import dev.suchbyte.openSurvivalGames.OpenSurvivalGamesPlugin
import dev.suchbyte.openSurvivalGames.core.game.states.GameStateHandler
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.common.extensions.ConfigLocationExtensions.Companion.toBukkitLocation
import dev.suchbyte.openSurvivalGames.common.extensions.StringExtensions.Companion.sendToOnlinePlayersWithPrefix
import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import dev.suchbyte.openSurvivalGames.core.maps.managers.BaseMapManager
import dev.suchbyte.openSurvivalGames.core.maps.modules.MapModule
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.random.Random

abstract class BaseGameManager(
    protected val plugin: OpenSurvivalGamesPlugin,
    private val config: PluginConfig,
    protected val mapModules: Map<String, Class<out MapModule>>,
    private val statsManager: StatsManager,
    private val mapManager: BaseMapManager
) {
    var mapModule: MapModule? = null
        protected set

    val alivePlayers = mutableListOf<Player>()
    protected val alivePlayersLock = ReentrantLock()
    val spectatingPlayers = mutableListOf<Player>()
    protected var lastDamager = mutableMapOf<Player, Player>()
    protected val deathCrateLocations = mutableListOf<Location>()

    var currentGameState: GameState = GameState.Lobby
        protected set
    var gameFinished: Boolean = false
        protected set
    protected var gameStarted: Long? = null

    fun deathCrateParticles() {
        deathCrateLocations.forEach {
            it.world.playEffect(it, Effect.MOBSPAWNER_FLAMES, 6)
            it.world.playEffect(it, Effect.MOBSPAWNER_FLAMES, 3)
            it.world.playEffect(it, Effect.MOBSPAWNER_FLAMES, 2)
        }
    }

    protected fun createDeathCrate(player: Player, items: List<ItemStack>) {
        val location = player.location
        val block: Block = location.block
        block.type = Material.CHEST
        val chest: Chest = block.state as Chest
        items.forEach { chest.inventory.addItem(it) }
        chest.update()
        deathCrateLocations.add(location)
    }

    abstract fun playerDied(player: Player, playerDeathEvent: PlayerDeathEvent? = null)

    abstract fun checkAlivePlayers()

    fun clearInventory(player: Player) {
        player.inventory.clear()
        player.inventory.armorContents = null
        player.health = 20.0
        player.foodLevel = 20
        player.fireTicks = 0
        player.exp = 0f
        player.level = 0
        player.isFlying = false
        player.allowFlight = false
    }

    fun teleportToLobby(player: Player) {
        player.velocity = Vector(0, 0, 0)
        val spawnLocation = config.lobbyLocation?.toBukkitLocation()
        if (spawnLocation != null) {
            spawnLocation.x += Random.nextDouble(-0.4, 0.4)
            spawnLocation.y += 0.5
            spawnLocation.z += Random.nextDouble(-0.4, 0.4)
            player.teleport(spawnLocation)
        } else {
            player.sendMessageWithPrefix("§cLobby is not set up yet")
        }
    }

    fun addSpectator(player: Player) {
        if (!spectatingPlayers.contains(player)) {
            spectatingPlayers.add(player)
        }

        Bukkit.getOnlinePlayers().forEach { it.hidePlayer(player) }
        spectatingPlayers.forEach { player.hidePlayer(it) }

        player.gameMode = GameMode.ADVENTURE
        player.fireTicks = 0
        player.spigot().collidesWithEntities = false
        addSpectatorItems(player)
        spectatorAdded(player)
    }

    open fun spectatorAdded(player: Player) {}

    protected fun shutDown(completion: (() -> Unit)? = null) {
        setGameState(GameState.Ending)

        statsManager.commitAll()

        plugin.server.scheduler.runTaskLater(plugin, {
            "§cGame Over §7» §3Restarting in 10 seconds.".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)

            plugin.server.scheduler.runTaskLater(plugin, {
                completion?.invoke()
                gameFinished = true
                Bukkit.shutdown()
            }, 20 * 10)
        }, 20 * 2)
    }

    abstract fun addSpectatorItems(player: Player)

    open fun playerDisconnected(player: Player) {
        if (currentGameState == GameState.Warmup
            || currentGameState == GameState.InGame
            || currentGameState == GameState.DeathMatch
        ) {
            alivePlayersLock.withLock {
                if (!alivePlayers.contains(player)) {
                    return
                }

                val location: Location = player.location
                for (item in player.inventory.contents.filter { x -> x != null && x.type != Material.AIR }) {
                    if (item != null) {
                        location.world.dropItemNaturally(location, item)
                    }
                }

                for (armorItem in player.inventory.armorContents.filter { x -> x != null && x.type != Material.AIR }) {
                    if (armorItem != null) {
                        location.world.dropItemNaturally(location, armorItem)
                    }
                }

                player.inventory.clear()
                player.inventory.armorContents = null
            }
            playerDied(player)
        }

        if (spectatingPlayers.contains(player)) {
            spectatingPlayers.remove(player)
        }

        if (alivePlayers.contains(player)) {
            alivePlayers.remove(player)
        }

        if (currentGameState != GameState.Lobby && currentGameState != GameState.Ending) {
            checkAlivePlayers()
        }
    }

    protected fun initializeMapModule() {
        val mapModuleType = mapModules[mapManager.mainMapManifest!!.id] ?: return
        val constructor = mapModuleType.getDeclaredConstructor(
            OpenSurvivalGamesPlugin::class.java,
            BaseGameManager::class.java,
            BaseMapManager::class.java
        )
        mapModule = constructor.newInstance(plugin, this, mapManager)
    }

    protected fun setGameState(gameState: GameState) {
        currentGameState = gameState
    }

    protected fun <T> getGameStateHandler(gameState: GameState): T
            where T : GameStateHandler {
        val handler = plugin.gameStates.find { it.gameState == gameState }
        return handler?.let {
            @Suppress("UNCHECKED_CAST") // Suppress unchecked cast warning
            it as T
        } ?: throw NoSuchElementException("No GameStateHandler found for gameState: $gameState")
    }

    abstract fun isInGame(): Boolean

    fun playerDamaged(player: Player, damager: Player?) {
        if (damager == null
            || !alivePlayers.contains(player)
            || !alivePlayers.contains(damager)
            || currentGameState != GameState.InGame && currentGameState != GameState.DeathMatch
        ) {
            return
        }

        lastDamager[player] = damager
    }

    open fun handleTeleporter(player: Player) {
        if (!spectatingPlayers.contains(player)
            || (currentGameState != GameState.InGame && currentGameState != GameState.DeathMatch)
        ) {
            player.sendMessageWithPrefix("§cYou cannot use this right now!")
            return
        }
    }
}