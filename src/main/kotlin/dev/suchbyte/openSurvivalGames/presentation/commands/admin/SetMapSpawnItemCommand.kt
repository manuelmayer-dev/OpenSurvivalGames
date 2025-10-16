package dev.suchbyte.openSurvivalGames.presentation.commands.admin

import dev.suchbyte.openSurvivalGames.presentation.commands.base.BaseCommand
import dev.suchbyte.openSurvivalGames.common.utils.Items
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetMapSpawnItemCommand : BaseCommand("setmapspawnitem", "opensurvivalgames.commands.setup") {
    override fun executePlayerCommand(player: Player, args: Array<out String>) {
        player.inventory.addItem(Items.setSpawnItem)
    }

    override fun executeConsoleCommand(sender: CommandSender, args: Array<out String>) {
        sendOnlyPlayerMessage(sender)
    }
}