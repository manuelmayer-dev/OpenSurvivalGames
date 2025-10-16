package dev.suchbyte.openSurvivalGames.common.extensions

import dev.suchbyte.openSurvivalGames.core.config.ConfigLocation
import org.bukkit.Bukkit
import org.bukkit.Location

class ConfigLocationExtensions {
    companion object {
        fun ConfigLocation.toBukkitLocation(): Location {
            return Location(Bukkit.getWorld(worldName), x, y, z, yaw ?: 0F, pitch ?: 0F)
        }

        fun Location.toConfigLocation(): ConfigLocation {
            return ConfigLocation(
                this.world.name,
                this.x,
                this.y,
                this.z,
                this.yaw,
                this.pitch,
            )
        }
    }
}