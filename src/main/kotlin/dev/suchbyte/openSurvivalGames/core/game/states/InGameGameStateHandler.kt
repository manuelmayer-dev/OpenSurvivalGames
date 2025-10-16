package dev.suchbyte.openSurvivalGames.core.game.states

import dev.suchbyte.openSurvivalGames.core.game.managers.GameManager
import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import dev.suchbyte.openSurvivalGames.common.utils.DateUtils
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.common.utils.Title
import dev.suchbyte.openSurvivalGames.common.Constants
import dev.suchbyte.openSurvivalGames.OpenSurvivalGamesPlugin
import dev.suchbyte.openSurvivalGames.infrastructure.caching.ScoreboardCache
import dev.suchbyte.openSurvivalGames.presentation.commands.admin.CancelCommand
import dev.suchbyte.openSurvivalGames.presentation.commands.admin.StartCommand
import dev.suchbyte.openSurvivalGames.presentation.commands.player.VoteCommand
import dev.suchbyte.openSurvivalGames.core.config.PluginConfig
import dev.suchbyte.openSurvivalGames.core.maps.managers.DeathMatchMapManager
import dev.suchbyte.openSurvivalGames.core.maps.managers.MapManager
import dev.suchbyte.openSurvivalGames.core.maps.managers.MapVoting
import dev.suchbyte.openSurvivalGames.core.scoreboard.implementations.Hive2013InGameScoreboard
import dev.suchbyte.openSurvivalGames.core.scoreboard.implementations.Hive2014InGameScoreboard
import dev.suchbyte.openSurvivalGames.core.scoreboard.implementations.ModernInGameScoreboard
import dev.suchbyte.openSurvivalGames.core.stats.StatsManager
import dev.suchbyte.openSurvivalGames.core.cosmetics.managers.CosmeticManager
import dev.suchbyte.openSurvivalGames.domain.player.CrateItem
import dev.suchbyte.openSurvivalGames.domain.game.GameState
import dev.suchbyte.openSurvivalGames.domain.scoreboard.ScoreboardType
import dev.suchbyte.openSurvivalGames.common.extensions.StringExtensions.Companion.removeMinecraftColorCodes
import dev.suchbyte.openSurvivalGames.common.extensions.StringExtensions.Companion.sendToOnlinePlayersWithPrefix
import dev.suchbyte.openSurvivalGames.core.scoreboard.managers.ScoreboardManager
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask
import java.util.*
import kotlin.random.Random


