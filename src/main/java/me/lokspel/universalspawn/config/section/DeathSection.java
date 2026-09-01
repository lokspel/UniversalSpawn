package me.lokspel.universalspawn.config.section;

import me.lokspel.universalspawn.UniversalSpawn;
import org.bukkit.configuration.file.FileConfiguration;

public final class DeathSection {

    private static final String PATH = "teleport-on-death.";

    private final UniversalSpawn plugin;

    public DeathSection(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    private FileConfiguration config() {
        return plugin.getConfig();
    }

    public boolean enabled() {
        return config().getBoolean(PATH + "enabled", true);
    }

    public boolean autoRespawn() {
        return config().getBoolean(PATH + "auto-respawn", true);
    }

    public long respawnDelayTicks() {
        return Math.max(1L, config().getLong(PATH + "respawn-delay-ticks", 2L));
    }

    public int respawnRetries() {
        return Math.max(0, config().getInt(PATH + "respawn-retries", 4));
    }

    public long postRespawnTeleportDelayTicks() {
        return Math.max(0L, config().getLong(PATH + "post-respawn-teleport-delay", 1L));
    }
}
