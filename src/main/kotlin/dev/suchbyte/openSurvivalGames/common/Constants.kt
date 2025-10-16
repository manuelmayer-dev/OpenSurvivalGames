package dev.suchbyte.openSurvivalGames.common

import dev.suchbyte.openSurvivalGames.domain.player.CrateItem
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

class Constants {
    companion object {
        val entityWhitelist = listOf(
            EntityType.PLAYER,
            EntityType.ARMOR_STAND,
            EntityType.BOAT,
            EntityType.ITEM_FRAME
        )

        val tier1Items = listOf(
            CrateItem(ItemStack(Material.WOOD_SWORD), 20),
            CrateItem(ItemStack(Material.GOLD_SWORD), 20),
            CrateItem(ItemStack(Material.STONE_SWORD), 20),
            CrateItem(ItemStack(Material.WOOD_AXE), 20),
            CrateItem(ItemStack(Material.STONE_AXE), 20),
            CrateItem(ItemStack(Material.LEATHER_HELMET), 16),
            CrateItem(ItemStack(Material.LEATHER_CHESTPLATE), 16),
            CrateItem(ItemStack(Material.LEATHER_LEGGINGS), 16),
            CrateItem(ItemStack(Material.LEATHER_BOOTS), 16),
            CrateItem(ItemStack(Material.GOLD_HELMET), 18),
            CrateItem(ItemStack(Material.GOLD_CHESTPLATE), 18),
            CrateItem(ItemStack(Material.GOLD_LEGGINGS), 18),
            CrateItem(ItemStack(Material.GOLD_BOOTS), 18),
            CrateItem(ItemStack(Material.CHAINMAIL_HELMET), 16),
            CrateItem(ItemStack(Material.CHAINMAIL_CHESTPLATE), 16),
            CrateItem(ItemStack(Material.CHAINMAIL_LEGGINGS), 16),
            CrateItem(ItemStack(Material.CHAINMAIL_BOOTS), 16),
            CrateItem(ItemStack(Material.BREAD), 19),
            CrateItem(ItemStack(Material.APPLE), 16),
            CrateItem(ItemStack(Material.RAW_BEEF), 16),
            CrateItem(ItemStack(Material.RAW_CHICKEN), 16),
            CrateItem(ItemStack(Material.RAW_FISH), 16),
            CrateItem(ItemStack(Material.COOKED_FISH), 6),
            CrateItem(ItemStack(Material.BONE), 4),
            CrateItem(ItemStack(Material.BOWL), 4),
            CrateItem(ItemStack(Material.COOKIE), 10),
            CrateItem(ItemStack(Material.STRING), 8),
            CrateItem(ItemStack(Material.LEATHER), 8),
            CrateItem(ItemStack(Material.PORK), 16),
            CrateItem(ItemStack(Material.CARROT), 18),
            CrateItem(ItemStack(Material.POTATO), 18),
            CrateItem(ItemStack(Material.GOLD_INGOT), 9),
            CrateItem(ItemStack(Material.BOW), 13),
            CrateItem(ItemStack(Material.FLINT), 8),
            CrateItem(ItemStack(Material.STICK, 2), 10),
            CrateItem(ItemStack(Material.FISHING_ROD), 20),
            CrateItem(ItemStack(Material.FEATHER), 6),
            CrateItem(ItemStack(Material.MUSHROOM_SOUP), 8),
            CrateItem(ItemStack(Material.WEB), 8),
            CrateItem(ItemStack(Material.TNT), 10),
            CrateItem(ItemStack(Material.RED_MUSHROOM), 8),
            CrateItem(ItemStack(Material.BROWN_MUSHROOM), 8),
            CrateItem(ItemStack(Material.BROWN_MUSHROOM), 8),
            CrateItem(ItemStack(Material.WHEAT, 4), 12),
        )

        val tier2Items = listOf(
            CrateItem(ItemStack(Material.STICK, 2), 12),
            CrateItem(ItemStack(Material.FISHING_ROD), 22),
            CrateItem(ItemStack(Material.BREAD), 18),
            CrateItem(ItemStack(Material.APPLE), 18),
            CrateItem(ItemStack(Material.WOOD_SWORD), 20),
            CrateItem(ItemStack(Material.GOLD_SWORD), 20),
            CrateItem(ItemStack(Material.STONE_SWORD), 25),
            CrateItem(ItemStack(Material.IRON_INGOT), 5),
            CrateItem(ItemStack(Material.GOLD_INGOT), 9),
            CrateItem(ItemStack(Material.DIAMOND), 5),
            CrateItem(ItemStack(Material.CHAINMAIL_HELMET), 18),
            CrateItem(ItemStack(Material.CHAINMAIL_CHESTPLATE), 18),
            CrateItem(ItemStack(Material.CHAINMAIL_LEGGINGS), 18),
            CrateItem(ItemStack(Material.CHAINMAIL_BOOTS), 18),
            CrateItem(ItemStack(Material.IRON_HELMET), 20),
            CrateItem(ItemStack(Material.IRON_CHESTPLATE), 20),
            CrateItem(ItemStack(Material.IRON_LEGGINGS), 20),
            CrateItem(ItemStack(Material.IRON_BOOTS), 20),
            CrateItem(ItemStack(Material.COOKED_BEEF), 20),
            CrateItem(ItemStack(Material.GRILLED_PORK), 20),
            CrateItem(ItemStack(Material.COOKED_CHICKEN), 20),
            CrateItem(ItemStack(Material.BONE), 8),
            CrateItem(ItemStack(Material.BOWL), 3),
            CrateItem(ItemStack(Material.COOKED_FISH), 20),
            CrateItem(ItemStack(Material.BAKED_POTATO), 20),
            CrateItem(ItemStack(Material.WEB), 10),
            CrateItem(ItemStack(Material.FLINT_AND_STEEL), 12),
            CrateItem(ItemStack(Material.TNT), 10),
            CrateItem(ItemStack(Material.ARROW, 4), 6),
            CrateItem(ItemStack(Material.PUMPKIN_PIE), 20),
            CrateItem(ItemStack(Material.CAKE), 8),
            CrateItem(ItemStack(Material.EXP_BOTTLE), 8),
        )

        val blockBreakWhitelist = listOf(
            Material.WEB,
            Material.FIRE,
            Material.LEAVES,
            Material.LEAVES_2,
            Material.LONG_GRASS,
            Material.DEAD_BUSH,
            Material.RED_ROSE,
            Material.YELLOW_FLOWER,
            Material.RED_MUSHROOM,
            Material.BROWN_MUSHROOM,
            Material.WHEAT,
            Material.CROPS,
            Material.MELON_BLOCK,
            Material.VINE,
            Material.CARROT,
            Material.POTATO,
            Material.CAKE_BLOCK,
            Material.DOUBLE_PLANT,
            Material.SNOW
        )

        val deathMatchBlockBreakWhitelist = listOf(
            Material.WEB,
            Material.FIRE,
            Material.MELON_BLOCK,
            Material.CAKE_BLOCK,
            Material.TNT
        )

        val blockPlaceWhitelist = listOf(
            Material.WEB,
            Material.FIRE,
            Material.LONG_GRASS,
            Material.DEAD_BUSH,
            Material.RED_ROSE,
            Material.YELLOW_FLOWER,
            Material.WHEAT,
            Material.CAKE_BLOCK,
            Material.TNT
        )
    }
}