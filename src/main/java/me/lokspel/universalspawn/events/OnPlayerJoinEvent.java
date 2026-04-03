package me.lokspel.universalspawn.events;

import me.lokspel.universalspawn.UniversalSpawn;
import me.lokspel.universalspawn.utils.FoliaAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class OnPlayerJoinEvent implements Listener {
    private final UniversalSpawn plugin;

    public OnPlayerJoinEvent(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.getSettingsConfig().shouldTeleportOnJoin()) {
            return;
        }

        if (plugin.getSpawnLocation().getLocation() == null) {
            return;
        }

        FoliaAPI.runTaskLater(() -> FoliaAPI.teleportPlayer(
                event.getPlayer(),
                plugin.getSpawnLocation().getLocation(),
                true
        ), 1L);
    }
}
