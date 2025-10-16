package dev.suchbyte.openSurvivalGames.common.utils

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector

class PlayerUtils {
    companion object {
        fun fixPlayerPosition(player: Player) {
            player.location.chunk.unload()
            player.location.chunk.load()
            player.teleport(player.location.add(0.0, 0.1, 0.0))
            player.velocity = Vector(0, 0, 0)
            player.updateInventory();
        }

        fun fixPlayerVisibility(plugin: JavaPlugin, player: Player) {
            plugin.server.scheduler.runTaskLater(plugin, {
                Bukkit.getOnlinePlayers().forEach { otherPlayer ->
                    otherPlayer.showPlayer(player)
                }
            }, 5)
        }
    }
}