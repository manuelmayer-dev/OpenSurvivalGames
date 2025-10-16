package dev.suchbyte.openSurvivalGames.core.config

class MapConfiguration {
    var hasJumpPads = false
    var resetTime = true
    var spawnLocations = mutableListOf<ConfigLocation>()
    var tier2Chests = mutableListOf<ConfigLocation>()
}