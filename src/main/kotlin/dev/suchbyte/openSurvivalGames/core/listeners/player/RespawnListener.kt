package dev.suchbyte.openSurvivalGames.core.listeners.player

import dev.suchbyte.openSurvivalGames.common.extensions.ConfigLocationExtensions.Companion.toBukkitLocation
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.util.Vector

class RespawnListener(
    private val config: PluginConfig,
    private val gameManager: BaseGameManager
) : Listener {
    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        event.player.velocity = Vector(0, 0, 0)
        gameManager.addSpectator(event.player)
        event.respawnLocation = config.lobbyLocation?.toBukkitLocation()
    }
}