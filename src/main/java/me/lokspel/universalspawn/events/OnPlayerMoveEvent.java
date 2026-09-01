package me.lokspel.universalspawn.events;

import me.lokspel.universalspawn.UniversalSpawn;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public final class OnPlayerMoveEvent implements Listener {
    private final UniversalSpawn plugin;

    public OnPlayerMoveEvent(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!plugin.getMainConfig().voidSection().enabled()) {
            return;
        }

        Player player = event.getPlayer();
        if (player.getLocation().getBlockY() > plugin.getMainConfig().voidSection().checkHeight()) {
            return;
        }

        Location spawnLocation = plugin.getSpawnLocation().getLocation();
        if (spawnLocation == null
                || spawnLocation.getWorld() == null
                || !spawnLocation.getWorld().equals(player.getWorld())) {
            return;
        }

        plugin.getFoliaLib().getScheduler().teleportAsync(player, spawnLocation);
        event.setCancelled(true);
    }
}
