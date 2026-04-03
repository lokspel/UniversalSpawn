package me.lokspel.spawnauth.events;

import me.lokspel.spawnauth.SpawnAuth;
import me.lokspel.spawnauth.utils.FoliaAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public final class OnPlayerMoveEvent implements Listener {
    private final SpawnAuth plugin;

    public OnPlayerMoveEvent(SpawnAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!plugin.getSettingsConfig().shouldTeleportOutOfVoid()) {
            return;
        }

        Player player = event.getPlayer();
        if (player.getLocation().getBlockY() > plugin.getSettingsConfig().getVoidCheckHeight()) {
            return;
        }

        Location spawnLocation = plugin.getSpawnLocation().getLocation();
        if (spawnLocation == null || !spawnLocation.getWorld().equals(player.getWorld())) {
            return;
        }

        FoliaAPI.teleportPlayer(player, spawnLocation, true);
        event.setCancelled(true);
    }
}
