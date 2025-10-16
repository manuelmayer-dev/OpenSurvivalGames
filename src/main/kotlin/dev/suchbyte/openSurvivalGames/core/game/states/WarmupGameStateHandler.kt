package dev.suchbyte.openSurvivalGames.core.game.states

import dev.suchbyte.openSurvivalGames.OpenSurvivalGamesPlugin
import dev.suchbyte.openSurvivalGames.presentation.commands.admin.SkipWarmupCommand
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.ArrowTrailManager
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.BattleCryManager
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import dev.suchbyte.openSurvivalGames.common.extensions.ConfigLocationExtensions.Companion.toBukkitLocation
import dev.suchbyte.openSurvivalGames.common.extensions.StringExtensions.Companion.sendToOnlinePlayersWithPrefix
import dev.suchbyte.openSurvivalGames.core.maps.managers.MapManager
import dev.suchbyte.openSurvivalGames.core.scoreboard.managers.ScoreboardManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import dev.suchbyte.openSurvivalGames.common.utils.DateUtils
import dev.suchbyte.openSurvivalGames.common.utils.Title
import dev.suchbyte.openSurvivalGames.common.utils.WorldUtils
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.abs

class WarmupGameStateHandler(
    private val plugin: OpenSurvivalGamesPlugin,
    private val config: PluginConfig,
    private val mapManager: MapManager,
    private val scoreboardManager: ScoreboardManager,
    private val statsManager: StatsManager,
    private val battleCryManager: BattleCryManager,
    private val arrowTrailManager: ArrowTrailManager,
) : GameStateHandler(plugin, GameState.Warmup) {
    private val initialCountdown = 31
    private var countdown = initialCountdown
    private var task: BukkitTask? = null
    private var teleportTask: BukkitTask? = null
    private val teleportedPlayers = mutableListOf<UUID>()

    private var titleIndex = 0
    private var titles = listOf<String>()

    override fun onEnable() {
        super.onEnable()

        plugin.getCommand("skipwarmup").executor = SkipWarmupCommand(this)

        countdown = initialCountdown
        titles = listOf(
            "§aWelcome to SurvivalGames",
            (if (config.maxTeamSize > 1) "§6Team Size: §f${config.maxTeamSize}" else "§cNo Teams!"),
            "§eHave fun!",
        )

        val players = Bukkit.getOnlinePlayers()
        teleportPlayers(players.toList())
        players.forEach { player ->
            player.spigot().collidesWithEntities = true
            statsManager.updateLastPlayed(player)
            battleCryManager.cachePlayer(player)
            arrowTrailManager.cachePlayer(player)
        }
        players.forEach { player ->
            player.inventory.clear()
            player.inventory.armorContents = null
            player.foodLevel = 20
            player.health = 20.0
            player.fireTicks = 0
            player.isFlying = false
            player.allowFlight = false
            scoreboardManager.removeScoreboard(player)
        }

        plugin.server.scheduler.runTaskLater(plugin, {
            startCountdown()
        }, 20)
    }

    override fun onDisable() {
        super.onDisable()
        plugin.getCommand("skipwarmup").executor = null
        task?.cancel()
        task = null
    }

    fun skipWarmup() {
        if (countdown <= 4) {
            return
        }

        countdown = 4
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (event.player.world != mapManager.mainMapWorld
            || !teleportedPlayers.contains(event.player.uniqueId)
        ) {
            return
        }

        val diffX = abs(event.from.x - event.to.x)
        val diffZ = abs(event.from.z - event.to.z)

        if (diffX > 0 || diffZ > 0) {
            event.player.teleport(event.from)
        }
    }

    private fun startCountdown() {
        task = plugin.server.scheduler.runTaskTimer(plugin, {
            countdown--

            Bukkit.getOnlinePlayers().forEach { player ->
                player.level = countdown
            }

            if (countdown >= 10) {
                if (countdown % 10 == 0) {
                    announceGameStart()
                    val title = titles.getOrNull(titleIndex)
                    if (title != null) {
                        Title.sendToOnlinePlayers(title, "")
                        titleIndex += 1
                    }
                }
                if (countdown == 20) {
                    sendMapDetails()
                }
            }

            if (countdown < 10) {
                announceGameStart()
            }

            if (countdown <= 3) {
                Title.sendToOnlinePlayers("§a${countdown}", "")
                Bukkit.getOnlinePlayers().forEach { player ->
                    player.playSound(player.eyeLocation, Sound.SUCCESSFUL_HIT, 1f, 1f)
                }
            }

            if (countdown < 1) {
                task?.cancel()
                onDisable()
            }
        }, 20, 20)
    }

    private fun sendMapDetails() {
        "§6Map: §b${mapManager.mainMapManifest!!.name}".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
        "§6Creator: §b${mapManager.mainMapManifest!!.author}".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
    }

    private fun announceGameStart() {
        if (countdown < 1) {
            return
        }

        "§b${DateUtils.formatSeconds(countdown.toLong())} §6until the game starts!"
            .sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
    }

    private fun teleportPlayers(players: List<Player>) {
        val spawnLocations = mapManager.getSpawnLocations()
        if (spawnLocations.isEmpty()) {
            Bukkit.getLogger().severe(
                "No spawn locations found!" +
                        "Chosen map: ${mapManager.mainMapManifest?.name}," +
                        "Online players: ${Bukkit.getOnlinePlayers().size},"
            )

            "§cThis game lobby is broken.".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
            return
        }

        "§6You will be teleported in the next 10 seconds.".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)

        WorldUtils.cleanUpWorld(spawnLocations[0].toBukkitLocation().world)
        var playerIndex = 0
        teleportedPlayers.clear()
        var teleportDelay = (10 * 20 / players.size).toLong()
        if (teleportDelay > 20) {
            teleportDelay = 20
        }
        teleportTask = plugin.server.scheduler.runTaskTimer(plugin, {
            if (playerIndex >= players.size) {
                teleportTask?.cancel()
                return@runTaskTimer
            }

            val playerToTeleport = players.getOrNull(playerIndex)
            if (playerToTeleport != null && !teleportedPlayers.contains(playerToTeleport.uniqueId)) {
                if (playerToTeleport.isOnline) {
                    val location = spawnLocations[playerIndex].toBukkitLocation()
                    playerToTeleport.teleport(
                        Location(
                            location.world,
                            location.blockX + 0.5,
                            location.y + 1,
                            location.blockZ + 0.5,
                            location.yaw,
                            location.pitch
                        )
                    )
                    playerToTeleport.velocity = Vector(0, 0, 0)
                    teleportedPlayers.add(playerToTeleport.uniqueId)
                }
            }

            playerIndex++
        }, 0, teleportDelay)
    }
}