package dev.suchbyte.openSurvivalGames.core.maps.modules

import dev.suchbyte.openSurvivalGames.OpenSurvivalGamesPlugin
import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import dev.suchbyte.openSurvivalGames.core.maps.managers.BaseMapManager
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

abstract class MapModule(
    val plugin: OpenSurvivalGamesPlugin,
    val gameManager: BaseGameManager,
    val mapManager: BaseMapManager
): Listener {
    open fun onEnable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
        plugin.logger.info("Map module ${javaClass.simpleName} enabled")
    }

    open fun onDisable() {
        HandlerList.unregisterAll(this)
    }
}