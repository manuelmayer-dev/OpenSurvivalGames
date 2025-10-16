package dev.suchbyte.openSurvivalGames.presentation.gui.base

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

abstract class Menu(
    private val playerMenuUtility: PlayerMenuUtility
) : InventoryHolder {
    protected lateinit var menuInventory: Inventory

    abstract fun getMenuName(): String
    abstract fun getSlots(): Int
    abstract fun handleMenu(event: InventoryClickEvent)
    abstract fun setMenuItems()

    open fun onOpen() {
        menuInventory = Bukkit.createInventory(this, getSlots(), getMenuName())

        setMenuItems()

        playerMenuUtility.getOwner().openInventory(menuInventory)
    }

    open fun onClose() {}

    override fun getInventory(): Inventory {
        return menuInventory
    }

    fun makeEnchantedItem(material: Material, displayName: String, vararg lore: String): ItemStack {
        val item = ItemStack(material)
        val itemMeta = item.itemMeta
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true)
        itemMeta!!.displayName = displayName
        itemMeta.lore = listOf(*lore)
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE)
        item.setItemMeta(itemMeta)

        return item
    }

    fun makeItem(material: Material, displayName: String, vararg lore: String): ItemStack {
        val item = ItemStack(material)
        val itemMeta = item.itemMeta
        itemMeta!!.displayName = displayName
        itemMeta.lore = listOf(*lore)
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE)
        item.setItemMeta(itemMeta)

        return item
    }

    fun makeItem(material: Material, displayName: String, amount: Int, vararg lore: String): ItemStack {
        val item = ItemStack(material, amount)
        val itemMeta = item.itemMeta
        itemMeta!!.displayName = displayName
        itemMeta.lore = listOf(*lore)
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE)
        item.setItemMeta(itemMeta)

        return item
    }

    fun makeItem(material: Material, data: Int, displayName: String, vararg lore: String): ItemStack {
        val item = ItemStack(material, 1, data.toShort())
        val itemMeta = item.itemMeta
        itemMeta!!.displayName = displayName
        itemMeta.lore = listOf(*lore)
        item.setItemMeta(itemMeta)

        return item
    }

    fun makeItem(material: Material, displayName: String, lore: List<String>): ItemStack {
        val item = ItemStack(material)
        val itemMeta = item.itemMeta
        itemMeta!!.displayName = displayName
        itemMeta.lore = lore
        item.setItemMeta(itemMeta)

        return item
    }
}