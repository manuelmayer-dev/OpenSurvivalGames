package dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Sound

class ZombieDoorBreakBattleCry : BattleCry() {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGZombieDoorBreakBattleCry

    override val sound: Sound
        get() = Sound.ZOMBIE_WOODBREAK
}