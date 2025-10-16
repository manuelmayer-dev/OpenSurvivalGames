package dev.suchbyte.openSurvivalGames.core.scoreboard.implementations

import dev.suchbyte.openSurvivalGames.common.utils.DateUtils
import dev.suchbyte.openSurvivalGames.core.scoreboard.builders.ScoreboardBuilder
import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard

class Hive2014InGameScoreboard(
    player: Player,
    scoreboard: Scoreboard,
    private val statsManager: StatsManager,
    private val gameManager: GameManager
) : ScoreboardBuilder(player, scoreboard, "sgInGameStats") {
    init {
        createScoreboard()
    }

    override fun createScoreboard() {
        setDisplayName(getTitle())
        setScore(ChatColor.RED, "Kills:", 11)
        setScore(ChatColor.YELLOW, "", 10)
        setSpacer(9)
        setScore(ChatColor.AQUA, "Remaining:", 8)
        setScore(ChatColor.YELLOW, "", 7)
        setSpacer(6)
        setScore(ChatColor.AQUA, "Spectators:", 5)
        setScore(ChatColor.YELLOW, "", 4)
        setSpacer(3)
        setScore(ChatColor.AQUA, "Game ID:", 2)
        setScore(ChatColor.YELLOW, "-", 1)

        update()
    }

    override fun update() {
        setDisplayName(getTitle())

        val roundStats = statsManager.getRoundStats(player)
        updateContent(roundStats.kills.toString(), 10)
        updateContent(gameManager.alivePlayers.size.toString(), 7)
        updateContent(gameManager.spectatingPlayers.size.toString(), 4)
    }

    private fun getTitle(): String {
        return "§bSG " +
                "§3${DateUtils.formatSecondsToMinutesAndSeconds(gameManager.getTimeLeft().toLong())}"
    }
}