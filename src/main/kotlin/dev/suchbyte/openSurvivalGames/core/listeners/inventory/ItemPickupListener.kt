package dev.suchbyte.openSurvivalGames.core.listeners.inventory

import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPickupItemEvent

class ItemPickupListener(
    private val gameManager: BaseGameManager
): Listener {
    @EventHandler
    fun onItemPickup(event: PlayerPickupItemEvent) {
        event.isCancelled = (!gameManager.isInGame() && gameManager.currentGameState != GameState.Ending)
                || !gameManager.alivePlayers.contains(event.player)
    }
}