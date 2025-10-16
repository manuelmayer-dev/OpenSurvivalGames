package dev.suchbyte.openSurvivalGames.domain.scoreboard

import org.bukkit.ChatColor

enum class EntryName(val entry: Int, val entryName: String) {
    ENTRY_0(0, ChatColor.BLACK.toString()),
    ENTRY_1(1, ChatColor.DARK_BLUE.toString()),
    ENTRY_2(2, ChatColor.DARK_GREEN.toString()),
    ENTRY_3(3, ChatColor.DARK_AQUA.toString()),
    ENTRY_4(4, ChatColor.DARK_RED.toString()),
    ENTRY_5(5, ChatColor.DARK_PURPLE.toString()),
    ENTRY_6(6, ChatColor.GOLD.toString()),
    ENTRY_7(7, ChatColor.GRAY.toString()),
    ENTRY_8(8, ChatColor.DARK_GRAY.toString()),
    ENTRY_9(9, ChatColor.BLUE.toString()),
    ENTRY_10(10, ChatColor.GREEN.toString()),
    ENTRY_11(11, ChatColor.AQUA.toString()),
    ENTRY_12(12, ChatColor.RED.toString()),
    ENTRY_13(13, ChatColor.LIGHT_PURPLE.toString()),
    ENTRY_14(14, ChatColor.YELLOW.toString()),
    ENTRY_15(15, ChatColor.WHITE.toString())
}
