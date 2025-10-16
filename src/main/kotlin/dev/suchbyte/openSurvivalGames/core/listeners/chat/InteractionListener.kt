package dev.suchbyte.openSurvivalGames.core.listeners.chat

import dev.suchbyte.openSurvivalGames.core.config.ConfigLoader
import dev.suchbyte.openSurvivalGames.common.extensions.ConfigLocationExtensions.Companion.toConfigLocation
import dev.suchbyte.openSurvivalGames.OpenSurvivalGamesPlugin
import dev.suchbyte.openSurvivalGames.infrastructure.caching.PlayerMenuUtilityCache
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import dev.suchbyte.openSurvivalGames.presentation.gui.menus.SettingsMenu
import dev.suchbyte.openSurvivalGames.core.config.MapConfiguration
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.ArrowTrailManager
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.BattleCryManager
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.CosmeticManager
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.core.scoreboard.managers.ScoreboardManager
import dev.suchbyte.openSurvivalGames.common.utils.BungeeUtils
import dev.suchbyte.openSurvivalGames.common.utils.Items
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.io.path.Path


class InteractionListener(
    private val plugin: OpenSurvivalGamesPlugin,
    private val config: PluginConfig,
    private val gameManager: GameManager,
    private val statsManager: StatsManager,
    private val cosmeticManager: CosmeticManager,
    private val battleCryManager: BattleCryManager,
    private val arrowTrailManager: ArrowTrailManager,
    private val scoreboardManager: ScoreboardManager
) : Listener {
    @EventHandler
    fun onInteraction(event: PlayerInteractEvent) {
        if (gameManager.currentGameState == GameState.Lobby
            || !gameManager.alivePlayers.contains(event.player)
        ) {
            event.isCancelled = event.player.gameMode != GameMode.CREATIVE
        }

        when (event.action) {
            Action.LEFT_CLICK_BLOCK -> handleLeftClickBlock(event)

            Action.RIGHT_CLICK_BLOCK -> handleRightClickBlock(event)

            Action.RIGHT_CLICK_AIR -> handleRightClickAir(event)

            else -> {}
        }
    }

    private fun handleItems(event: PlayerInteractEvent): Boolean {
        if (event.player.itemInHand.isSimilar(Items.instructionBookSurvivalGamesClassic)) {
            event.isCancelled = false
            return true
        }

        if (event.player.itemInHand.isSimilar(Items.setSpawnItem)) {
            event.player.performCommand("setmapspawn")
            return true
        }

        if (gameManager.alivePlayers.contains(event.player)) {
            return false
        }

        if (event.player.itemInHand.isSimilar(Items.teleporterItem)) {
            gameManager.handleTeleporter(event.player)
            return true
        }

        if (event.player.itemInHand.isSimilar(Items.leftServerItem)) {
            BungeeUtils.sendPlayerToServer(plugin, event.player, config.hubServerName)
            return true
        }

        if (event.player.itemInHand.isSimilar(Items.settingsItem)) {
            SettingsMenu(
                PlayerMenuUtilityCache.getPlayerMenuUtility(event.player),
                statsManager,
                cosmeticManager,
                battleCryManager,
                arrowTrailManager,
                scoreboardManager
            ).onOpen()
            return true
        }
        return false
    }

    private fun handleRightClickAir(event: PlayerInteractEvent) {
        handleItems(event)
    }

    private fun handleRightClickBlock(event: PlayerInteractEvent) {
        if (handleItems(event)) {
            return
        }

        val item = event.item
        if (item != null && item.type === Material.FLINT_AND_STEEL) {
            val remainingDurability: Int = item.type.maxDurability - item.durability

            if (remainingDurability <= 4) {
                event.player.inventory.remove(item)
                event.player.playSound(event.player.eyeLocation, Sound.ITEM_BREAK, 1f, 1f)
            } else {
                item.durability = (item.durability + (item.type.maxDurability / 4)).toShort()
            }
        }

        if (event.player.gameMode != GameMode.CREATIVE) {
            return
        }

        val itemInHand = event.player.inventory.itemInHand
        if (itemInHand.type == Material.STICK
            && event.clickedBlock.type == Material.ENDER_CHEST
            && itemInHand.itemMeta.displayName == "§cTier2 Maker"
            && event.player.gameMode == GameMode.CREATIVE
        ) {
            event.isCancelled = true
            setTierTwoChest(event.player, event.clickedBlock, false)
        }
    }

    private fun handleLeftClickBlock(event: PlayerInteractEvent) {
        if (event.player.gameMode != GameMode.CREATIVE) {
            return
        }

        val itemInHand = event.player.inventory.itemInHand
        if (itemInHand.type == Material.STICK
            && event.clickedBlock.type == Material.ENDER_CHEST
            && itemInHand.itemMeta.displayName == "§cTier2 Maker"
            && event.player.gameMode == GameMode.CREATIVE
        ) {
            event.isCancelled = true
            setTierTwoChest(event.player, event.clickedBlock, true)
        }
    }

    private fun setTierTwoChest(player: Player, clickedBlock: Block, tierTwo: Boolean) {
        val mapManifest = config.maps.find { it.id == clickedBlock.world.uid.toString() }
        if (mapManifest == null) {
            player.sendMessageWithPrefix("You're not in a known map")
            return
        }

        val mapConfigPath = Path(mapManifest.path).toFile()
        val mapConfig = ConfigLoader.loadConfig(mapConfigPath, MapConfiguration())
        val configLocation = clickedBlock.location.toConfigLocation()

        val existingLocation =
            mapConfig.tier2Chests.find { x ->
                x.worldName == configLocation.worldName
                        && x.x == configLocation.x
                        && x.y == configLocation.y
                        && x.z == configLocation.z
            }

        if (tierTwo) {
            if (existingLocation != null) {
                player.sendMessageWithPrefix("§eThis crate is already a tier2 crate")
                return
            }

            mapConfig.tier2Chests.add(configLocation)
            ConfigLoader.saveConfig(mapConfigPath, mapConfig)
            player.sendMessageWithPrefix("§aThis crate is now a tier2 crate")
            return
        }

        if (existingLocation != null) {
            mapConfig.tier2Chests.remove(existingLocation)
            ConfigLoader.saveConfig(mapConfigPath, mapConfig)
            player.sendMessageWithPrefix("§eThis crate is no longer a tier2 crate")
        } else {
            player.sendMessageWithPrefix("§cThis crate is not a tier2 crate")
        }
    }
}