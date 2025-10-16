package dev.suchbyte.openSurvivalGames.presentation.commands.admin

import dev.suchbyte.openSurvivalGames.presentation.commands.base.BaseCommand
import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import dev.suchbyte.openSurvivalGames.infrastructure.caching.PlayerMenuUtilityCache
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.common.extensions.StringExtensions.Companion.sendToOnlinePlayersWithPrefix
import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import dev.suchbyte.openSurvivalGames.core.maps.managers.MapManager
import dev.suchbyte.openSurvivalGames.core.maps.managers.MapVoting
import dev.suchbyte.openSurvivalGames.presentation.gui.menus.MapSelectionMenu
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ForceMapCommand(
    private val config: PluginConfig,
    private val mapManager: MapManager,
    private val gameManager: GameManager,
    private val voting: MapVoting
) : BaseCommand("forcemap", "opensurvivalgames.commands.forcemap") {
    override fun executePlayerCommand(player: Player, args: Array<out String>) {
        MapSelectionMenu(PlayerMenuUtilityCache.getPlayerMenuUtility(player), config.maps.filter { x -> x.enabled }.toList()) { map ->
            if (gameManager.currentGameState != GameState.Lobby) {
                return@MapSelectionMenu
            }

            voting.cancelVoting()
            mapManager.setMap(map)

            "§6The map §b${map.name} §6has was chosen by ${player.customName}!"
                .sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
        }.onOpen()
    }

    override fun executeConsoleCommand(sender: CommandSender, args: Array<out String>) {
        sendOnlyPlayerMessage(sender)
    }
}