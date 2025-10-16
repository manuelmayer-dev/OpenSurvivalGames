package dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Sound

class HorseDeathBattleCry : BattleCry() {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGHorseDeathBattleCry

    override val sound: Sound
        get() = Sound.HORSE_DEATH
}