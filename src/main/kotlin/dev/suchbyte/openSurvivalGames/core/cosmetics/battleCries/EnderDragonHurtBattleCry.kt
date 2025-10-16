package dev.suchbyte.openSurvivalGames.core.cosmetics.battleCries

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Sound

class EnderDragonHurtBattleCry : BattleCry() {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGEnderDragonHurtBattleCry

    override val sound: Sound
        get() = Sound.ENDERDRAGON_HIT
}