package dev.suchbyte.openSurvivalGames.core.listeners.player

import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class PlayerPreLoginListener(
    private val gameManager: GameManager
) : Listener {
    @EventHandler
    fun onPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
        if (gameManager.currentGameState == GameState.Warmup || gameManager.currentGameState == GameState.Ending) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cYou can't join this server now!")
        }
    }
}