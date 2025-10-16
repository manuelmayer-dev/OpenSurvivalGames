package dev.suchbyte.openSurvivalGames.core.listeners.world

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockSpreadEvent

class BlockSpreadListener : Listener {
    @EventHandler
    fun onBlockSpread(event: BlockSpreadEvent) {
        if(event.newState.type == Material.FIRE){
            event.isCancelled = true;
        }
    }
}