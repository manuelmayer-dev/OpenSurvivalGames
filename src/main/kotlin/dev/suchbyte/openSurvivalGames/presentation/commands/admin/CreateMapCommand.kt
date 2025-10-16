package dev.suchbyte.openSurvivalGames.presentation.commands.admin

import dev.suchbyte.openSurvivalGames.presentation.commands.base.BaseCommand
import dev.suchbyte.openSurvivalGames.core.config.ConfigLoader
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.core.config.MapManifest
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import org.bukkit.GameMode
import org.bukkit.WorldCreator
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.io.File

class CreateMapCommand(
    private val plugin: Plugin,
    private val config: PluginConfig
) : BaseCommand("createmap", "opensurvivalgames.commands.setup") {
    override fun executePlayerCommand(player: Player, args: Array<out String>) {
        val mapPath = args.firstOrNull()?.lowercase()
        val mapType = args.getOrNull(1)?.lowercase()
        if (mapPath == null || mapType == null || (mapType != "sg" && mapType != "dm")) {
            sendUsageMessage(player)
            return
        }

        if (!File(mapPath).exists()) {
            player.sendMessageWithPrefix("§cThis path does not exist")
            return
        }

        try {
            val world = WorldCreator(mapPath).createWorld()

            when (mapType) {
                "sg" -> addMapIfNotExists(config.maps, mapPath, world.uid.toString())
                "dm" -> addMapIfNotExists(config.deathMatches, mapPath, world.uid.toString())
            }

            player.gameMode = GameMode.CREATIVE
            player.teleport(world.spawnLocation)
        } catch (e: Exception) {
            player.sendMessageWithPrefix("World cannot be created: ${e.message}")
        }

        ConfigLoader.saveConfig(plugin.dataFolder, config)

        player.sendMessageWithPrefix("§6Map created")

    }

    override fun executeConsoleCommand(sender: CommandSender, args: Array<out String>) {
        sendOnlyPlayerMessage(sender)
    }

    override fun sendUsageMessage(sender: CommandSender) {
        sender.sendMessageWithPrefix("§cUsage: /$commandName <path> <sg, dm>")
    }

    private fun addMapIfNotExists(mapList: MutableList<MapManifest>, mapPath: String, worldUid: String) {
        if (!mapList.any { it.path == mapPath }) {
            mapList.add(MapManifest().apply {
                name = mapPath
                id = worldUid
                path = mapPath
            })
        }
    }
}