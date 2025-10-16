package dev.suchbyte.openSurvivalGames.presentation.gui.menus

import dev.suchbyte.openSurvivalGames.common.extensions.StringExtensions.Companion.removeMinecraftColorCodes
import dev.suchbyte.openSurvivalGames.presentation.gui.base.Menu
import dev.suchbyte.openSurvivalGames.presentation.gui.base.PlayerMenuUtility
import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import org.bukkit.Material
import org.bukkit.SkullType
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class PlayerTeleporterMenu(
    private val gameManager: GameManager,
    private val playerMenuUtility: PlayerMenuUtility,
    private val statsManager: StatsManager
) : Menu(playerMenuUtility) {
    override fun getMenuName(): String {
        return "Teleport to a player"
    }

    override fun getSlots(): Int {
        return when {
            gameManager.alivePlayers.size <= 9 -> 9
            gameManager.alivePlayers.size <= 18 -> 18
            gameManager.alivePlayers.size <= 27 -> 27
            gameManager.alivePlayers.size <= 36 -> 36
            gameManager.alivePlayers.size <= 45 -> 45
            else -> 54
        }
    }

    override fun handleMenu(event: InventoryClickEvent) {
        val whoClicked = event.whoClicked as Player
        whoClicked.closeInventory()
        val itemDisplayName = event.currentItem?.itemMeta?.displayName ?: return
        val destPlayer = gameManager.alivePlayers.find { x ->
            x.player.customName.removeMinecraftColorCodes() == itemDisplayName.removeMinecraftColorCodes()
        }

        if (destPlayer != null) {
            whoClicked.allowFlight = true
            whoClicked.isFlying = true
            whoClicked.teleport(destPlayer.location.add(0.0, 2.0, 0.0))
        }

        whoClicked.fireTicks = 0
        whoClicked.exp = 0f
        whoClicked.level = 0
    }

    override fun setMenuItems() {
        gameManager.alivePlayers.forEach { player ->
            val skull = ItemStack(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal.toShort())
            val meta = skull.itemMeta as SkullMeta
            meta.displayName = player.customName
            meta.owner = player.customName.removeMinecraftColorCodes()

            val roundStats = statsManager.getRoundStats(player)
            meta.lore = listOf(
                "§cKills: §f${roundStats.kills}"
            )
            skull.itemMeta = meta
            inventory.addItem(skull)
        }
    }
}