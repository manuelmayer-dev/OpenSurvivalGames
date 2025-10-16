package dev.suchbyte.openSurvivalGames.core.listeners.world

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFadeEvent

class BlockFadeListener : Listener {
    @EventHandler
    fun onBlockFade(event: BlockFadeEvent) {
        if (event.block.type == Material.ICE || event.block.type == Material.PACKED_ICE) {
            event.isCancelled = true
        }
    }
}