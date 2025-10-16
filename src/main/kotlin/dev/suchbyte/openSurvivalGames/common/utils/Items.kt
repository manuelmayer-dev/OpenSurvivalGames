package dev.suchbyte.openSurvivalGames.common.utils

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta


class Items {
    companion object {
        val teleporterItem = makeItem(Material.COMPASS, ChatColor.WHITE, "Teleporter")
        val leftServerItem = makeItem(Material.SLIME_BALL, ChatColor.RED, "Back to hub")
        val settingsItem = makeItem(Material.NETHER_STAR, ChatColor.GRAY, "Settings")
        val setSpawnItem = makeItem(Material.NAME_TAG, ChatColor.GRAY, "Set Map Spawn")
        val instructionBookSurvivalGamesClassic = makeInstructionBookSurvivalGamesClassic()

        private fun makeInstructionBookSurvivalGamesClassic(): ItemStack {
            val writtenBook: ItemStack = makeItem(Material.WRITTEN_BOOK, ChatColor.WHITE, "How to play?")
            val bookMeta = writtenBook.itemMeta as BookMeta
            bookMeta.setTitle("How to play")
            bookMeta.author = "SuchByte"

            val pages: MutableList<String> = ArrayList()
            pages.add(
                "Welcome to\n" +
                        "§3SurvivalGames Classic\n" +
                        "\n" +
                        "§0§lWhat is SurvivalGames?\n" +
                        "§rSurvival Games is a battle royale mode where 24 players battle to be the last standing."
            )

            pages.add(
                "§lHow to play?\n" +
                        "§rWhen the game starts, either run to the center of the spawn or run away to find stuff in supply " +
                        "crates (Ender Chests). Supply crates are spread over the entire map. After you found stuff, try " +
                        "to kill everyone else to win the SurvivalGames!"
            )

            bookMeta.pages = pages
            writtenBook.itemMeta = bookMeta

            return writtenBook
        }

        private fun makeItem(material: Material, color: ChatColor, name: String): ItemStack {
            val item = ItemStack(material, 1)
            val itemMeta = item.itemMeta
            itemMeta!!.displayName = color.toString() + name
            item.itemMeta = itemMeta
            return item
        }
    }
}