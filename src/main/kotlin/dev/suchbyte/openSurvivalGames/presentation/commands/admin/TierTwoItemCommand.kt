package dev.suchbyte.openSurvivalGames.presentation.commands.admin

import dev.suchbyte.openSurvivalGames.presentation.commands.base.BaseCommand
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class TierTwoItemCommand : BaseCommand("tier2tool", "opensurvivalgames.commands.setup") {
    override fun executePlayerCommand(player: Player, args: Array<out String>) {
        player.inventory.addItem(makeItem())

    }

    override fun executeConsoleCommand(sender: CommandSender, args: Array<out String>) {
        sendOnlyPlayerMessage(sender)
    }

    private fun makeItem(): ItemStack {
        val item = ItemStack(Material.STICK)
        val itemMeta = item.itemMeta
        itemMeta!!.displayName = "§cTier2 Maker"

        itemMeta.lore = listOf(
            "Set's a chest as tier2"
        )
        item.setItemMeta(itemMeta)

        return item
    }
}