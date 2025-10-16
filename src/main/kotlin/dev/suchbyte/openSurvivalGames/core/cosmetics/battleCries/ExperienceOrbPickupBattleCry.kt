package dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Sound

class ExperienceOrbPickupBattleCry : BattleCry() {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGExperienceOrbPickupBattleCry

    override val sound: Sound
        get() = Sound.ORB_PICKUP
}