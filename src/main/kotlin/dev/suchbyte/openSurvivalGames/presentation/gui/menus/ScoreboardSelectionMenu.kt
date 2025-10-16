package dev.suchbyte.openSurvivalGames.presentation.gui.menus

import dev.suchbyte.openSurvivalGames.presentation.gui.base.Menu
import dev.suchbyte.openSurvivalGames.presentation.gui.base.PlayerMenuUtility
import dev.suchbyte.openSurvivalGames.domain.scoreboard.ScoreboardType
import dev.suchbyte.openSurvivalGames.core.scoreboard.managers.ScoreboardManager
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent

class ScoreboardSelectionMenu(
    private val playerMenuUtility: PlayerMenuUtility,
    private val scoreboardManager: ScoreboardManager,
    private val back: () -> Unit
) : Menu(playerMenuUtility) {
    override fun getMenuName(): String {
        return "Select Scoreboard"
    }

    override fun getSlots(): Int {
        return 9
    }

    override fun handleMenu(event: InventoryClickEvent) {
        when (event.slot) {
            0 -> selectScoreboard(ScoreboardType.Disabled)
            1 -> selectScoreboard(ScoreboardType.Hive2013)
            2 -> selectScoreboard(ScoreboardType.Hive2014)
            3 -> selectScoreboard(ScoreboardType.Modern)
            8 -> back()
        }
    }

    private fun selectScoreboard(type: ScoreboardType) {
        this.scoreboardManager.setInGameScoreboard(playerMenuUtility.getOwner(), type) {
            setMenuItems()
        }
    }

    override fun setMenuItems() {
        menuInventory.setItem(
            0,
            makeItem(Material.REDSTONE_BLOCK, "§cDisable", getScoreboardLore(ScoreboardType.Disabled))
        )
        menuInventory.setItem(1, makeItem(Material.SIGN, "§eHive 2013", getScoreboardLore(ScoreboardType.Hive2013)))
        menuInventory.setItem(
            2,
            makeItem(Material.SIGN, "§eHive 2014", getScoreboardLore(ScoreboardType.Hive2014))
        )
        menuInventory.setItem(
            3, makeItem(Material.SIGN, "§eModern (SG 2 inspired)", getScoreboardLore(ScoreboardType.Modern))
        )
        menuInventory.setItem(8, makeItem(Material.STAINED_CLAY, 14, "§cBack"))
    }

    private fun getScoreboardLore(type: ScoreboardType): List<String> {
        val currentScoreboard = this.scoreboardManager.getSelectedInGameScoreboard(playerMenuUtility.getOwner())
        val enabled = type == currentScoreboard
        val preview = when (type) {
            ScoreboardType.Disabled -> listOf("§fShow no Scoreboard")
            ScoreboardType.Hive2013 -> listOf("§6§lGame ID", "§b154212200")
            ScoreboardType.Hive2014 -> listOf(
                "§cKills",
                "§e1",
                "  ",
                "§bRemaining",
                "§e23",
                "   ",
                "§bSpectators",
                "§e1",
                "    ",
                "§bGame ID",
                "§e154212200"
            )

            ScoreboardType.Modern -> listOf(
                "§a§lPlayers Left",
                "§f23",
                " ",
                "§b§lRound Stats",
                "§7Kills: §f1",
                "§7Points: §f230",
                "§7Crates: §f2",
                "  ",
                "§e§lGame ID",
                "§f154212200"
            )
        }

        return preview.union(listOf("     ", if (enabled) "§aEnabled" else "§cDisabled")).toList()
    }
}