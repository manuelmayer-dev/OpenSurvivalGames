package dev.suchbyte.openSurvivalGames.infrastructure.tasks

import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class HideSpectatorsRunnable(
    private val gameManager: BaseGameManager
) : BukkitRunnable() {
    override fun run() {
        Bukkit.getOnlinePlayers().forEach { player ->
            gameManager.spectatingPlayers.forEach { spectator ->
                player.hidePlayer(spectator)
            }
        }
    }
}