class InGameGameStateHandler(
    private val plugin: OpenSurvivalGamesPlugin,
    private val config: PluginConfig,
    private val mapManager: MapManager,
    private val gameManager: GameManager,
    private val statsManager: StatsManager,
    private val scoreboardManager: ScoreboardManager,
    private val deathMatchMapManager: DeathMatchMapManager,
    private val cosmeticManager: CosmeticManager
) : GameStateHandler(plugin, GameState.InGame) {
    private val jumpPadsLastUse = mutableMapOf<UUID, Long>()
    private val crates = mutableMapOf<Block, Inventory>()
    private val cratesOpened = mutableMapOf<UUID, MutableList<Location>>()
    private val crateItemInHandBlacklist = listOf(
        Material.WOOD_SWORD,
        Material.GOLD_SWORD,
        Material.STONE_SWORD,
        Material.IRON_SWORD,
        Material.DIAMOND_SWORD
    )

    private var deathMatchTimerTask: BukkitTask? = null
    private val initialDeathMatchCountdown = 60 * 30 + 1
    private var deathMatchCountdown = initialDeathMatchCountdown

    private var crateRefillTimerTask: BukkitTask? = null
    private val initialCrateRefillCountdown = 60 * 12 + 1
    private var crateRefillCountdown = initialCrateRefillCountdown

    private var gameStarted: Long? = null

    private var deathMatchCountdownCancelled = false

    private val deathMatchVoting = MapVoting(
        config.deathMatches.filter { x -> x.enabled }.shuffled().sortedByDescending { x -> x.newMap }.take(3)
    ) {
        listOf(
            "§b${DateUtils.formatSeconds(deathMatchCountdown.toLong())} §6until arena deathmatch.",
            "§6Players alive: §4${gameManager.alivePlayers.size}"
        )
    }

    override fun onEnable() {
        super.onEnable()
        gameManager.mapModule?.onEnable()

        deathMatchCountdown = initialDeathMatchCountdown
        crateRefillCountdown = initialCrateRefillCountdown
        gameStarted = System.currentTimeMillis()

        plugin.getCommand("cancel").executor = CancelCommand {
            deathMatchCountdownCancelled = true
            deathMatchCountdown = initialDeathMatchCountdown
            "§aThe deathmatch countdown has been cancelled.".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
        }

        plugin.getCommand("start").executor = StartCommand {
            deathMatchCountdownCancelled = false
        }

        Bukkit.getOnlinePlayers().forEach { player ->
            setScoreboard(player)
            cratesOpened[player.uniqueId] = mutableListOf()
            player.level = 0
            player.gameMode = GameMode.SURVIVAL
        }
        startDeathMatchTimer()
        startCrateRefillTimer()

        Title.sendToOnlinePlayers("§cFight!", "")

        if (config.maxTeamSize > 1) {
            "§cMax Team size: ${config.maxTeamSize}!".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
        } else {
            "§cNo Teams!".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
        }
    }

    private fun setScoreboard(player: Player) {
        val scoreboard = when (scoreboardManager.getSelectedInGameScoreboard(player)) {
            ScoreboardType.Disabled -> null
            ScoreboardType.Hive2013 -> Hive2013InGameScoreboard(
                player,
                ScoreboardCache.getScoreboard(player),
                gameManager
            )

            ScoreboardType.Hive2014 -> Hive2014InGameScoreboard(
                player,
                ScoreboardCache.getScoreboard(player),
                statsManager,
                gameManager
            )

            ScoreboardType.Modern -> ModernInGameScoreboard(
                player,
                ScoreboardCache.getScoreboard(player),
                statsManager,
                gameManager
            )
        } ?: return

        scoreboardManager.registerScoreboard(player, scoreboard)
    }

    override fun onDisable() {
        super.onDisable()
        deathMatchTimerTask?.cancel()
        crateRefillTimerTask?.cancel()
        deathMatchTimerTask = null
        crateRefillTimerTask = null
        crates.clear()
        jumpPadsLastUse.clear()
        plugin.getCommand("cancel").executor = null
        plugin.getCommand("start").executor = null
    }

    fun getTimeLeft(): Int {
        return deathMatchCountdown
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        handleJumpPads(event)
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.entity !is Player || event.cause != EntityDamageEvent.DamageCause.FALL) {
            return
        }

        if (gameStarted == null || System.currentTimeMillis() - gameStarted!! < 3000) {
            event.isCancelled = true
            return
        }

        if (!jumpPadsLastUse.containsKey(event.entity.uniqueId)) {
            return
        }

        val jumpPadsLastUsed = jumpPadsLastUse[event.entity.uniqueId]!!
        if (System.currentTimeMillis() - jumpPadsLastUsed < 10000) {
            event.isCancelled = true
        } else {
            jumpPadsLastUse.remove(event.entity.uniqueId)
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (!gameManager.alivePlayers.contains(event.player)
            || event.action != Action.RIGHT_CLICK_BLOCK || event.clickedBlock?.type != Material.ENDER_CHEST
        ) {
            return
        }

        event.isCancelled = true

        if (event.player.itemInHand?.type != null && crateItemInHandBlacklist.contains(event.player.itemInHand?.type)) {
            event.player.sendMessageWithPrefix(
                "§cYou cannot open crates with a sword in your hand.",
                Prefix.SurvivalGames
            )
            return
        }

        if (!cratesOpened[event.player.uniqueId]!!.contains(event.clickedBlock.location)) {
            statsManager.addChestOpened(event.player)
            cratesOpened[event.player.uniqueId]!!.add(event.clickedBlock.location)
        }

        val crate = crates[event.clickedBlock!!] ?: createCrate(event.clickedBlock)
        event.player.openInventory(crate)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (!event.view.title.contains("Supply Crate", true)
            || !cosmeticManager.hasEnabled(event.whoClicked as Player, Cosmetics.SGMySword)
            || event.rawSlot > event.inventory.size
        ) {
            return
        }

        val clickedItem = event.currentItem
        if (clickedItem == null
            || !listOf(
                Material.WOOD_SWORD,
                Material.GOLD_SWORD,
                Material.STONE_SWORD,
                Material.IRON_SWORD,
                Material.DIAMOND_SWORD
            ).contains(clickedItem.type)
        ) {
            return
        }

        val meta = clickedItem.itemMeta
        if (meta != null) {
            meta.displayName = "§fSword of §e${event.whoClicked.customName.removeMinecraftColorCodes()}"
            clickedItem.itemMeta = meta
        }

        event.inventory.setItem(event.slot, clickedItem)
    }

    private fun startCrateRefillTimer() {
        if (crateRefillTimerTask != null) {
            return
        }

        crateRefillCountdown = initialCrateRefillCountdown

        crateRefillTimerTask = plugin.server.scheduler.runTaskTimer(plugin, {
            crateRefillCountdown -= 1

            if (crateRefillCountdown in 1..3) {
                Bukkit.getOnlinePlayers().forEach { it.playSound(it.eyeLocation, Sound.ANVIL_LAND, 1f, 1f) }
            }

            if (crateRefillCountdown < 4) {
                crates.forEach {
                    it.key.location.world.playEffect(it.key.location, Effect.MOBSPAWNER_FLAMES, 6)
                    it.key.location.world.playEffect(it.key.location, Effect.MOBSPAWNER_FLAMES, 2)
                    it.key.location.world.playEffect(it.key.location, Effect.MOBSPAWNER_FLAMES, 3)
                }
            }

            if (crateRefillCountdown < 1) {
                crateRefillTimerTask?.cancel()
                crateRefillTimerTask = null
                refillCrates()
            }
        }, 20, 20)
    }

    private fun refillCrates() {
        "§6Fresh supplies have been added to the crates.".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
        crates.clear()
        Bukkit.getOnlinePlayers().forEach { player ->
            player.playSound(player.eyeLocation, Sound.FIREWORK_BLAST, 1f, 1f)
            cratesOpened[player.uniqueId] = mutableListOf()
        }

        Title.sendToOnlinePlayers("§6Supply crates", "§eare now refilled!")

        startCrateRefillTimer()
    }

    private fun startDeathMatchTimer() {
        if (deathMatchTimerTask != null) {
            return
        }

        deathMatchTimerTask = plugin.server.scheduler.runTaskTimer(plugin, {
            deathMatchCountdown -= 1

            if (deathMatchCountdown > 60) {
                if (deathMatchCountdown % (60 * 5) == 0) {
                    announceDetails()
                    announceDeathMatchStart()
                }
            }

            if (deathMatchCountdown in 11..60) {
                if (deathMatchCountdown % 10 == 0) {
                    announceDeathMatchStart()
                }
            }

            if (deathMatchCountdown == 60) {
                deathMatchVoting.startVoting()
                deathMatchVoting.sendVotableMaps(gameManager.alivePlayers)
                plugin.getCommand("vote").executor = VoteCommand(deathMatchVoting, gameManager)
            }

            if (deathMatchCountdown == 30) {
                deathMatchVoting.sendVotableMaps(gameManager.alivePlayers)
            }

            if (deathMatchCountdown in 1..10) {
                announceDeathMatchStart()
            }

            if (deathMatchCountdown == 10) {
                plugin.getCommand("vote").executor = null
                deathMatchVoting.endVoting { deathMatchMapManager.setMap(it) }
            }

            if (deathMatchCountdown <= 3) {
                Bukkit.getOnlinePlayers().forEach { it.playSound(it.eyeLocation, Sound.SUCCESSFUL_HIT, 1f, 1f) }
            }

            if (deathMatchCountdown < 1) {
                deathMatchTimerTask?.cancel()
                deathMatchTimerTask = null
                onDisable()
            }

            checkDeathMatch()
        }, 20, 20)
    }

    private fun announceDetails() {
        "§b${gameManager.alivePlayers.size} §6tributes remain.".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
        "§b${gameManager.spectatingPlayers.size} §6spectators are watching.".sendToOnlinePlayersWithPrefix(
            Prefix.SurvivalGames
        )
    }

    private fun announceDeathMatchStart() {
        "§b${DateUtils.formatSeconds(deathMatchCountdown.toLong())} §6until arena deathmatch."
            .sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
    }

    private fun checkDeathMatch() {
        if (gameManager.alivePlayers.size > config.minPlayersToTriggerDeathMatchShortening
            || deathMatchCountdownCancelled) {
            return
        }

        if (deathMatchCountdown > 61) {
            deathMatchCountdown = 61
            "§cFinal deathmatch countdown has started!".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
        }
    }

    private fun handleJumpPads(event: PlayerMoveEvent) {
        if (mapManager.mainMapConfig?.hasJumpPads != true || !gameManager.alivePlayers.contains(event.player)) {
            return
        }

        val p = event.player
        val loc1 = Location(p.world, event.to.x, event.to.y - 1.0, event.to.z)
        val loc2 = Location(p.world, event.to.x, event.to.y - 0.0, event.to.z)
        val loc3 = Location(p.world, event.to.x, event.to.y - 2.0, event.to.z)

        if (loc1.block.type === Material.REDSTONE_BLOCK && loc2.block.type === Material.STONE_PLATE) {
            val s: Sign = loc3.block.state as Sign
            val multiply = s.getLine(0).toDouble()
            val yaw = s.getLine(1).toDouble()
            p.velocity = p.location.direction.multiply(multiply).setY(yaw)
            p.world.playEffect(loc2, Effect.SMOKE, 6)
            p.world.playEffect(loc2, Effect.SMOKE, 1)
            p.world.playEffect(loc2, Effect.SMOKE, 2)
            p.world.playSound(p.location, Sound.WITHER_SHOOT, 1.0f, 1f)
            jumpPadsLastUse[p.uniqueId] = System.currentTimeMillis()
        }
    }

    private fun createCrate(clickedBlock: Block): Inventory {
        val tier2 = mapManager.mainMapConfig!!.tier2Chests.any { x ->
            x.x.toInt() == clickedBlock.x
                    && x.y.toInt() == clickedBlock.y
                    && x.z.toInt() == clickedBlock.z
        }

        val inventory = Bukkit.createInventory(null, 27, "Supply Crate")

        selectItemStacks(tier2).forEach { item ->
            inventory.setItem(Random.nextInt(0, 26), item)
        }

        crates[clickedBlock] = inventory

        return inventory
    }

    private fun selectItemStacks(tier2: Boolean): List<ItemStack> {
        return selectCrateItems(tier2).map { x -> x.item }
    }

    private fun selectCrateItems(tier2: Boolean): List<CrateItem> {
        val possibleItems = if (tier2) Constants.tier2Items else Constants.tier1Items
        val selectedItems = possibleItems.filter { Random.nextInt(100) < it.dropChance }

        if (selectedItems.size < 3) {
            val remainingConfigs = possibleItems - selectedItems.toSet()
            val additionalConfigs = remainingConfigs.shuffled().take(3 - selectedItems.size)
            return selectedItems + additionalConfigs
        }

        if (selectedItems.size > 5) {
            return selectedItems.shuffled().take(5)
        }

        return selectedItems
    }
}