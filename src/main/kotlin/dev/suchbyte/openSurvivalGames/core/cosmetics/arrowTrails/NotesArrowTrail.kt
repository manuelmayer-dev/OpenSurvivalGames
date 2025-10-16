package dev.suchbyte.openSurvivalGames.core.cosmetics.arrowTrails

import dev.suchbyte.openSurvivalGames.domain.cosmetics.Cosmetics
import org.bukkit.Effect
import org.bukkit.plugin.Plugin
import kotlin.random.Random

class NotesArrowTrail(
    plugin: Plugin
): ArrowTrail(plugin) {
    override val cosmetic: Cosmetics
        get() = Cosmetics.SGNotesArrowTrail
    override val effect: Effect
        get() = Effect.NOTE

    override fun getData(): Int {
        return Random.nextInt(1, 25)
    }
}