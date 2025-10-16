package dev.suchbyte.openSurvivalGames.core.maps.modules

import dev.suchbyte.openSurvivalGames.OpenSurvivalGamesPlugin
import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.common.extensions.StringExtensions.Companion.sendToOnlinePlayersWithPrefix
import dev.suchbyte.openSurvivalGames.core.game.managers.BaseGameManager
import dev.suchbyte.openSurvivalGames.core.maps.managers.BaseMapManager
import org.bukkit.Bukkit
import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask
import java.util.*

class FyreGames2Module(
    plugin: OpenSurvivalGamesPlugin,
    gameManager: BaseGameManager,
    mapManager: BaseMapManager
) : MapModule(plugin, gameManager, mapManager) {
    private val playersInWater = mutableMapOf<Player, BukkitTask>()
    private val playerInWaterDuration = mutableMapOf<UUID, Int>()
    private var bankDoorsTimer: BukkitTask? = null

    override fun onEnable() {
        super.onEnable()
        "§4Water in this map is poisonous!".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
        bankDoorsTimer = plugin.server.scheduler.runTaskLater(plugin, {
            breakBankDoors()
        }, 20 * 60 * 5)
    }

    override fun onDisable() {
        super.onDisable()
        bankDoorsTimer?.cancel()
        playersInWater.entries.forEach { entry ->
            entry.value.cancel()
        }
        playersInWater.clear()
        playerInWaterDuration.clear()
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (playersInWater.contains(event.player)) {
            playersInWater.remove(event.player)
        }

        if (playerInWaterDuration.containsKey(event.player.uniqueId)) {
            playerInWaterDuration.remove(event.player.uniqueId)
        }
    }

    @EventHandler
    fun onItemPickup(event: PlayerPickupItemEvent) {
        if (event.item?.itemStack?.type == Material.IRON_DOOR) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerDied(event: EntityDeathEvent) {
        if (event.entity !is Player) return
        if (playersInWater.contains(event.entity)) {
            playersInWater[event.entity]?.cancel()
            playersInWater.remove(event.entity)
            playerInWaterDuration.remove(event.entity.uniqueId)
        }
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        handlePoisonousWater(event)
    }

    private fun breakBankDoors() {
        val world = mapManager.mainMapWorld ?: run {
            Bukkit.getLogger().warning("World was null")
            return
        }

        val bankDoorsLocation = listOf(
            Location(world, 130.0, 176.0, -106.0),
            Location(world, 125.0, 176.0, -106.0),
        )
        val blocks = bankDoorsLocation.map { it.block }
        blocks.forEach {
            it.type = Material.AIR
            it.location.add(0.0, 1.0, 0.0).block.type = Material.AIR
            world.playEffect(it.location, Effect.EXPLOSION, 1)
            world.dropItem(it.location, ItemStack(Material.IRON_DOOR))
        }

        "§cThe bank doors have been breached!".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)
    }

    private fun handlePoisonousWater(event: PlayerMoveEvent) {
        if (!gameManager.alivePlayers.contains(event.player)) {
            return
        }

        val p = event.player

        if (p.location.block.type == Material.STATIONARY_WATER && !playersInWater.contains(p)) {
            playersInWater[p] = plugin.server.scheduler.runTaskTimer(plugin, {
                val duration = playerInWaterDuration.getOrPut(p.uniqueId) { 1 }
                p.damage(duration.toDouble())
                playerInWaterDuration[p.uniqueId] = duration + 1
            }, 0, 20*3)

            p.sendMessageWithPrefix("§cThe water is poisonous! Don't stay in too long.", Prefix.SurvivalGames)
        }

        if (p.location.block.type == Material.AIR && playersInWater.contains(p)) {
            playersInWater[p]?.cancel()
            playersInWater.remove(p)
            playerInWaterDuration.remove(p.uniqueId)
        }
    }
}