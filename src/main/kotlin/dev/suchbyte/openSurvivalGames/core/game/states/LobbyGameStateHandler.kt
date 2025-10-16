package dev.suchbyte.openSurvivalGames.core.game.states

import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import dev.suchbyte.openSurvivalGames.presentation.commands.admin.ForceMapCommand
import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import dev.suchbyte.openSurvivalGames.OpenSurvivalGamesPlugin
import dev.suchbyte.openSurvivalGames.infrastructure.caching.ScoreboardCache
import dev.suchbyte.openSurvivalGames.presentation.commands.admin.ForceStartCommand
import dev.suchbyte.openSurvivalGames.presentation.commands.player.VoteCommand
import dev.suchbyte.openSurvivalGames.presentation.commands.admin.StartCommand
import dev.suchbyte.openSurvivalGames.presentation.commands.admin.CancelCommand
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.common.extensions.StringExtensions.Companion.sendToOnlinePlayersWithPrefix
import dev.suchbyte.openSurvivalGames.core.maps.managers.MapManager
import dev.suchbyte.openSurvivalGames.core.maps.managers.MapVoting
import dev.suchbyte.openSurvivalGames.core.scoreboard.implementations.LobbyScoreboard
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import dev.suchbyte.openSurvivalGames.common.utils.WorldUtils
import dev.suchbyte.openSurvivalGames.core.scoreboard.managers.ScoreboardManager
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.WorldCreator
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitTask

class LobbyGameStateHandler(
    private val plugin: OpenSurvivalGamesPlugin,
    private val config: PluginConfig,
    private val mapManager: MapManager,
    private val scoreboardManager: ScoreboardManager,
    private val statsManager: StatsManager,
    private val gameManager: GameManager
) : GameStateHandler(plugin, GameState.Lobby) {
    var countdown = config.initialLobbyCountdown + 1
        private set

    private var task: BukkitTask? = null
    private var ignoreMinimumPlayers = false

    private val mapVoting = MapVoting(
        config.maps.filter { x -> x.enabled }
            .shuffled()
            .sortedByDescending { x -> x.newMap }
            .sortedByDescending { x -> x.seasonMap }
            .take(5)
    ) {
        listOf("§6Time remaining: §b$countdown §6seconds.", "§6Players waiting: §4${Bukkit.getOnlinePlayers().count()}")
    }

    override fun onEnable() {
        super.onEnable()
        plugin.getCommand("forcestart").executor = ForceStartCommand(this)
        plugin.getCommand("vote").executor = VoteCommand(mapVoting, gameManager)
        plugin.getCommand("start").executor = StartCommand {
            shortenCountdown()
        }
        plugin.getCommand("cancel").executor = CancelCommand {
            stopCountdown()
            "§aThe countdown has been cancelled.".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
        }
        plugin.getCommand("forcemap").executor =
            ForceMapCommand(config, mapManager, gameManager, mapVoting)
        mapVoting.startVoting()
        loadLobbyMap()
    }

    override fun onDisable() {
        super.onDisable()
        plugin.getCommand("vote").executor = null
        plugin.getCommand("start").executor = null
        plugin.getCommand("cancel").executor = null
        plugin.getCommand("forcestart").executor = null
        plugin.getCommand("forcemap").executor = null
    }

    @EventHandler
    fun onPlayerDisconnect(event: PlayerQuitEvent) {
        if (Bukkit.getOnlinePlayers().size > 1) {
            return
        }

        mapManager.reset()
        mapVoting.startVoting()
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        startCountdown()

        scoreboardManager.registerScoreboard(
            event.player,
            LobbyScoreboard(plugin, event.player, ScoreboardCache.getScoreboard(event.player), this, statsManager)
        )

        mapVoting.sendVotableMaps(event.player)

        if (Bukkit.getOnlinePlayers().size >= config.minPlayersToTriggerVotingShortening) {
            shortenCountdown()
        }
    }

    fun forceStart() {
        ignoreMinimumPlayers = true
        shortenCountdown()
        startCountdown()
    }

    fun stopCountdown() {
        task?.cancel()
        task = null
        countdown = config.initialLobbyCountdown + 1
    }

    fun shortenCountdown() {
        if (countdown <= 11) {
            return
        }

        countdown = 11
    }

    private fun loadLobbyMap() {
        val lobbyConfigLocation = config.lobbyLocation ?: return
        val worldName = lobbyConfigLocation.worldName
        val world = WorldCreator(worldName).createWorld()
        world.setSpawnLocation(
            lobbyConfigLocation.x.toInt(),
            lobbyConfigLocation.y.toInt(),
            lobbyConfigLocation.z.toInt()
        )
        world.setGameRuleValue("doFireTick", "false")
        world.setGameRuleValue("doDaylightCycle", "false")
        world.setGameRuleValue("naturalRegeneration", "true")
        world.time = 0
        WorldUtils.cleanUpWorld(world)
    }

    private fun startCountdown() {
        if (task != null) {
            return
        }

        task = plugin.server.scheduler.runTaskTimer(plugin, {
            countdown--

            Bukkit.getOnlinePlayers().forEach { player ->
                player.level = countdown
            }

            if (countdown > 10) {
                if (countdown % 60 == 0) {
                    mapVoting.sendVotableMaps()
                }
            }

            if (countdown <= 10) {
                mapVoting.endVoting { mapManager.setMap(it) }
            }

            if (countdown <= 3) {
                Bukkit.getOnlinePlayers().forEach { player ->
                    player.playSound(player.eyeLocation, Sound.SUCCESSFUL_HIT, 1f, 1f)
                }
            }

            if (countdown < 1) {
                stopCountdown()
                checkStart()
            }
        }, 20, 20)
    }

    private fun checkStart() {
        if (Bukkit.getOnlinePlayers().size < config.minPlayers && !ignoreMinimumPlayers) {
            "§cNot enough players are online. §6${config.minPlayers} §cplayers are required to start the game."
                .sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)

            stopCountdown()
            startCountdown()
            mapManager.reset()
            mapVoting.startVoting()
            return
        }

        onDisable()
    }
}