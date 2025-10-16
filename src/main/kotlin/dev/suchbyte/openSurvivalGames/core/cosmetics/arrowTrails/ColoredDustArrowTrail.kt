package dev.suchbyte.openSurvivalGames.core.cosmetics.arrowTrails

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Effect
import org.bukkit.plugin.Plugin
import kotlin.random.Random

class ColoredDustArrowTrail(
    plugin: Plugin
): ArrowTrail(plugin) {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGColoredDustArrowTrail
    override val effect: Effect
        get() = Effect.COLOURED_DUST

    override fun getData(): Int {
        return Random.nextInt(1, 25)
    }
}