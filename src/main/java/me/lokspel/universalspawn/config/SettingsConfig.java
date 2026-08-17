package me.lokspel.universalspawn.config;

import me.lokspel.universalspawn.UniversalSpawn;
import org.bukkit.configuration.file.FileConfiguration;

public final class SettingsConfig {

    private static final String TELEPORT_ON_JOIN = "teleport-on-join.enabled";
    private static final String TELEPORT_ON_DEATH = "teleport-on-death.enabled";
    private static final String AUTO_RESPAWN = "teleport-on-death.auto-respawn";
    private static final String AUTO_RESPAWN_DELAY = "teleport-on-death.respawn-delay-ticks";
    private static final String AUTO_RESPAWN_RETRIES = "teleport-on-death.respawn-retries";
    private static final String POST_RESPAWN_TELEPORT_DELAY = "teleport-on-death.post-respawn-teleport-delay";
    private static final String TELEPORT_OUT_OF_VOID = "teleport-out-of-void.enabled";
    private static final String VOID_CHECK_HEIGHT = "teleport-out-of-void.check-height";

    private final UniversalSpawn plugin;

    public SettingsConfig(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    private FileConfiguration config() {
        return plugin.getConfig();
    }

    public boolean teleportOnJoin() {
        return config().getBoolean(TELEPORT_ON_JOIN, true);
    }

    public boolean teleportOnRespawn() {
        return config().getBoolean(TELEPORT_ON_DEATH, true);
    }

    public boolean autoRespawn() {
        return config().getBoolean(AUTO_RESPAWN, true);
    }

    public long respawnDelayTicks() {
        return Math.max(1L, config().getLong(AUTO_RESPAWN_DELAY, 2L));
    }

    public int respawnRetries() {
        return Math.max(0, config().getInt(AUTO_RESPAWN_RETRIES, 4));
    }

    public long postRespawnTeleportDelayTicks() {
        return Math.max(0L, config().getLong(POST_RESPAWN_TELEPORT_DELAY, 1L));
    }

    public boolean teleportOutOfVoid() {
        return config().getBoolean(TELEPORT_OUT_OF_VOID, true);
    }

    public int voidCheckHeight() {
        return config().getInt(VOID_CHECK_HEIGHT, 0);
    }
}