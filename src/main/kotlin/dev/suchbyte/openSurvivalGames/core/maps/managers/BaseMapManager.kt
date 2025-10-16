package dev.suchbyte.openSurvivalGames.core.maps.managers

import dev.suchbyte.openSurvivalGames.core.config.ConfigLoader
import dev.suchbyte.openSurvivalGames.core.config.ConfigLocation
import dev.suchbyte.openSurvivalGames.common.extensions.ConfigLocationExtensions.Companion.toBukkitLocation
import dev.suchbyte.openSurvivalGames.core.config.MapConfiguration
import dev.suchbyte.openSurvivalGames.core.config.MapManifest
import dev.suchbyte.openSurvivalGames.common.utils.WorldUtils
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import kotlin.io.path.Path

abstract class BaseMapManager {
    var mainMapManifest: MapManifest? = null
        protected set

    var mainMapConfig: MapConfiguration? = null
        protected set

    var mainMapWorld: World? = null
        protected set

    fun reset() {
        if (mainMapWorld != null) {
            Bukkit.getServer().unloadWorld(mainMapWorld!!, false)
        }

        mainMapWorld = null
        mainMapConfig = null
        mainMapManifest = null
    }

    fun setMap(map: MapManifest) {
        reset()
        this.mainMapManifest = map
        this.loadMap()
    }

    fun getSpawnLocations(): List<ConfigLocation> {
        val requiredSpawnLocations = Bukkit.getOnlinePlayers().size
        if (mainMapConfig == null) {
            Bukkit.getLogger().severe("Chosen MapConfig was null")
            return emptyList()
        }

        if (mainMapConfig!!.spawnLocations.isEmpty()) {
            Bukkit.getLogger().severe("Chosen MapConfig contains no spawns")
            return emptyList()
        }

        if (mainMapConfig!!.spawnLocations.isEmpty()) {
            Bukkit.getLogger().severe("Chosen MapConfig contains no spawns")
            return emptyList()
        }

        if (requiredSpawnLocations > mainMapConfig!!.spawnLocations.size) {
            Bukkit.getLogger()
                .severe(
                    "Chosen MapConfig contains not enough spawn " +
                            "locations ${mainMapConfig!!.spawnLocations.size}/${requiredSpawnLocations}"
                )
            return emptyList()
        }

        val step = mainMapConfig!!.spawnLocations.size / requiredSpawnLocations

        return List(requiredSpawnLocations) { i ->
            mainMapConfig!!.spawnLocations[(i * step)]
        }
    }

    private fun loadMap() {
        if (mainMapManifest == null) {
            return
        }

        Bukkit.getLogger().info("Loading map...")

        mainMapWorld = WorldCreator(mainMapManifest!!.path).createWorld()

        mainMapConfig = ConfigLoader.loadConfig(Path(mainMapManifest!!.path).toFile(), MapConfiguration())
        mainMapConfig!!.spawnLocations.forEach { spawnLocation ->
            val chunk = spawnLocation.toBukkitLocation().chunk
            val loaded = chunk.load()
            Bukkit.getLogger().info("Loaded chunk ${chunk.x} ${chunk.z}: $loaded")
        }

        mainMapWorld!!.setGameRuleValue("doFireTick", "false")
        mainMapWorld!!.setGameRuleValue("naturalRegeneration", "true")

        if (mainMapConfig?.resetTime == true) {
            mainMapWorld!!.setGameRuleValue("doDaylightCycle", "true")
            mainMapWorld!!.time = 0
        }

        WorldUtils.cleanUpWorld(mainMapWorld!!)
    }
}