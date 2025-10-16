package dev.suchbyte.openSurvivalGames.core.scoreboard.implementations

import dev.suchbyte.openSurvivalGames.common.utils.DateUtils
import dev.suchbyte.openSurvivalGames.core.scoreboard.builders.ScoreboardBuilder
import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard

class Hive2013InGameScoreboard(
    player: Player,
    scoreboard: Scoreboard,
    private val gameManager: GameManager
) : ScoreboardBuilder(player, scoreboard, "sgInGameStats") {
    init {
        createScoreboard()
    }

    override fun createScoreboard() {
        setDisplayName(getTitle())
        setScore(ChatColor.GOLD, "§lGame ID:", 1)
        setScore(ChatColor.AQUA, "-", 0)

        update()
    }

    override fun update() {
        setDisplayName(getTitle())
    }

    private fun getTitle(): String {
        return "§bSG " +
                "§4${DateUtils.formatSecondsToMinutesAndSeconds(gameManager.getTimeLeft().toLong())}"
    }
}