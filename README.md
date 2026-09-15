# UniversalSpawn

Simple global spawn plugin for Spigot, Paper and Folia servers.  

## Features

- `/spawn` teleport command
- `/spawn set` to save the global spawn
- Teleport on join
- Teleport on respawn
- Void protection
- MiniMessage support

## Requirements

- Java 21
- Spigot, Paper, or Folia server
- Minecraft `1.21.11`, `26.1`, or `26.2`

## Build

```bash
mvn clean package
```

## Commands

| Command | Description | Permission |
| --- | --- | --- |
| `/spawn` | Teleport to the saved spawn | `universalspawn.spawn.use` |
| `/spawn set` | Save your current location as spawn | `universalspawn.spawn.set` |

## Permissions

| Permission | Default | Description |
| --- | --- | --- |
| `universalspawn.spawn.use` | `true` | Allows players to use `/spawn` |
| `universalspawn.spawn.set` | `op` | Allows players to use `/spawn set` |

## Notes

- Spawn is considered missing until an admin runs `/spawn set`.
- Messages use MiniMessage formatting.
- The plugin declares `folia-supported: true`.
