package me.lokspel.spawnauth.events;

import me.lokspel.spawnauth.SpawnAuth;
import me.lokspel.spawnauth.utils.FoliaAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class OnPlayerDeathEvent implements Listener {
    private final SpawnAuth plugin;

    public OnPlayerDeathEvent(SpawnAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!plugin.getSettingsConfig().shouldTeleportOnRespawn()) {
            return;
        }

        if (!plugin.getSettingsConfig().shouldAutoRespawn()) {
            return;
        }

        if (plugin.getSpawnLocation().getLocation() == null) {
            return;
        }

        attemptRespawn(event.getEntity(), plugin.getSettingsConfig().getAutoRespawnRetries());
    }

    private void attemptRespawn(Player player, int retriesLeft) {
        if (player == null || !player.isOnline()) {
            return;
        }

        FoliaAPI.runTaskForEntity(player, () -> {
            if (!player.isDead()) {
                return;
            }

            try {
                player.spigot().respawn();
            } catch (Exception ignored) {
            }

            if (player.isDead() && retriesLeft > 0) {
                attemptRespawn(player, retriesLeft - 1);
            }
        }, plugin.getSettingsConfig().getAutoRespawnDelayTicks());
    }
}
