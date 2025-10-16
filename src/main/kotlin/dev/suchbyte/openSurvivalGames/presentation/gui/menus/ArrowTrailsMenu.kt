package dev.suchbyte.openSurvivalGames.presentation.gui.menus

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.presentation.gui.base.Menu
import dev.suchbyte.openSurvivalGames.presentation.gui.base.PlayerMenuUtility
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.ArrowTrailManager
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.CosmeticManager
import dev.suchbyte.openSurvivalGames.core.cosmetics.arrowTrails.ArrowTrail
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent

class ArrowTrailsMenu(
    private val playerMenuUtility: PlayerMenuUtility,
    private val cosmeticManager: CosmeticManager,
    private val arrowTrailManager: ArrowTrailManager,
    private val back: () -> Unit
) : Menu(playerMenuUtility) {
    override fun getMenuName(): String {
        return "Arrow Trails"
    }

    override fun getSlots(): Int {
        val arrowTrails = arrowTrailManager.arrowTrails.size
        val reservedSlots = 2
        val requiredSlots = arrowTrails + reservedSlots
        return when {
            requiredSlots <= 9 -> 9
            requiredSlots <= 18 -> 18
            requiredSlots <= 27 -> 27
            requiredSlots <= 36 -> 36
            requiredSlots <= 45 -> 45
            else -> 54
        }
    }

    override fun handleMenu(event: InventoryClickEvent) {
        when (event.slot) {
            0 -> setArrowTrail(null)
            menuInventory.size - 1 -> back()
            else -> {
                val arrowTrail = arrowTrailManager.arrowTrails.getOrNull(event.slot - 1) ?: return
                setArrowTrail(arrowTrail)
            }
        }
    }

    private fun setArrowTrail(arrowTrail: ArrowTrail?) {
        cosmeticManager.disableCosmetics(
            playerMenuUtility.getOwner(),
            arrowTrailManager.arrowTrails.map { x -> x.cosmetic }) {
            if (arrowTrail != null) {
                cosmeticManager.enableCosmetic(playerMenuUtility.getOwner(), arrowTrail.cosmetic)
                playerMenuUtility.getOwner()
                    .sendMessageWithPrefix("§eYou enabled the arrow trail §b${arrowTrail.cosmetic.displayName}§e!")
            } else {
                playerMenuUtility.getOwner().sendMessageWithPrefix("§eYou disabled your arrow trail!")
            }

            setMenuItems()
        }
    }

    override fun setMenuItems() {
        menuInventory.setItem(0, makeItem(Material.REDSTONE_BLOCK, "§cDisable"))

        var index = 1
        arrowTrailManager.arrowTrails.forEach {
            setCosmeticItem(index, it.cosmetic)
            index++
        }

        menuInventory.setItem(menuInventory.size - 1, makeItem(Material.STAINED_CLAY, 14, "§cBack"))
    }

    private fun setCosmeticItem(slot: Int, cosmetics: Cosmetics) {
        val displayName = "§e${cosmetics.displayName}"
        val material = Material.ARROW
        val enabled = cosmeticManager.hasEnabled(playerMenuUtility.getOwner(), cosmetics)

        menuInventory.setItem(
            slot,
            makeItem(material, displayName, if (enabled) "§aEnabled" else "§cDisabled")
        )
    }
}