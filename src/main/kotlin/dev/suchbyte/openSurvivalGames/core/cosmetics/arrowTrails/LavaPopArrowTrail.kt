package dev.suchbyte.openSurvivalGames.core.cosmetics.arrowTrails

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Effect
import org.bukkit.plugin.Plugin

class LavaPopArrowTrail(
    plugin: Plugin
): ArrowTrail(plugin) {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGLavaPopArrowTrail
    override val effect: Effect
        get() = Effect.LAVA_POP
}