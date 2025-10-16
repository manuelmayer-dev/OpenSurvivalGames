package dev.suchbyte.openSurvivalGames.core.game.states

import dev.suchbyte.openSurvivalGames.OpenSurvivalGamesPlugin
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

abstract class GameStateHandler(private val plugin: OpenSurvivalGamesPlugin, val gameState: GameState) : Listener {
    private var delegate: (() -> Unit)? = null

    fun setDelegate(delegate: () -> Unit) {
        this.delegate = delegate
    }

    open fun onEnable() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    open fun onDisable() {
        HandlerList.unregisterAll(this)
        delegate?.invoke()
    }
}