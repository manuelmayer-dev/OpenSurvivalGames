package dev.suchbyte.openSurvivalGames.core.listeners.entity

import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetLivingEntityEvent

class EntityTargetLivingEntityListener(
    private val gameManager: BaseGameManager
) : Listener {
    @EventHandler
    fun onEntityTargetLivingEntity(event: EntityTargetLivingEntityEvent) {
        event.isCancelled = !gameManager.alivePlayers.contains(event.target)
    }
}