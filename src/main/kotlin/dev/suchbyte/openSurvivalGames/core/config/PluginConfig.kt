package dev.suchbyte.openSurvivalGames.core.config

open class PluginConfig {
    var hubServerName: String = "lobby"
    var maxTeamSize: Int = 2
    var minPlayers: Int = 6
    var initialLobbyCountdown: Int = 120
    var minPlayersToTriggerDeathMatchShortening = 4
    var minPlayersToTriggerVotingShortening = 20
    var lobbyLocation: ConfigLocation? = null
    var maps = mutableListOf<MapManifest>()
    var deathMatches = mutableListOf<MapManifest>()
}