package dev.suchbyte.openSurvivalGames.core.listeners.world

import dev.suchbyte.openSurvivalGames.common.Constants
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkLoadEvent

class ChunkLoadListener : Listener {
    @EventHandler
    fun onChunkLoad(event: ChunkLoadEvent) {
        event.chunk.entities.filter { x -> !Constants.entityWhitelist.contains(x.type) }.forEach { entity ->
            entity.remove()
        }
    }
}