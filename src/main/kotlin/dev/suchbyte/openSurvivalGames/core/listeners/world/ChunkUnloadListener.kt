package dev.suchbyte.openSurvivalGames.core.listeners.world

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkUnloadEvent

class ChunkUnloadListener : Listener {
    @EventHandler
    fun onChunkUnload(event: ChunkUnloadEvent) {
        event.isCancelled = true
    }
}