package dev.suchbyte.openSurvivalGames.infrastructure.tasks

import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import org.bukkit.scheduler.BukkitRunnable

class DeathCrateParticlesRunnable(
    private val gameManager: BaseGameManager
) : BukkitRunnable() {
    override fun run() {
        gameManager.deathCrateParticles()
    }
}