package dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Sound

class IronGolemDeathBattleCry : BattleCry() {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGIronGolemDeathBattleCry

    override val sound: Sound
        get() = Sound.IRONGOLEM_DEATH
}