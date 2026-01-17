package me.miko.universalspawn;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class BukkitEvent implements Listener {
    private final UniversalSpawn plugin;

    public BukkitEvent(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getConfigManager().TeleportOnJoin()) {
            FoliaAPI.runTaskLater(plugin, () -> {
                FoliaAPI.teleportPlayer(plugin, event.getPlayer(), plugin.getSpawnLocation());
            }, 1L);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (plugin.getConfigManager().TeleportOutOfVoid()) {
            Player player = event.getPlayer();
            int checkHeight = plugin.getConfig().getInt("teleport-out-of-void.check-height");
            if (player.getLocation().getBlockY() <= checkHeight) {
                Location spawnLocation = plugin.getSpawnLocation();
                if (spawnLocation != null && spawnLocation.getWorld().equals(player.getWorld())) {
                    FoliaAPI.teleportPlayer(plugin, player, plugin.getSpawnLocation());
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        if (plugin.getConfigManager().TeleportOnDeath()) {
            Player player = event.getEntity();

            FoliaAPI.runTaskForEntity(plugin, event.getEntity(), () -> {
                try {
                    event.getEntity().spigot().respawn();
                } catch (Exception e) {
                }
                FoliaAPI.teleportPlayer(plugin, event.getEntity(), plugin.getSpawnLocation());
            }, 1L);
        }
    }
}