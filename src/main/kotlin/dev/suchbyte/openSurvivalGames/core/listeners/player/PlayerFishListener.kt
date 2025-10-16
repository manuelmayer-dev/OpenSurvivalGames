package dev.suchbyte.openSurvivalGames.core.listeners.player

import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent

class PlayerFishListener(
    private val gameManager: BaseGameManager,
    private val statsManager: StatsManager
) : Listener {
    @EventHandler
    fun onPlayerFish(event: PlayerFishEvent) {
        if (!gameManager.isInGame()) {
            return
        }

        if (event.state == PlayerFishEvent.State.FISHING && isPlayerNearby(event.player)) {
            statsManager.rodThrownOut(event.player)
        }
    }

    private fun isPlayerNearby(player: Player): Boolean {
        gameManager.alivePlayers.forEach { alivePlayer ->
            if (player.location.distance(alivePlayer.location) <= 18) {
                val directionToPlayer = alivePlayer.location.toVector().subtract(player.location.toVector()).normalize()
                val playerDirection = player.location.direction.normalize()
                val dotProduct = playerDirection.dot(directionToPlayer)
                if (dotProduct > 0.9) {
                    return true
                }
            }
        }

        return false
    }
}