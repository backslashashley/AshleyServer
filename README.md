# Ashley's Server!

My custom 1.12 server with my silly features.
A lot stolen from bis :)


## Feature List - Incomplete 

### Stackable Shulker Boxes
Executing the command `/stackboxes [stack size]` will stack all available boxes in your inventory. Maximum stack size is 16.

### AFK Player Statistics
There are two new statistics, stat.playOnMinuteNoAFK and stat.minutesAFK. Both of these track playtime without including afk time and afk time respectively. 

### Fixed Firework Usage Statistic
Using a firework with an Elytra will now increment the use firework statistic. 

### Hopper, Dropper, Dispenser Updates
Hoppers, droppers and dispensers will now send block state updates to the client.

### Camera Mode
Executing the command `/cam` will put the player into spectator and save their position and rotation. Executing the command again will put them back into survival and restore their position and rotation

### Total Digs Statistic
Combines pickaxe, axe, and shovel usage across all tool types into one unified dig statistic.

### Ping Command
Executing `/ping` will return the players ping.

### Server Bots
Server sided bots to load areas. Command is `/bot`.

### Server Sided Macro
Executing `/macro` will enable the player to set a server sided repeating action. The actions are `[attack or use]` and the operation modes are `[once|interval|continous]`.

### Hud
Server information (mspt, tps, mobcaps) are displayed in the tab list.

### Silence Mobs
Silence annoying mobs by using a name tag called `stfu`.

### Ghost block fixes
Fixes piston ghost blocks and mining ghost blocks.

### Shit scoreboard system.
Allows players to easily change the scoreboard displays using `/stat`. The backend for this sucks and needs a rewrite.
