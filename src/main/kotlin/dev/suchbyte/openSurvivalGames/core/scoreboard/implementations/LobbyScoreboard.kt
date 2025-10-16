package dev.suchbyte.openSurvivalGames.core.scoreboard.implementations

import dev.suchbyte.openSurvivalGames.OpenSurvivalGamesPlugin
import dev.suchbyte.openSurvivalGames.common.utils.DateUtils
import dev.suchbyte.openSurvivalGames.core.scoreboard.builders.ScoreboardBuilder
import dev.suchbyte.openSurvivalGames.core.game.states.LobbyGameStateHandler
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard

class LobbyScoreboard(
    private val plugin: OpenSurvivalGamesPlugin,
    player: Player,
    scoreboard: Scoreboard,
    private val lobbyGameStateHandler: LobbyGameStateHandler,
    private val statsManager: StatsManager
) : ScoreboardBuilder(player, scoreboard, "sgLobbyStats") {
    init {
        createScoreboard()
    }

    private var globalStatistics = false
    private var statisticsChanged: Long = 0

    override fun createScoreboard() {
        setDisplayName("§3SurvivalGames")

        setSpacer(15)
        setScore(ChatColor.GREEN, "§lWarmUp in", 14)
        setScore(
            ChatColor.WHITE,
            DateUtils.formatSecondsToMinutesAndSeconds(lobbyGameStateHandler.countdown.toLong()),
            13
        )
        setSpacer(12)
        setScore(ChatColor.GOLD, "§lPlayers Waiting", 11)
        setScore(ChatColor.WHITE, Bukkit.getOnlinePlayers().size.toString(), 10)
        setSpacer(9)
        setScore(ChatColor.AQUA, "§lPlayer Stats", 8)
        setScore(ChatColor.GRAY, "Kills: §f...", 7)
        setScore(ChatColor.GRAY, "Deaths: §f...", 6)
        setScore(ChatColor.GRAY, "Points: §f...", 5)
        setScore(ChatColor.GRAY, "Victories: §f...", 4)
        setScore(ChatColor.GRAY, "Crates: §f...", 3)
        setScore(ChatColor.GRAY, "DeathMatches: §f...", 2)
        setScore(ChatColor.GRAY, "Games Played: §f...", 1)

        update()
    }

    override fun update() {
        updateContent(DateUtils.formatSecondsToMinutesAndSeconds(lobbyGameStateHandler.countdown.toLong()), 13)

        val waitingPlayers = Bukkit.getOnlinePlayers().size
        updateContent(waitingPlayers.toString(), 10)


        val playerStats =
            if (globalStatistics) statsManager.getStatsWithScrambled(player)
            else statsManager.getSeasonStatsWithScrambled(player)

        if (globalStatistics) {
            if (statsManager.isPlayerScrambled(player)) {
                updateContent("§c§lGlobal Scrambled", 8)
            } else {
                updateContent("§b§lGlobal Stats", 8)
            }
        } else {
            if (statsManager.isPlayerScrambled(player)) {
                updateContent("§c§lSeason Scrambled", 8)
            } else {
                updateContent("§e§lSeason Stats", 8)
            }
        }

        updateContent("Kills: §f${playerStats.kills}", 7)
        updateContent("Deaths: §f${playerStats.deaths}", 6)
        updateContent("Points: §f${playerStats.points}", 5)
        updateContent("Victories: §f${playerStats.wins}", 4)
        updateContent("Crates: §f${playerStats.chestsOpened}", 3)
        updateContent("DeathMatches: §f${playerStats.deathMatches}", 2)
        updateContent("Games Played: §f${playerStats.gamesPlayed}", 1)

        if (System.currentTimeMillis() - statisticsChanged > 1000*5) {
            globalStatistics = !globalStatistics
            statisticsChanged = System.currentTimeMillis()
        }
    }
}