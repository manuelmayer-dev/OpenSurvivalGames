package dev.suchbyte.openSurvivalGames.common.utils

import dev.suchbyte.openSurvivalGames.common.Constants
import org.bukkit.World

class WorldUtils {
    companion object {
        fun cleanUpWorld(world: World) {
            world.setStorm(false)
            world.entities.filter { x -> !Constants.entityWhitelist.contains(x.type) }.forEach { entity ->
                entity.remove()
            }
        }
    }
}