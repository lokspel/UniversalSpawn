package me.lokspel.universalspawn.world;

import me.lokspel.universalspawn.UniversalSpawn;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public final class SpawnLocation {
    private final UniversalSpawn plugin;
    private Location location;

    public SpawnLocation(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration config = plugin.getConfig();
        String worldName = config.getString("spawn.world");
        if (worldName == null || worldName.trim().isEmpty()) {
            location = null;
            return;
        }

        World world = plugin.getServer().getWorld(worldName);
        if (world == null) {
            plugin.getLogger().warning("Spawn world '" + worldName + "' was not found.");
            location = null;
            return;
        }

        location = new Location(
                world,
                config.getDouble("spawn.x"),
                config.getDouble("spawn.y"),
                config.getDouble("spawn.z"),
                (float) config.getDouble("spawn.yaw"),
                (float) config.getDouble("spawn.pitch")
        );
    }

    public void save(Location location) {
        this.location = location;
        FileConfiguration config = plugin.getConfig();
        if (location == null) {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}