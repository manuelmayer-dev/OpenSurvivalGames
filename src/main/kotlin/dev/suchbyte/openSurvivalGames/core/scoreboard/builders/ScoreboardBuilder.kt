package dev.suchbyte.openSurvivalGames.core.scoreboard.builders

import dev.suchbyte.openSurvivalGames.domain.scoreboard.EntryName
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

abstract class ScoreboardBuilder(
    protected val player: Player,
    private val scoreboard: Scoreboard,
    objectiveName: String
){
    private val objective: Objective

    private val colors = mutableMapOf<Int, ChatColor>()

    init {
        if (scoreboard.getObjective(objectiveName) != null) {
            scoreboard.getObjective(objectiveName).unregister()
        }

        objective = scoreboard.registerNewObjective(objectiveName, "dummy")
        objective.displaySlot = DisplaySlot.SIDEBAR
    }

    abstract fun createScoreboard()

    abstract fun update()

    fun setDisplayName(displayName: String) {
        objective.displayName = displayName
    }

    fun setSpacer(score: Int) {
        setScore(ChatColor.WHITE, "", score)
    }

    fun setScore(color: ChatColor, content: String, score: Int) {
        val team = getTeamByScore(score) ?: return
        colors[score] = color

        var prefix = if (content.length > 12) content.substring(0, 12) else content
        var suffix = if (content.length > 12) content.substring(12) else null

        if (prefix.endsWith("§")) {
            prefix = prefix.substring(0, prefix.length - 1) // Remove incomplete color code marker
            suffix = "§" + (suffix ?: "") // Move the color code marker to the start of the suffix
        }

        // Set the team prefix with the original color
        team.prefix = color.toString() + prefix

        if (suffix != null) {
            val lastColorInPrefix = ChatColor.getLastColors(team.prefix)
            team.suffix = lastColorInPrefix + suffix
        } else {
            team.suffix = ""
        }

        showScore(score)
    }

    fun updateContent(content: String, score: Int) {
        val color = colors[score] ?: return
        setScore(color, content, score)
    }

    fun updateColor(color: ChatColor, score: Int) {
        colors[score] = color
    }

    fun clear() {
        for (score in 1..16) {
            removeScore(score)
        }
    }

    fun removeScore(score: Int) {
        hideScore(score)
    }

    private fun getEntryNameByScore(score: Int): EntryName? {
        return EntryName.entries.find { x -> x.entry == score }
    }

    private fun getTeamByScore(score: Int): Team? {
        val name = getEntryNameByScore(score) ?: return null

        var team = scoreboard.getEntryTeam(name.entryName)

        if (team != null) {
            return team
        }

        team = scoreboard.registerNewTeam(name.name)
        team.addEntry(name.entryName)

        return team
    }

    private fun showScore(score: Int) {
        val name = getEntryNameByScore(score) ?: return

        if (objective.getScore(name.entryName).isScoreSet) {
            return
        }

        objective.getScore(name.entryName).score = score
    }

    private fun hideScore(score: Int) {
        val name = getEntryNameByScore(score) ?: return

        if (!objective.getScore(name.entryName).isScoreSet) {
            return
        }

        scoreboard.resetScores(name.entryName)
    }
}