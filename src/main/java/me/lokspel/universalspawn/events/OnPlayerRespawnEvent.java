package me.lokspel.universalspawn.events;

import me.lokspel.universalspawn.UniversalSpawn;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class OnPlayerRespawnEvent implements Listener {
    private final UniversalSpawn plugin;

    public OnPlayerRespawnEvent(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!plugin.getMainConfig().death().enabled()) {
            return;
        }

        Location spawnLocation = plugin.getSpawnLocation().getLocation();
        if (spawnLocation == null) {
            return;
        }

        event.setRespawnLocation(spawnLocation);
        plugin.getFoliaLib().getScheduler().runAtEntityLater(
                event.getPlayer(),
                () -> {
                    if (!event.getPlayer().isOnline()) {
                        return;
                    }

                    plugin.getFoliaLib().getScheduler().teleportAsync(event.getPlayer(), spawnLocation);
                },
                () -> {
                },
                plugin.getMainConfig().death().postRespawnTeleportDelayTicks()
        );
    }
}
