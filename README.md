# UniversalSpawn

Simple global spawn plugin for Spigot, Paper and Folia

## » About

UniversalSpawn provides a simple global spawn system for Minecraft servers. Players can teleport to a configured spawn, automatically teleport there when joining, and return to it after respawning. Includes void protection and MiniMessage-formatted messages.

## » Requirements

- **Java 21**
- **Spigot, Paper, or Folia**
- **Minecraft `1.21.11`, `26.1`, or `26.2`**

## » Installation

1. Install a compatible Spigot, Paper, or Folia server
2. Drop `UniversalSpawn.jar` into your `plugins/` folder
3. Restart the server
4. Run `/spawn set` to configure the global spawn

## » Commands

| Command | Description | Permission |
| --- | --- | --- |
| `/spawn` | Teleport to the saved spawn | `universalspawn.spawn.use` |
| `/spawn set` | Save your current location as spawn | `universalspawn.spawn.set` |

## » Permissions

| Permission | Default | Description |
| --- | --- | --- |
| `universalspawn.spawn.use` | `true` | Allows players to use `/spawn` |
| `universalspawn.spawn.set` | `op` | Allows players to use `/spawn set` |

## » Features

- Global `/spawn` teleport command
- `/spawn set` to save the global spawn
- Teleport players to spawn on join
- Teleport players to spawn on respawn
- Void protection
- MiniMessage support

## » Build

```bash
mvn clean package
