# UniversalSpawn

Simple global spawn plugin for Paper and Folia servers.  
Works on both Paper and Folia.

## Features

- `/spawn` teleport command
- `/spawn set` to save the global spawn
- Teleport on join
- Teleport on respawn
- Void protection
- MiniMessage support

## Requirements

- Java 21
- Paper `1.21.11` API or compatible server

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

## Configuration

`config.yml`

```yml
# Teleport players to the configured spawn when they join the server.
teleport-on-join:
  enabled: true

# Control respawn behavior after death.
# enabled: send players to the configured spawn after they respawn.
# auto-respawn: force the respawn screen to be skipped automatically.
# respawn-delay-ticks: delay before each auto-respawn attempt.
# respawn-retries: how many extra attempts should be made if the first respawn fails.
# post-respawn-teleport-delay: extra safety delay before teleporting after respawn.
teleport-on-death:
  enabled: true
  auto-respawn: true
  respawn-delay-ticks: 2
  respawn-retries: 4
  post-respawn-teleport-delay: 1

# Protect players from falling into the void in the spawn world.
# check-height: players at or below this Y level will be teleported to spawn.
teleport-out-of-void:
  enabled: true
  check-height: 0

# The saved global spawn location.
# Leave this unset until an admin runs /spawn set.
spawn:
  world:
  x:
  y:
  z:
  yaw:
  pitch:
```

`messages.yml`

```yml
only-player: "<red>Only players can use this command.</red>"
no-permission: "<red>You do not have permission to use this command.</red>"
no-spawn-permission: "<red>You do not have permission to use /spawn.</red>"
spawn-set: "<green>Spawn location has been set.</green>"
spawn-teleported: "<green>You were teleported to spawn.</green>"
spawn-missing: "<red>Spawn location is not configured.</red>"
```

## Notes

- Spawn is considered missing until an admin runs `/spawn set`.
- Messages use MiniMessage formatting.
- The plugin declares `folia-supported: true`.
