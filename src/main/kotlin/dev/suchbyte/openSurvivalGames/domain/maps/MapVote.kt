package dev.suchbyte.openSurvivalGames.domain.maps

import dev.suchbyte.openSurvivalGames.core.config.MapManifest

data class MapVote(
    val map: MapManifest?,
    val amount: Int
)