package dev.suchbyte.openSurvivalGames.core.listeners.player

import dev.suchbyte.openSurvivalGames.OpenSurvivalGamesPlugin
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.common.extensions.StringExtensions.Companion.sendToOnlinePlayers
import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import dev.suchbyte.openSurvivalGames.common.utils.Items
import dev.suchbyte.openSurvivalGames.common.utils.PlayerUtils
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(
    private val plugin: OpenSurvivalGamesPlugin,
    private val gameManager: GameManager
) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage = null
        gameManager.clearInventory(event.player)
        event.player.gameMode = GameMode.ADVENTURE
        event.player.spigot().collidesWithEntities = true

        Bukkit.getOnlinePlayers().forEach { player ->
            player.showPlayer(event.player)
        }

        if (gameManager.currentGameState == GameState.Lobby) {
            PlayerUtils.fixPlayerVisibility(plugin, event.player)
            "${event.player.displayName} §6has joined the server.".sendToOnlinePlayers()
            event.player.inventory.setItem(8, Items.leftServerItem)
            event.player.inventory.setItem(4, Items.settingsItem)
            event.player.inventory.setItem(0, Items.instructionBookSurvivalGamesClassic)
        } else {
            gameManager.addSpectator(event.player)
        }

        gameManager.teleportToLobby(event.player)
    }
}