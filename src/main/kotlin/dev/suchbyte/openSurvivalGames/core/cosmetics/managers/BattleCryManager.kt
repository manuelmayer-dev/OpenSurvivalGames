package dev.suchbyte.openSurvivalGames.core.cosmetics.managers

import dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

class BattleCryManager(
    private val cosmeticManager: CosmeticManager
) : Listener {
    val battleCries = listOf(
        BlazeDeathBattleCry(),
        EnderDragonHurtBattleCry(),
        ExperienceOrbPickupBattleCry(),
        ExplosionBattleCry(),
        HorseDeathBattleCry(),
        IronGolemDeathBattleCry(),
        LevelUpBattleCry(),
        PigDeathBattleCry(),
        PlayerBurpBattleCry(),
        WitherDeathBattleCry(),
        WitherHurtBattleCry(),
        WolfHowlBattleCry(),
        ZombieDoorBreakBattleCry()
    )

    private val cachedBattleCry = mutableMapOf<UUID, BattleCry>()

    fun play(player: Player, players: List<Player>) {
        cachedBattleCry[player.uniqueId]?.play(players)
    }

    fun cachePlayer(player: Player) {
        val enabledBattleCry = battleCries.find { x -> cosmeticManager.hasEnabled(player, x.cosmetic.uid) } ?: return
        cachedBattleCry[player.uniqueId] = enabledBattleCry
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        cachedBattleCry.remove(event.player.uniqueId)
    }
}