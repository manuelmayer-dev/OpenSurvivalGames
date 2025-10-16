package dev.suchbyte.openSurvivalGames.core.listeners.server

import dev.suchbyte.openSurvivalGames.core.maps.managers.MapManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent

class ServerListPingListener(
    private val mapManager: MapManager
) : Listener {
    @EventHandler
    fun onServerListPingEvent(event: ServerListPingEvent) {
        event.motd = getMessage()
    }

    private fun getMessage(): String {
        return if (mapManager.mainMapManifest == null) "Voting" else mapManager.mainMapManifest!!.name.take(16)
    }
}