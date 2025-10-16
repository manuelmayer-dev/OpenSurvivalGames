package dev.suchbyte.openSurvivalGames.core.listeners.entity

import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent

class EntityExplodeListener(
    private val gameManager: BaseGameManager
) : Listener {
    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent) {
        if (gameManager.currentGameState != GameState.Ending) {
            event.blockList().clear()
        }
    }
}