package dev.suchbyte.openSurvivalGames.core.listeners.entity

import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent

class FoodLevelChangeListener(
    private val gameManager: BaseGameManager
) : Listener {
    @EventHandler
    fun onFoodLevelChange(event: FoodLevelChangeEvent) {
        if (!gameManager.isInGame() || !gameManager.alivePlayers.contains(event.entity)) {
            event.isCancelled = true
        }
    }
}