package dev.suchbyte.openSurvivalGames.presentation.commands.base

import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendNoPermissionsMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

abstract class BaseCommand(
    protected val commandName: String,
    private val permission: String? = null,
    private val cooldown: Int? = null
) : CommandExecutor {
    companion object {
        val lastUsages = mutableMapOf<UUID, MutableMap<String, Long>>()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!command.name.equals(commandName, ignoreCase = true)) {
            return false
        }

        if (sender is Player) {
            if (cooldown != null) {
                val lastCommandUsage = lastUsages.getOrPut(sender.uniqueId) { mutableMapOf() }[commandName]
                val currentTime = System.currentTimeMillis()
                if (lastCommandUsage != null && (currentTime - lastCommandUsage) < cooldown * 1000) {
                    sender.sendMessageWithPrefix("§cThis command can only be used every $cooldown seconds.")
                    return true
                }

                lastUsages[sender.uniqueId]!![commandName] = currentTime
            }

            if (!permission.isNullOrEmpty() && !sender.hasPermission(permission)) {
                sender.sendNoPermissionsMessage()
                return true
            }

            executePlayerCommand(sender, args)
            return true
        }

        executeConsoleCommand(sender, args)
        return true
    }

    abstract fun executePlayerCommand(player: Player, args: Array<out String>)

    abstract fun executeConsoleCommand(sender: CommandSender, args: Array<out String>)

    protected open fun sendUsageMessage(sender: CommandSender) {
        sender.sendMessageWithPrefix("§cUsage: /$commandName")
    }

    protected fun sendOnlyPlayerMessage(sender: CommandSender) {
        sender.sendMessageWithPrefix("§cThis command can only be executed by player")
    }

    protected fun isInteger(s: String): Boolean {
        return s.toIntOrNull() != null
    }

    fun register(plugin: JavaPlugin) {
        plugin.getCommand(commandName).executor = this
    }
}