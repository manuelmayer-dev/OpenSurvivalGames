package dev.suchbyte.openSurvivalGames.core.listeners.world

import dev.suchbyte.openSurvivalGames.common.Constants
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BlockBreakListener(
    private val gameManager: BaseGameManager
) : Listener {
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) {
            return
        }

        if (!gameManager.alivePlayers.contains(event.player)) {
            event.isCancelled = true
            return
        }

        if (gameManager.currentGameState == GameState.InGame
            && !Constants.blockBreakWhitelist.contains(event.block.type)) {
            event.isCancelled = true
        }

        if (gameManager.currentGameState == GameState.DeathMatch
            && !Constants.deathMatchBlockBreakWhitelist.contains(event.block.type)) {
            event.isCancelled = true
        }
    }
}