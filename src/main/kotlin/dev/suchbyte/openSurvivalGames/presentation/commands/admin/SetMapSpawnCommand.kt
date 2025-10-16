package dev.suchbyte.openSurvivalGames.presentation.commands.admin

import dev.suchbyte.openSurvivalGames.presentation.commands.base.BaseCommand
import dev.suchbyte.openSurvivalGames.core.config.ConfigLoader
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.common.extensions.ConfigLocationExtensions.Companion.toConfigLocation
import dev.suchbyte.openSurvivalGames.core.config.MapConfiguration
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.io.path.Path

class SetMapSpawnCommand(
    private val config: PluginConfig
) : BaseCommand("setmapspawn", "opensurvivalgames.commands.setup") {
    override fun executePlayerCommand(player: Player, args: Array<out String>) {
        val world = player.world
        val mapManifest = config.maps.union(config.deathMatches).find { it.id == world.uid.toString() }
        if (mapManifest == null) {
            player.sendMessageWithPrefix("You're not in a known map! Use /createmap to create a map.")
            return
        }

        val mapConfigPath = Path(mapManifest.path).toFile()
        val mapConfig = ConfigLoader.loadConfig(mapConfigPath, MapConfiguration())
        mapConfig.spawnLocations.add(player.location.toConfigLocation())
        ConfigLoader.saveConfig(mapConfigPath, mapConfig)

        player.sendMessageWithPrefix("§aCreated spawn ${mapConfig.spawnLocations.size}")
    }

    override fun executeConsoleCommand(sender: CommandSender, args: Array<out String>) {
        sendOnlyPlayerMessage(sender)
    }

    override fun sendUsageMessage(sender: CommandSender) {
        sender.sendMessageWithPrefix("§cUsage: /$commandName")
    }
}