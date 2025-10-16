package dev.suchbyte.openSurvivalGames.core.listeners.chat

import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener(
    private val gameManager: GameManager,
    private val statsManager: StatsManager
) : Listener {
    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        var message = event.message.replace("%", "%%")

        when (gameManager.currentGameState) {
            GameState.Lobby -> {
                val points = statsManager.getStatsWithScrambled(event.player).points
                event.format = "§e$points §8▏ ${event.player.displayName} §8» §f$message"
            }

            GameState.Warmup -> {
                event.format = "${event.player.displayName} §8» §f$message"
            }

            GameState.InGame, GameState.DeathMatch -> {
                if (gameManager.alivePlayers.contains(event.player)) {
                    event.format = "${event.player.displayName} §8» §f$message"
                } else {
                    event.isCancelled = true
                    message = formatDeadPlayerMessage(event.player, message)
                    for (player in gameManager.spectatingPlayers) {
                        player.sendMessage(message)
                    }
                }
            }

            GameState.Ending -> {
                if (gameManager.alivePlayers.contains(event.player)) {
                    event.format = "${event.player.displayName} §8» §f$message"
                } else {
                    event.format = formatDeadPlayerMessage(event.player, message)
                }
            }
        }
    }

    private fun formatDeadPlayerMessage(player: Player, message: String): String {
        val points = statsManager.getStatsWithScrambled(player).points
        return "§e$points §8▎ §4DEAD §8▏ ${player.displayName} §8» §f$message"
    }
}