package me.lokspel.universalspawn.config.section;

import me.lokspel.universalspawn.UniversalSpawn;
import org.bukkit.configuration.file.FileConfiguration;

public final class VoidSection {

    private static final String PATH = "teleport-out-of-void.";

    private final UniversalSpawn plugin;

    public VoidSection(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    private FileConfiguration config() {
        return plugin.getConfig();
    }

    public boolean enabled() {
        return config().getBoolean(PATH + "enabled", true);
    }

    public int checkHeight() {
        return config().getInt(PATH + "check-height", 0);
    }
}
