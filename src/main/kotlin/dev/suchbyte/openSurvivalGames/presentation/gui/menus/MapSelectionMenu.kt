package dev.suchbyte.openSurvivalGames.presentation.gui.menus

import dev.suchbyte.openSurvivalGames.presentation.gui.base.Menu
import dev.suchbyte.openSurvivalGames.presentation.gui.base.PlayerMenuUtility
import dev.suchbyte.openSurvivalGames.core.config.MapManifest
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent

class MapSelectionMenu(
    private val playerMenuUtility: PlayerMenuUtility,
    private val selectableMaps: List<MapManifest>,
    private val selected: (MapManifest) -> Unit
) : Menu(playerMenuUtility) {
    private var selectedMap: MapManifest? = null

    override fun getMenuName(): String {
        return "Select the next map"
    }

    override fun getSlots(): Int {
        return when {
            selectableMaps.size <= 9 -> 9
            selectableMaps.size <= 18 -> 18
            selectableMaps.size <= 27 -> 27
            selectableMaps.size <= 36 -> 36
            selectableMaps.size <= 45 -> 45
            else -> 54
        }
    }

    override fun handleMenu(event: InventoryClickEvent) {
        val index = event.slot
        selectedMap = selectableMaps.getOrNull(index) ?: return
        selected(selectedMap!!)
        playerMenuUtility.getOwner().closeInventory()
    }

    override fun setMenuItems() {
        var index = 0
        selectableMaps.forEach { map ->
            menuInventory.setItem(index, makeItem(Material.PAPER, "§f${map.name}", map.author))
            index++
        }
    }
}