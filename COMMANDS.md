# OpenSurvivalGames - Commands Reference

This document provides a comprehensive overview of all commands available in OpenSurvivalGames.

## Table of Contents
- [Player Commands](#player-commands)
- [Admin Commands](#admin-commands)
- [Permission Reference](#permission-reference)

---

## Player Commands

These commands are available to all players by default.

### `/stats`
**Aliases:** `/records`, `/globalstats`  
**Permission:** None (available to all players)  
**Description:** Display your personal statistics

Shows detailed statistics including:
- Kills
- Deaths
- Points
- Victories
- Chests opened
- Deathmatches participated
- Games played

**Usage:**
```
/stats
```

---

### `/list`
**Permission:** None (available to all players)  
**Description:** Show all online players

Displays a list of all players currently on the server.

**Usage:**
```
/list
```

---

### `/scramble`
**Permission:** None (available to all players)  
**Description:** Hide your statistics from other players

Toggle whether other players can see your stats. Useful for privacy or playing incognito.

**Usage:**
```
/scramble
```

---

### `/vote`
**Aliases:** `/v`  
**Permission:** None (available to all players)  
**Description:** Vote for a map during the lobby phase

Opens an inventory GUI where you can vote for one of the available maps. The map with the most votes will be selected for the next game.

**Usage:**
```
/vote
/v
```

**Note:** Only available during the lobby phase before the game starts.

---

## Admin Commands

These commands require specific permissions and are typically restricted to server operators.

### `/cancel`
**Permission:** `opensurvivalgames.commands.cancel`  
**Description:** Cancel the current countdown

Cancels the active countdown, which can be:
- Lobby countdown (before game starts)
- Deathmatch countdown (before deathmatch teleportation)

**Usage:**
```
/cancel
```

**Use Cases:**
- Stop an accidental game start
- Prevent deathmatch when testing
- Give more time for players to join

---

### `/setlobby`
**Permission:** `opensurvivalgames.commands.setup`  
**Description:** Set the lobby spawn location

Sets the spawn point where players will be teleported when they join the server or when a game ends.

**Usage:**
```
/setlobby
```

**Setup Steps:**
1. Stand at the desired lobby location
2. Face the direction you want players to spawn facing
3. Execute `/setlobby`

---

### `/createmap <name>`
**Permission:** `opensurvivalgames.commands.setup`  
**Description:** Import a map and create its configuration

Creates a new Survival Games map by:
1. Importing the world (must be placed in server's root directory)
2. Creating the map configuration file
3. Teleporting you to the map for setup

**Usage:**
```
/createmap <map-name>
```

**Example:**
```
/createmap SkyIslands
```

**Prerequisites:**
- World folder must exist in the server's root directory
- World must be a valid Minecraft world

**Next Steps After Creation:**
1. Use `/setmapspawnitem` to set spawn points
2. (Optional) Use `/tier2tool` to mark tier 2 chests
3. Edit the map's `manifest.yml` to add builders, display name, etc.

---

### `/skipwarmup`
**Permission:** `opensurvivalgames.commands.skipwarmup`  
**Description:** Shorten the warmup countdown

Reduces the warmup phase countdown to 5 seconds. The warmup phase is the time between lobby and the actual game start where players are frozen at their spawns.

**Usage:**
```
/skipwarmup
```

**Note:** Only works during the warmup phase.

---

### `/setmapspawn`
**Permission:** `opensurvivalgames.commands.setup`  
**Description:** Set a spawn point for the current map

Adds a spawn location at your current position to the active map's configuration.

**Usage:**
```
/setmapspawn
```

**Setup Steps:**
1. Be on the map you want to configure
2. Stand at the spawn location
3. Face the direction you want players to spawn facing
4. Execute `/setmapspawn`
5. Repeat for each spawn point (minimum 12 recommended)

**Alternative:** Use `/setmapspawnitem` for a more convenient item-based approach.

---

### `/setmapspawnitem`
**Permission:** `opensurvivalgames.commands.setup`  
**Description:** Receive a convenient spawn-setting tool

Gives you a special item that sets spawn points when right-clicking. Much more convenient than typing the command repeatedly.

**Usage:**
```
/setmapspawnitem
```

**How to Use:**
1. Execute the command to receive the tool
2. Walk to each spawn location
3. Right-click with the tool to set a spawn point
4. Repeat until all spawns are set

**Recommended Spawn Count:** 12-24 spawns per map depending on map size

---

### `/tier2tool`
**Permission:** `opensurvivalgames.commands.setup`  
**Description:** Get a tool to mark Tier 2 chests

Provides an item to designate chests as Tier 2 (higher quality loot). Right-click chests with this tool to toggle their tier status.

**Usage:**
```
/tier2tool
```

**How to Use:**
1. Execute the command to receive the tool
2. Right-click chests you want to mark as Tier 2
3. Tier 2 chests will provide better loot during the game

**Balancing Tips:**
- Place Tier 2 chests in central/dangerous locations
- Typically 20-30% of chests should be Tier 2
- Tier 2 chests encourage player movement and combat

---

### `/start`
**Permission:** `opensurvivalgames.commands.start`  
**Description:** Shorten the lobby countdown

Reduces the lobby countdown to 10 seconds, allowing the game to start faster when enough players are online.

**Usage:**
```
/start
```

**Requirements:**
- Minimum number of players must be online
- Must be executed during the lobby phase
- Cannot bypass player requirements (use `/forcestart` for that)

---

### `/forcestart`
**Permission:** `opensurvivalgames.commands.forcestart`  
**Description:** Force start the game bypassing all requirements

Immediately starts the game, ignoring:
- Minimum player requirements
- Countdown timer

**Usage:**
```
/forcestart
```

**Use Cases:**
- Testing maps with few players
- Starting games during low-population periods
- Development and debugging

**Warning:** May result in unbalanced games if too few players are present.

---

### `/forcemap <map>`
**Permission:** `opensurvivalgames.commands.forcemap`  
**Description:** Force select a specific map from the entire pool

Bypasses the voting system and directly selects a map for the next game.

**Usage:**
```
/forcemap <map-name>
```

**Example:**
```
/forcemap SkyIslands
```

**Features:**
- Cancels any ongoing map voting
- Selects from the entire map pool (not just the 5 voting options)
- Useful for testing specific maps or fulfilling player requests

---

## Permission Reference

### Summary Table

| Permission | Default | Commands |
|------------|---------|----------|
| None | Everyone | `/stats`, `/list`, `/scramble`, `/vote` |
| `opensurvivalgames.commands.cancel` | OP | `/cancel` |
| `opensurvivalgames.commands.setup` | OP | `/setlobby`, `/createmap`, `/setmapspawn`, `/setmapspawnitem`, `/tier2tool` |
| `opensurvivalgames.commands.skipwarmup` | OP | `/skipwarmup` |
| `opensurvivalgames.commands.start` | OP | `/start` |
| `opensurvivalgames.commands.forcestart` | OP | `/forcestart` |
| `opensurvivalgames.commands.forcemap` | OP | `/forcemap` |

### Permission Groups

For permission plugin users (LuckPerms, PermissionsEx, etc.), here are suggested permission groups:

#### Player (Default)
```yaml
permissions:
  # All player commands are available without explicit permissions
```

#### Moderator
```yaml
permissions:
  - opensurvivalgames.commands.cancel
  - opensurvivalgames.commands.start
```

#### Admin
```yaml
permissions:
  - opensurvivalgames.commands.*
  # Or individually:
  - opensurvivalgames.commands.cancel
  - opensurvivalgames.commands.setup
  - opensurvivalgames.commands.skipwarmup
  - opensurvivalgames.commands.start
  - opensurvivalgames.commands.forcestart
  - opensurvivalgames.commands.forcemap
```

---

## Command Usage Tips

### For Server Owners

1. **Initial Setup Workflow:**
   ```
   /setlobby                  # Set lobby spawn
   /createmap MyFirstMap      # Create a map
   /setmapspawnitem          # Get spawn tool
   # Right-click at each spawn location (12+)
   /tier2tool                # Get chest tier tool
   # Right-click tier 2 chests
   /forcestart               # Test the game
   ```

2. **Testing Maps:**
   ```
   /forcemap MapName         # Select specific map
   /forcestart               # Start without min players
   /skipwarmup               # Skip warmup phase
   ```

3. **Managing Games:**
   ```
   /start                    # Speed up start when ready
   /cancel                   # Stop accidental starts
   ```

### For Players

1. **Viewing Stats:**
   ```
   /stats                    # Your stats
   /list                     # See who's online
   ```

2. **Map Voting:**
   ```
   /vote                     # Open voting GUI
   # Click on your preferred map
   ```

3. **Privacy:**
   ```
   /scramble                 # Hide your stats
   ```

---

## Troubleshooting

### "You don't have permission to use this command"
- Check that you have the required permission
- Contact a server administrator
- Verify your permission group has the correct permissions

### "/setmapspawn doesn't work"
- Ensure you're on a valid map world
- Check that the map exists in the configuration
- Verify you have `opensurvivalgames.commands.setup` permission

### "Not enough players to start"
- Wait for more players to join
- Use `/forcestart` to bypass (requires permission)
- Check server configuration for minimum player count

### "No map selected"
- Ensure maps are properly configured
- Check that at least one map is enabled
- Verify map files exist in `plugins/OpenSurvivalGames/maps/`

---

## Additional Resources

- **Main Documentation:** [README.md](README.md)
- **Configuration Guide:** Check `plugins/OpenSurvivalGames/config.yml`
- **Map Setup:** See map manifest files in `plugins/OpenSurvivalGames/maps/`
- **Support:** [GitHub Issues](https://github.com/manuelmayer-dev/OpenSurvivalGames/issues)
