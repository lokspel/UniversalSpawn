package me.lokspel.universalspawn.config.section;

import me.lokspel.universalspawn.UniversalSpawn;
import org.bukkit.configuration.file.FileConfiguration;

public final class JoinSection {

    private static final String PATH = "teleport-on-join.";

    private final UniversalSpawn plugin;

    public JoinSection(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    private FileConfiguration config() {
        return plugin.getConfig();
    }

    public boolean enabled() {
        return config().getBoolean(PATH + "enabled", true);
    }
}
