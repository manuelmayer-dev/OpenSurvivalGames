package dev.suchbyte.openSurvivalGames.presentation.gui.listeners

import dev.suchbyte.openSurvivalGames.presentation.gui.base.Menu
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MenuListener: Listener {
    @EventHandler
    fun onMenuClick(event: InventoryClickEvent) {
        val holder = event.inventory.holder
        if (holder !is Menu) {
           return
        }

        event.isCancelled = true
        if (event.currentItem == null) {
            return
        }

        holder.handleMenu(event)
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val holder = event.inventory.holder
        if (holder !is Menu) {
            return
        }

        holder.onClose()
    }
}