package dev.suchbyte.openSurvivalGames.presentation.gui.menus

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.presentation.gui.base.Menu
import dev.suchbyte.openSurvivalGames.presentation.gui.base.PlayerMenuUtility
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.BattleCryManager
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.CosmeticManager
import dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries.BattleCry
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent

class BattleCriesMenu(
    private val playerMenuUtility: PlayerMenuUtility,
    private val cosmeticManager: CosmeticManager,
    private val battleCryManager: BattleCryManager,
    private val back: () -> Unit
) : Menu(playerMenuUtility) {
    override fun getMenuName(): String {
        return "Battle Cries"
    }

    override fun getSlots(): Int {
        val battleCries = battleCryManager.battleCries.size
        val reservedSlots = 2
        val requiredSlots = battleCries + reservedSlots
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
            0 -> setBattleCry(null)
            menuInventory.size - 1 -> back()
            else -> {
                val battleCry = battleCryManager.battleCries.getOrNull(event.slot - 1) ?: return
                battleCry.play(listOf(playerMenuUtility.getOwner()))
                setBattleCry(battleCry)
            }
        }
    }

    private fun setBattleCry(battleCry: BattleCry?) {
        cosmeticManager.disableCosmetics(
            playerMenuUtility.getOwner(),
            battleCryManager.battleCries.map { x -> x.cosmetic }) {
            if (battleCry != null) {
                cosmeticManager.enableCosmetic(playerMenuUtility.getOwner(), battleCry.cosmetic)
                playerMenuUtility.getOwner()
                    .sendMessageWithPrefix("§eYou enabled the battle cry §b${battleCry.cosmetic.displayName}§e!")
            } else {
                playerMenuUtility.getOwner().sendMessageWithPrefix("§eYou disabled your battle cry!")
            }

            setMenuItems()
        }
    }

    override fun setMenuItems() {
        menuInventory.setItem(0, makeItem(Material.REDSTONE_BLOCK, "§cDisable"))

        var index = 1
        battleCryManager.battleCries.forEach {
            setCosmeticItem(index, it.cosmetic)
            index++
        }

        menuInventory.setItem(menuInventory.size - 1, makeItem(Material.STAINED_CLAY, 14, "§cBack"))
    }

    private fun setCosmeticItem(slot: Int, cosmetics: Cosmetics) {
        val displayName = "§e${cosmetics.displayName}"
        val material = Material.NOTE_BLOCK
        val enabled = cosmeticManager.hasEnabled(playerMenuUtility.getOwner(), cosmetics)
        menuInventory.setItem(
            slot,
            makeItem(material, displayName, if (enabled) "§aEnabled" else "§cDisabled")
        )
    }
}