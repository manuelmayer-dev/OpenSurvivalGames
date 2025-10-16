package dev.suchbyte.openSurvivalGames.core.game.managers

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import dev.suchbyte.openSurvivalGames.common.utils.Title
import dev.suchbyte.openSurvivalGames.OpenSurvivalGamesPlugin
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import dev.suchbyte.openSurvivalGames.core.maps.managers.MapManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.BattleCryManager
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.CosmeticManager
import dev.suchbyte.openSurvivalGames.core.game.states.DeathMatchGameStateHandler
import dev.suchbyte.openSurvivalGames.core.game.states.InGameGameStateHandler
import dev.suchbyte.openSurvivalGames.core.game.states.LobbyGameStateHandler
import dev.suchbyte.openSurvivalGames.core.game.states.WarmupGameStateHandler
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.common.extensions.StringExtensions.Companion.removeMinecraftColorCodes
import dev.suchbyte.openSurvivalGames.common.extensions.StringExtensions.Companion.sendToOnlinePlayersWithPrefix
import dev.suchbyte.openSurvivalGames.core.maps.modules.MapModule
import dev.suchbyte.openSurvivalGames.common.utils.Items
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import java.time.LocalDate
import kotlin.concurrent.withLock

class GameManager(
    plugin: OpenSurvivalGamesPlugin,
    private val config: PluginConfig,
    private val statsManager: StatsManager,
    private val mapManager: MapManager,
    mapModules: Map<String, Class<out MapModule>>,
    private val cosmeticManager: CosmeticManager,
    private val battleCryManager: BattleCryManager,
) : BaseGameManager(plugin, config, mapModules, statsManager, mapManager) {
    private lateinit var lobbyGameStateHandler: LobbyGameStateHandler
    private lateinit var warmupGameStateHandler: WarmupGameStateHandler
    private lateinit var inGameGameStateHandler: InGameGameStateHandler
    private lateinit var deathMatchGameStateHandler: DeathMatchGameStateHandler

    fun start() {
        lobbyGameStateHandler = getGameStateHandler(GameState.Lobby)
        warmupGameStateHandler = getGameStateHandler(GameState.Warmup)
        inGameGameStateHandler = getGameStateHandler(GameState.InGame)
        deathMatchGameStateHandler = getGameStateHandler(GameState.DeathMatch)

        lobbyGameStateHandler.setDelegate(::onLobbyFinish)
        warmupGameStateHandler.setDelegate(::onWarmupFinish)
        inGameGameStateHandler.setDelegate(::onInGameFinish)
        deathMatchGameStateHandler.setDelegate(::onDeathMatchFinish)

        lobbyGameStateHandler.onEnable()
    }

    override fun isInGame(): Boolean {
        return currentGameState == GameState.InGame || currentGameState == GameState.DeathMatch
    }

    override fun playerDied(player: Player, playerDeathEvent: PlayerDeathEvent?) {
        alivePlayersLock.withLock {
            if (!alivePlayers.contains(player)) {
                return
            }

            if (alivePlayers.contains(player) && alivePlayers.size > 1) {
                alivePlayers.remove(player)
            }

            "§6Tribute ${player.displayName} §6has fallen.".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
            "§b${alivePlayers.size} §6tributes remain.".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)

            val killer = lastDamager[player]
            if (killer != null && (alivePlayers.contains(killer) || spectatingPlayers.contains(killer))) {
                statsManager.setKiller(player, killer)
                statsManager.addKill(killer)

                var lostGlobalPoints = statsManager.getStats(player).points / 40
                if (lostGlobalPoints < 5) {
                    lostGlobalPoints = 5
                }

                var lostSeasonPoints = statsManager.getSeasonStats(player).points / 40
                if (lostSeasonPoints < 5) {
                    lostSeasonPoints = 5
                }

                statsManager.addPoints(killer, lostGlobalPoints)
                statsManager.addSeasonPoints(killer, lostSeasonPoints)
                statsManager.removePoints(player, lostGlobalPoints)
                statsManager.removeSeasonPoints(player, lostSeasonPoints)

                player.sendMessageWithPrefix(
                    "§6You lost $lostGlobalPoints §2global points §6and $lostSeasonPoints §cseason points " +
                            "§6for being killed by ${killer.displayName.removeMinecraftColorCodes()}.",
                    Prefix.SurvivalGames
                )
                if (killer.isOnline) {
                    killer.sendMessageWithPrefix(
                        "§6You gained $lostGlobalPoints §2global points §6and $lostSeasonPoints §cseason points " +
                                "§6for killing ${player.displayName.removeMinecraftColorCodes()}.",
                        Prefix.SurvivalGames
                    )
                }

                if (killer.isOnline) {
                    battleCryManager.play(
                        killer,
                        listOf(killer, player).union(
                            player.getNearbyEntities(10.0, 10.0, 10.0).filterIsInstance<Player>()
                        ).toList()
                    )
                }

                if (cosmeticManager.hasEnabled(killer, Cosmetics.SGDeathCrate)) {
                    val items: List<ItemStack>
                    if (playerDeathEvent != null) {
                        items = playerDeathEvent.drops.filter { x -> x.type != null && x.type != Material.AIR }.toList()
                        playerDeathEvent.drops.clear()
                    } else {
                        items = player.inventory.contents.union(player.inventory.armorContents.toList())
                            .filter { x -> x.type != null && x.type != Material.AIR }.toList()
                    }

                    createDeathCrate(player, items)
                }
            } else {
                /*val deathReason = when (player.lastDamageCause?.cause) {
                    EntityDamageEvent.DamageCause.ENTITY_ATTACK -> PlayerDeathReason.KilledByMob
                    EntityDamageEvent.DamageCause.PROJECTILE -> PlayerDeathReason.KilledByMob
                    EntityDamageEvent.DamageCause.SUFFOCATION -> PlayerDeathReason.Suffocation
                    EntityDamageEvent.DamageCause.FALL -> PlayerDeathReason.FallDamage
                    EntityDamageEvent.DamageCause.FIRE -> PlayerDeathReason.Fire
                    EntityDamageEvent.DamageCause.FIRE_TICK -> PlayerDeathReason.Fire
                    EntityDamageEvent.DamageCause.MELTING -> PlayerDeathReason.Fire
                    EntityDamageEvent.DamageCause.LAVA -> PlayerDeathReason.Lava
                    EntityDamageEvent.DamageCause.DROWNING -> PlayerDeathReason.Drowning
                    EntityDamageEvent.DamageCause.BLOCK_EXPLOSION -> PlayerDeathReason.Explosion
                    EntityDamageEvent.DamageCause.ENTITY_EXPLOSION -> PlayerDeathReason.Explosion
                    EntityDamageEvent.DamageCause.VOID -> PlayerDeathReason.Void
                    EntityDamageEvent.DamageCause.LIGHTNING -> PlayerDeathReason.Lightning
                    EntityDamageEvent.DamageCause.STARVATION -> PlayerDeathReason.Starvation
                    EntityDamageEvent.DamageCause.POISON -> PlayerDeathReason.Poison
                    else -> PlayerDeathReason.Other
                }*/

                // TODO: Maybe add game events later
            }

            if (isInGame()) {
                statsManager.addDeath(player)
            }

            statsManager.commit(player)
            checkAlivePlayers()
        }
    }

    override fun checkAlivePlayers() {
        if (!isInGame()) {
            return
        }

        if (alivePlayers.size > 1) {
            return
        }

        endRound()
    }

    override fun addSpectatorItems(player: Player) {
        player.inventory.setItem(0, Items.teleporterItem)
        player.inventory.setItem(8, Items.leftServerItem)
    }

    private fun endRound() {
        if (currentGameState == GameState.Ending) {
            return
        }
        setGameState(GameState.Ending)

        plugin.gameStates.forEach { it.onDisable() }

        Bukkit.getOnlinePlayers().forEach {
            val roundStats = statsManager.getRoundStatsOrNull(it) ?: return@forEach
            if (roundStats.killerName == null || roundStats.killerHealth == null) return@forEach
            it.sendMessageWithPrefix(
                "§cThis round, you were killed by ${roundStats.killerName}. " +
                        "Your killer was on ${roundStats.killerHealth?.toInt()} hearts.", Prefix.SurvivalGames
            )
        }

        val winner = if (alivePlayers.size == 1) alivePlayers.firstOrNull() else null
        var firstWinOfToday = false
        if (winner != null) {
            firstWinOfToday = statsManager.getStats(winner).lastWon?.toLocalDate() != LocalDate.now()
            statsManager.addWins(winner)
            statsManager.addPoints(winner, if (firstWinOfToday) 300 else 100)
            statsManager.addSeasonPoints(winner, if (firstWinOfToday) 300 else 100)
        }

        Bukkit.getOnlinePlayers().forEach {
            val roundStats = statsManager.getRoundStatsOrNull(it) ?: return@forEach
            val totalPoints = roundStats.gainedPoints - roundStats.lostPoints
            it.sendMessageWithPrefix(
                "§c§lMatch Recap §6- " +
                        "Games Played: §21 " +
                        "§6Crates Opened: §2${roundStats.chestsOpened} " +
                        "§6Deathmatches: §2${if (roundStats.wasInDeathMatch) 1 else 0} " +
                        "§6Kills: §2${roundStats.kills} " +
                        "§6Deaths: §2${if (roundStats.hasDied) 1 else 0} " +
                        "§6Total Points: §${if (totalPoints < 0) "4" else "2"}$totalPoints", Prefix.SurvivalGames
            )
        }

        "§cThe round has ended!".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
        if (winner == null) {
            "§cThere was no winner in this round!".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
        } else {
            "§c${winner.displayName.removeMinecraftColorCodes()} has won the Survival Games!"
                .sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)

            if (firstWinOfToday) {
                winner.sendMessageWithPrefix(
                    "§cThis was your first win of today! You gained 300 points.",
                    Prefix.SurvivalGames
                )
            } else {
                winner.sendMessageWithPrefix(
                    "§6You gained 100 points for winning the SurvivalGames!",
                    Prefix.SurvivalGames
                )
            }

            Title.sendToOnlinePlayers(winner.displayName, "§6is the §eWINNER!")
        }

        shutDown()
    }

    private fun onLobbyFinish() {
        if (currentGameState != GameState.Lobby) {
            return
        }

        alivePlayers.addAll(Bukkit.getOnlinePlayers())
        setGameState(GameState.Warmup)
        warmupGameStateHandler.onEnable()
    }

    private fun onWarmupFinish() {
        if (currentGameState != GameState.Warmup) {
            return
        }

        initializeMapModule()

        setGameState(GameState.InGame)
        gameStarted = System.currentTimeMillis()
        alivePlayers.forEach {
            statsManager.addGamesPlayed(it)
            it.spigot().collidesWithEntities = true
        }
        spectatingPlayers.forEach {
            addSpectatorItems(it)
        }
        inGameGameStateHandler.onEnable()
    }

    private fun onInGameFinish() {
        mapModule?.onDisable()
        mapModule = null

        if (currentGameState != GameState.InGame) {
            return
        }

        setGameState(GameState.DeathMatch)
        deathMatchGameStateHandler.onEnable()
    }

    private fun onDeathMatchFinish() {
        if (currentGameState != GameState.DeathMatch) {
            return
        }

        setGameState(GameState.Ending)
        endRound()
    }

    fun checkWillGameStart(): Boolean {
        if (mapManager.mainMapManifest == null) return false
        if (Bukkit.getOnlinePlayers().size < config.minPlayers + 4) return false
        return true
    }

    fun getTimeLeft(): Int {
        return when (currentGameState) {
            GameState.Lobby -> lobbyGameStateHandler.countdown
            GameState.InGame -> inGameGameStateHandler.getTimeLeft()
            GameState.DeathMatch -> deathMatchGameStateHandler.getTimeLeft()
            else -> 0
        }
    }

    override fun handleTeleporter(player: Player) {
        super.handleTeleporter(player)

        // TODO: Handle teleporter
        /*PlayerTeleporterMenu(
            this,
            swordGameCoreApi.getPlayerMenuUtility(player),
            statsManager
        ).onOpen()*/
    }
}