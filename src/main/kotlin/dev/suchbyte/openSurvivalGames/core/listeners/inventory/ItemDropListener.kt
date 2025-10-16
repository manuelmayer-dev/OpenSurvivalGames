package dev.suchbyte.openSurvivalGames.core.listeners.inventory

import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class ItemDropListener(
    private val gameManager: BaseGameManager
) : Listener {
    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) {
        if ((gameManager.currentGameState != GameState.InGame && gameManager.currentGameState != GameState.DeathMatch)
            || !gameManager.alivePlayers.contains(event.player)) {
            event.isCancelled = true
        }
    }
}