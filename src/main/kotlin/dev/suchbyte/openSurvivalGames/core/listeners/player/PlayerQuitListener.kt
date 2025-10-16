package dev.suchbyte.openSurvivalGames.core.listeners.player

import dev.suchbyte.openSurvivalGames.infrastructure.caching.PlayerMenuUtilityCache
import dev.suchbyte.openSurvivalGames.infrastructure.caching.ScoreboardCache
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import dev.suchbyte.openSurvivalGames.core.scoreboard.managers.ScoreboardManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener(
    private val statsManager: StatsManager,
    private val gameManager: GameManager,
    private val scoreboardManager: ScoreboardManager
) : Listener {
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        event.quitMessage = null
        if (gameManager.currentGameState == GameState.Lobby) {
            statsManager.removeCached(event.player)
        }
        gameManager.playerDisconnected(event.player)
        scoreboardManager.unregisterScoreboard(event.player)
        PlayerMenuUtilityCache.removePlayerMenu(event.player)
        ScoreboardCache.removeScoreboard(event.player)
    }

    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
        event.leaveMessage = ""
    }
}