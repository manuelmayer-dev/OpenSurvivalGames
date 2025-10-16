package dev.suchbyte.openSurvivalGames.core.scoreboard.implementations

import dev.suchbyte.openSurvivalGames.common.utils.DateUtils
import dev.suchbyte.openSurvivalGames.core.scoreboard.builders.ScoreboardBuilder
import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard

class ModernInGameScoreboard(
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
        setSpacer(11)
        setScore(ChatColor.GREEN, "§lPlayers Left", 10)
        setScore(ChatColor.WHITE, gameManager.alivePlayers.size.toString(), 9)
        setSpacer(8)
        setScore(ChatColor.AQUA, "§lRound Stats", 7)
        setScore(ChatColor.GRAY, "Kills: §f0", 6)
        setScore(ChatColor.GRAY, "Points: §f0", 5)
        setScore(ChatColor.GRAY, "Crates: §f0", 4)
        setSpacer(3)
        setScore(ChatColor.YELLOW, "§lGame ID", 2)
        setScore(ChatColor.WHITE, "-", 1)

        update()
    }

    override fun update() {
        setDisplayName(getTitle())

        val roundStats = statsManager.getRoundStats(player)
        updateContent(gameManager.alivePlayers.size.toString(), 9)
        updateContent("Kills: §f${roundStats.kills}", 6)
        updateContent("Points: §f${(roundStats.gainedPoints - roundStats.lostPoints)}", 5)
        updateContent("Crates: §f${roundStats.chestsOpened}", 4)
    }

    private fun getTitle(): String {
        return "§3§lSurvivaGames §8▏ " +
                "§c${DateUtils.formatSecondsToMinutesAndSeconds(gameManager.getTimeLeft().toLong())}"
    }
}