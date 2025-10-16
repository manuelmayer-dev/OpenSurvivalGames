package dev.suchbyte.openSurvivalGames.core.listeners.inventory

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.EnchantingInventory
import org.bukkit.inventory.ItemStack

class InventoryOpenListener : Listener {
    @EventHandler
    fun onOpen(event: InventoryOpenEvent) {
        if (event.inventory is EnchantingInventory) {
            val enchantingInventory = event.inventory as EnchantingInventory
            enchantingInventory.setItem(1, ItemStack(Material.INK_SACK, 64, 4.toShort()))
        }
    }
}