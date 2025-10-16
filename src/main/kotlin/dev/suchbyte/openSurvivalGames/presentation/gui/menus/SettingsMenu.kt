package dev.suchbyte.openSurvivalGames.presentation.gui.menus

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.presentation.gui.base.Menu
import dev.suchbyte.openSurvivalGames.presentation.gui.base.PlayerMenuUtility
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.ArrowTrailManager
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.BattleCryManager
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.CosmeticManager
import dev.suchbyte.openSurvivalGames.core.scoreboard.managers.ScoreboardManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent

class SettingsMenu(
    private val playerMenuUtility: PlayerMenuUtility,
    private val statsManager: StatsManager,
    private val cosmeticManager: CosmeticManager,
    private val battleCryManager: BattleCryManager,
    private val arrowTrailsManager: ArrowTrailManager,
    private val scoreboardManager: ScoreboardManager
) : Menu(playerMenuUtility) {
    override fun getMenuName(): String {
        return "Settings"
    }

    override fun getSlots(): Int {
        return 18
    }

    override fun handleMenu(event: InventoryClickEvent) {
        when (event.slot) {
            0 -> ResetStatsMenu(playerMenuUtility, statsManager) { onOpen() }.onOpen()
            2 -> handleCosmeticClick(Cosmetics.SGMySword)
            4 -> handleCosmeticClick(Cosmetics.SGDeathCrate)
            6 -> BattleCriesMenu(
                playerMenuUtility,
                cosmeticManager,
                battleCryManager
            ) { onOpen() }.onOpen()

            8 -> ArrowTrailsMenu(
                playerMenuUtility,
                cosmeticManager,
                arrowTrailsManager
            ) { onOpen() }.onOpen()
            9 -> ScoreboardSelectionMenu(playerMenuUtility, scoreboardManager) { onOpen() }.onOpen()
        }
    }

    private fun handleCosmeticClick(cosmetic: Cosmetics) {
        cosmeticManager.toggleCosmetic(playerMenuUtility.getOwner(), cosmetic) {
            if (it) {
                playerMenuUtility.getOwner()
                    .sendMessageWithPrefix("§eYou enabled §b${cosmetic.displayName}§e!", Prefix.SurvivalGames)
            } else {
                playerMenuUtility.getOwner()
                    .sendMessageWithPrefix("§eYou disabled §b${cosmetic.displayName}§e!", Prefix.SurvivalGames)
            }

            setMenuItems()
        }
    }

    override fun setMenuItems() {
        menuInventory.setItem(
            0,
            makeItem(Material.NAME_TAG, "§cReset Stats", "§fUse a Stat Reset Token", "§fto reset your stats")
        )
        setCosmeticItem(2, Cosmetics.SGMySword)
        setCosmeticItem(4, Cosmetics.SGDeathCrate)
        menuInventory.setItem(
            6,
            makeItem(Material.NOTE_BLOCK, "§eBattle Cries")
        )
        menuInventory.setItem(
            8,
            makeItem(Material.ARROW, "§eArrow Trails")
        )
        menuInventory.setItem(9, makeItem(Material.SIGN, "§eIngame Scoreboard"))
    }

    private fun setCosmeticItem(slot: Int, cosmetics: Cosmetics) {
        val displayName = "§e${cosmetics.displayName}"
        var material = Material.AIR
        val enabled = cosmeticManager.hasEnabled(playerMenuUtility.getOwner(), cosmetics)
        when (cosmetics) {
            Cosmetics.SGMySword -> {
                material = Material.IRON_SWORD
            }

            Cosmetics.SGDeathCrate -> {
                material = Material.CHEST
            }

            else -> {}
        }

        menuInventory.setItem(
            slot,
            makeItem(material, displayName, if (enabled) "§aEnabled" else "§cDisabled")
        )
    }
}