package dev.suchbyte.openSurvivalGames.domain.stats

import java.util.*

data class SurvivalGamesLeaderboardEntry(
    val playerId: Long,
    val username: String,
    val uuid: UUID,
    val wins: Int,
    val points: Int
)
