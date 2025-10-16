package dev.suchbyte.openSurvivalGames.infrastructure.tasks

import dev.suchbyte.openSurvivalGames.core.scoreboard.managers.ScoreboardManager
import org.bukkit.scheduler.BukkitRunnable

class ScoreboardUpdateRunnable(
    private val scoreboardManager: ScoreboardManager
) : BukkitRunnable() {
    override fun run() {
        scoreboardManager.getAllScoreboards().forEach { scoreboardBuilder ->
            scoreboardBuilder.update()
        }
    }
}