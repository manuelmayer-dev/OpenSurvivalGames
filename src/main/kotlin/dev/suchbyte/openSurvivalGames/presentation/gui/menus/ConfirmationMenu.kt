package dev.suchbyte.openSurvivalGames.presentation.gui.menus

import dev.suchbyte.openSurvivalGames.presentation.gui.base.Menu
import dev.suchbyte.openSurvivalGames.presentation.gui.base.PlayerMenuUtility
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent

class ConfirmationMenu(
    playerMenuUtility: PlayerMenuUtility,
    private val confirm: () -> Unit,
    private val cancel: () -> Unit
) : Menu(playerMenuUtility) {
    override fun getMenuName(): String {
        return "Confirm Action"
    }

    override fun getSlots(): Int {
        return 9
    }

    override fun handleMenu(event: InventoryClickEvent) {
        when (event.slot) {
            0 -> {
                confirm()
            }
            8 -> {
                cancel()
            }
        }
    }

    override fun setMenuItems() {
        menuInventory.setItem(0, makeItem(Material.STAINED_CLAY, 5, "§aConfirm"))
        menuInventory.setItem(8, makeItem(Material.STAINED_CLAY, 14, "§cCancel"))
    }
}