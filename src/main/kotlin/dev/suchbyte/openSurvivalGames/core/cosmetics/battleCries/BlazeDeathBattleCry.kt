package dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Sound

class BlazeDeathBattleCry : BattleCry() {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGBlazeDeathBattleCry

    override val sound: Sound
        get() = Sound.BLAZE_DEATH
}