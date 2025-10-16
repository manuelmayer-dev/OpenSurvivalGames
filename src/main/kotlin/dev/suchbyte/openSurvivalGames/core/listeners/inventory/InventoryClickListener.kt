package dev.suchbyte.openSurvivalGames.core.listeners.inventory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.EnchantingInventory

class InventoryClickListener : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.inventory is EnchantingInventory && event.slot == 1) {
            event.isCancelled = true
        }
    }
}