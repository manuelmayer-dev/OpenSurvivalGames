package dev.suchbyte.openSurvivalGames.domain.player

import org.bukkit.inventory.ItemStack

data class CrateItem(
    var item: ItemStack,
    var dropChance: Int
)