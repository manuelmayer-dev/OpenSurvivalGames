package dev.suchbyte.openSurvivalGames.core.maps.managers

import dev.suchbyte.openSurvivalGames.core.config.ConfigLoader
import dev.suchbyte.openSurvivalGames.common.extensions.ConfigLocationExtensions.Companion.toBukkitLocation
import dev.suchbyte.openSurvivalGames.core.config.MapConfiguration
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import dev.suchbyte.openSurvivalGames.common.utils.WorldUtils
import dev.suchbyte.openSurvivalGames.core.config.MapManifest
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import kotlin.io.path.Path

class DeathMatchMapManager(
    config: PluginConfig
) {
    var mapManifest: MapManifest? = null
        private set
    var mapConfig: MapConfiguration? = null
        private set
    var mapWorld: World? = null
        private set
    val votableMaps =
        config.deathMatches.filter { x -> x.enabled }.shuffled().sortedByDescending { x -> x.newMap }.take(3)

    fun loadAllMaps() {
        votableMaps.forEach(::loadMap)
    }

    fun setMap(map: MapManifest) {
        mapManifest = map
        mapWorld = WorldCreator(map.path).createWorld()
        mapConfig = ConfigLoader.loadConfig(Path(map.path).toFile(), MapConfiguration())
    }

    private fun loadMap(mapManifest: MapManifest) {
        Bukkit.getLogger().info("Loading map...")

        val mapWorld = WorldCreator(mapManifest.path).createWorld()

        applyGameRules(mapWorld)
        WorldUtils.cleanUpWorld(mapWorld)

        val mapConfig = ConfigLoader.loadConfig(Path(mapManifest.path).toFile(), MapConfiguration())
        mapConfig.spawnLocations.forEach { spawnLocation ->
            val chunk = spawnLocation.toBukkitLocation().chunk
            val loaded = chunk.load()
            Bukkit.getLogger().info("Loaded chunk ${chunk.x} ${chunk.z}: $loaded")
        }
    }

    private fun applyGameRules(world: World) {
        world.setGameRuleValue("doFireTick", "false")
        world.setGameRuleValue("doDaylightCycle", "false")
        world.setGameRuleValue("naturalRegeneration", "true")
        world.time = 0
    }
}