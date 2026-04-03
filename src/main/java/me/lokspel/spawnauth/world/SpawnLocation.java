package me.lokspel.spawnauth.world;

import me.lokspel.spawnauth.SpawnAuth;
import org.bukkit.Location;

public final class SpawnLocation {
    private final SpawnAuth plugin;
    private Location location;

    public SpawnLocation(SpawnAuth plugin) {
        this.plugin = plugin;
    }

    public void load() {
        location = plugin.getSettingsConfig().loadSpawnLocation();
    }

    public void save(Location location) {
        this.location = location;
        plugin.getSettingsConfig().saveSpawnLocation(location);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
