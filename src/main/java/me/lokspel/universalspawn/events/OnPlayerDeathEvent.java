package me.lokspel.universalspawn.events;

import me.lokspel.universalspawn.UniversalSpawn;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class OnPlayerDeathEvent implements Listener {
    private final UniversalSpawn plugin;

    public OnPlayerDeathEvent(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!plugin.getMainConfig().death().enabled()) {
            return;
        }

        if (!plugin.getMainConfig().death().autoRespawn()) {
            return;
        }

        if (plugin.getSpawnLocation().getLocation() == null) {
            return;
        }

        attemptRespawn(event.getEntity(), plugin.getMainConfig().death().respawnRetries());
    }

    private void attemptRespawn(Player player, int retriesLeft) {
        if (player == null || !player.isOnline()) {
            return;
        }

        plugin.getFoliaLib().getScheduler().runAtEntityLater(
                player,
                () -> {
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
                },
                () -> {
                },
                plugin.getMainConfig().death().respawnDelayTicks()
        );
    }
}
