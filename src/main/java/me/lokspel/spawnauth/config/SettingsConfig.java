package me.lokspel.spawnauth.config;

import me.lokspel.spawnauth.SpawnAuth;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public final class SettingsConfig {
    private final SpawnAuth plugin;
    private boolean teleportOnJoin;
    private boolean teleportOnRespawn;
    private boolean autoRespawn;
    private boolean teleportOutOfVoid;
    private int voidCheckHeight;
    private long autoRespawnDelayTicks;
    private int autoRespawnRetries;
    private long postRespawnTeleportDelayTicks;

    public SettingsConfig(SpawnAuth plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration config = plugin.getConfig();
        teleportOnJoin = config.getBoolean("teleport-on-join.enabled", true);
        teleportOnRespawn = config.getBoolean("teleport-on-death.enabled", true);
        autoRespawn = config.getBoolean("teleport-on-death.auto-respawn", true);
        autoRespawnDelayTicks = Math.max(1L, config.getLong("teleport-on-death.respawn-delay-ticks", 2L));
        autoRespawnRetries = Math.max(0, config.getInt("teleport-on-death.respawn-retries", 4));
        postRespawnTeleportDelayTicks = Math.max(0L,
                config.getLong("teleport-on-death.post-respawn-teleport-delay", 1L));
        teleportOutOfVoid = config.getBoolean("teleport-out-of-void.enabled", true);
        voidCheckHeight = config.getInt("teleport-out-of-void.check-height", 0);
    }

    public Location loadSpawnLocation() {
        FileConfiguration config = plugin.getConfig();
        String worldName = config.getString("spawn.world");
        if (worldName == null || worldName.trim().isEmpty()) {
            return null;
        }

        World world = plugin.getServer().getWorld(worldName);
        if (world == null) {
            plugin.getLogger().warning("Spawn world '" + worldName + "' was not found.");
            return null;
        }

        return new Location(
                world,
                config.getDouble("spawn.x"),
                config.getDouble("spawn.y"),
                config.getDouble("spawn.z"),
                (float) config.getDouble("spawn.yaw"),
                (float) config.getDouble("spawn.pitch")
        );
    }

    public void saveSpawnLocation(Location location) {
        FileConfiguration config = plugin.getConfig();
        if (location == null || location.getWorld() == null) {
            config.set("spawn.world", null);
            config.set("spawn.x", null);
            config.set("spawn.y", null);
            config.set("spawn.z", null);
            config.set("spawn.yaw", null);
            config.set("spawn.pitch", null);
        } else {
            config.set("spawn.world", location.getWorld().getName());
            config.set("spawn.x", location.getX());
            config.set("spawn.y", location.getY());
            config.set("spawn.z", location.getZ());
            config.set("spawn.yaw", location.getYaw());
            config.set("spawn.pitch", location.getPitch());
        }
        plugin.saveConfig();
    }

    public boolean shouldTeleportOnJoin() {
        return teleportOnJoin;
    }

    public boolean shouldTeleportOnRespawn() {
        return teleportOnRespawn;
    }

    public boolean shouldAutoRespawn() {
        return autoRespawn;
    }

    public boolean shouldTeleportOutOfVoid() {
        return teleportOutOfVoid;
    }

    public int getVoidCheckHeight() {
        return voidCheckHeight;
    }

    public long getAutoRespawnDelayTicks() {
        return autoRespawnDelayTicks;
    }

    public int getAutoRespawnRetries() {
        return autoRespawnRetries;
    }

    public long getPostRespawnTeleportDelayTicks() {
        return postRespawnTeleportDelayTicks;
    }
}
