package dev.suchbyte.openSurvivalGames.core.maps.managers

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import dev.suchbyte.openSurvivalGames.domain.scoreboard.Prefix
import dev.suchbyte.openSurvivalGames.domain.maps.MapVote
import dev.suchbyte.openSurvivalGames.core.config.MapManifest
import dev.suchbyte.openSurvivalGames.common.extensions.CommandSenderExtensions.Companion.sendMessageWithPrefix
import dev.suchbyte.openSurvivalGames.common.extensions.StringExtensions.Companion.sendToOnlinePlayersWithPrefix
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class MapVoting(
    private var votableMaps: List<MapManifest>,
    private val messageFooterText: (() -> List<String>)? = null
) {
    private var votingActive = false
    private var mapVotes = mutableMapOf<UUID, MapVote>()

    fun startVoting() {
        votingActive = true
        mapVotes.clear()
    }

    fun cancelVoting() {
        if (!votingActive) {
            return
        }

        votingActive = false
    }

    fun endVoting(result: (MapManifest) -> Unit) {
        if (!votingActive) {
            return
        }

        votingActive = false

        val results = mutableMapOf<MapManifest, Int>()

        votableMaps.forEach { map ->
            val votes = mapVotes.entries.filter { x -> x.value.map == votableMaps.find { m -> m.id == map.id } }
                .sumOf { x -> x.value.amount }
            results[map] = votes
        }

        val map = results.entries.shuffled().maxBy { x -> x.value }.key
        "§6Voting has ended! The map §b${map.name} §6has won!".sendToOnlinePlayersWithPrefix(Prefix.SurvivalGames)

        result.invoke(map)
    }

    fun sendVotableMaps() {
        Bukkit.getOnlinePlayers().forEach(::sendVotableMaps)
    }

    fun sendVotableMaps(players: List<Player>) {
        players.forEach(::sendVotableMaps)
    }

    fun sendVotableMaps(player: Player) {
        player.sendMessageWithPrefix("§7--------------------------", Prefix.SurvivalGames)
        player.sendMessageWithPrefix("§6Vote for a map with /v #.", Prefix.SurvivalGames)
        player.sendMessageWithPrefix("§6map choices up for voting:", Prefix.SurvivalGames)
        votableMaps.forEach { map ->
            val index = votableMaps.indexOf(map) + 1
            val votes =
                mapVotes.entries.filter { x -> x.value.map?.id == map.id }.sumOf { x -> x.value.amount }

            val message =TextComponent(
                "${Prefix.SurvivalGames.getFormatted()} §c§l$index. ${(if (map.seasonMap) "§c§lSEASON " else "")}" +
                        "${(if (map.newMap) "§a§lNEW " else "")}§r§e${map.name} (§b$votes §evotes)")
            message.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote $index")
            message.hoverEvent =
                HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("Vote for ${map.name}").create())

            player.spigot().sendMessage(message)
        }
        messageFooterText?.invoke()?.forEach {
            player.sendMessageWithPrefix(it, Prefix.SurvivalGames)
        }
        player.sendMessageWithPrefix("§7--------------------------", Prefix.SurvivalGames)
    }

    fun voteMap(player: Player, number: Int) {
        if (!votingActive) {
            player.sendMessageWithPrefix("§cVoting is already over.", Prefix.SurvivalGames)
        }

        if (number < 1 || number > votableMaps.size) {
            sendVotableMaps(player)
            return
        }

        val map = votableMaps.getOrNull(number - 1)
        val voteAmount = 1

        mapVotes[player.uniqueId] = MapVote(map, voteAmount)

        if (map != null) {
            val votes = mapVotes.entries.filter { x -> x.value.map?.id == map.id }.sumOf { x -> x.value.amount }
            player.sendMessageWithPrefix("§6You voted for §e${map.name} (§b$votes §evotes)", Prefix.SurvivalGames)
        } else {
            val votes = mapVotes.entries.filter { x -> x.value.map == null }.sumOf { x -> x.value.amount }
            player.sendMessageWithPrefix("§6You voted for §4random map §6(§b$votes §evotes)", Prefix.SurvivalGames)
        }
    }
}