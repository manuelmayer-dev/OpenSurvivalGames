package dev.suchbyte.openSurvivalGames.core.listeners.world

import org.bukkit.Material
import org.bukkit.entity.Boat
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityChangeBlockEvent

class EntityChangeBlockListener : Listener {
    @EventHandler
    fun onEntityChangeBlock(event: EntityChangeBlockEvent) {
        if (event.entity is Boat && event.block.type == Material.WATER_LILY) {
            event.isCancelled = true
        }
    }
}