package dev.suchbyte.openSurvivalGames.core.listeners.inventory

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent

class PrepareItemCraftListener : Listener {
    private val blockedItems = listOf(
        Material.SHEARS,
        Material.BUCKET
    )

    @EventHandler
    fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
        if (event.recipe == null) {
            return
        }

        if (blockedItems.contains(event.recipe.result.type)) {
            event.inventory.result = null
        }
    }
}