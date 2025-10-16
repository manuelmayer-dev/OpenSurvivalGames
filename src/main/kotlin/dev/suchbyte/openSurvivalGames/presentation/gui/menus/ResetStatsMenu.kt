package dev.suchbyte.openSurvivalGames.presentation.gui.menus

import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import dev.suchbyte.openSurvivalGames.presentation.gui.base.Menu
import dev.suchbyte.openSurvivalGames.presentation.gui.base.PlayerMenuUtility
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class ResetStatsMenu(
    private val playerMenuUtility: PlayerMenuUtility,
    private val statsManager: StatsManager,
    private val cancel: () -> Unit
) : Menu(playerMenuUtility) {
    override fun getMenuName(): String {
        return "Settings"
    }

    override fun getSlots(): Int {
        return 9
    }

    override fun handleMenu(event: InventoryClickEvent) {
        when (event.slot) {
            3 -> handleStatReset(event) {
                statsManager.resetGlobalStats(event.whoClicked as Player)
            }
            8 -> cancel()
        }
    }

    private fun handleStatReset(event: InventoryClickEvent, confirm: () -> Unit) {
        ConfirmationMenu(playerMenuUtility, {
            event.whoClicked.closeInventory()
            confirm.invoke()
        }, {
            onOpen()
        }).onOpen()
    }

    override fun setMenuItems() {
        menuInventory.setItem(
            3,
            makeItem(Material.SIGN, "§2Reset Global Stats")
        )
        menuInventory.setItem(8, makeItem(Material.STAINED_CLAY, 14, "§cBack"))
    }
}