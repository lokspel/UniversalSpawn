package me.lokspel.spawnauth.events;

import me.lokspel.spawnauth.SpawnAuth;
import me.lokspel.spawnauth.utils.FoliaAPI;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class OnPlayerRespawnEvent implements Listener {
    private final SpawnAuth plugin;

    public OnPlayerRespawnEvent(SpawnAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!plugin.getSettingsConfig().shouldTeleportOnRespawn()) {
            return;
        }

        Location spawnLocation = plugin.getSpawnLocation().getLocation();
        if (spawnLocation == null) {
            return;
        }

        event.setRespawnLocation(spawnLocation);
        FoliaAPI.runTaskForEntity(event.getPlayer(), () -> {
            if (!event.getPlayer().isOnline()) {
                return;
            }

            FoliaAPI.teleportPlayer(event.getPlayer(), spawnLocation, true);
        }, plugin.getSettingsConfig().getPostRespawnTeleportDelayTicks());
    }
}
