package dev.suchbyte.openSurvivalGames.domain.stats

data class RoundStats(
    var kills: Int,
    var hasDied: Boolean,
    var hasWon: Boolean,
    var wasInDeathMatch: Boolean,
    var gainedPoints: Int,
    var lostPoints: Int,
    var chestsOpened: Int,
    var votedMap: String?,
    var arrowsShot: Int,
    var arrowsHit: Int,
    var rodThrownOut: Int,
    var rodHit: Int,
    var damageMade: Double,
    var damageTaken: Double,
    var killerName: String? = null,
    var killerHealth: Double? = null
)