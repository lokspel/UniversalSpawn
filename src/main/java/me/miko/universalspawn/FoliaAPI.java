package me.miko.universalspawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

public class FoliaAPI {

    private static final boolean IS_FOLIA;

    static {
        boolean isFolia = false;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (ClassNotFoundException e) {
            isFolia = false;
        }
        IS_FOLIA = isFolia;
    }

    public static void teleportPlayer(Plugin plugin, Player player, Location location) {
        if (IS_FOLIA) {
            player.teleportAsync(location);
        } else {
            if (Bukkit.isPrimaryThread()) {
                player.teleport(location);
            } else {
                Bukkit.getScheduler().runTask(plugin, () -> player.teleport(location));
            }
        }
    }

    public static void runTaskForEntity(Plugin plugin, Entity entity, Runnable runnable, long delayTicks) {
        if (IS_FOLIA) {
            try {
                Method getScheduler = Entity.class.getMethod("getScheduler");
                Object scheduler = getScheduler.invoke(entity);

                Method runDelayed = scheduler.getClass().getMethod("runDelayed", Plugin.class, java.util.function.Consumer.class, Runnable.class, long.class);

                runDelayed.invoke(scheduler, plugin, (java.util.function.Consumer<Object>) task -> runnable.run(), null, delayTicks);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks);
        }
    }

    public static void runTaskLater(Plugin plugin, Runnable runnable, long delayTicks) {
        if (IS_FOLIA) {
            Bukkit.getGlobalRegionScheduler().runDelayed(plugin, task -> runnable.run(), delayTicks);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks);
        }
    }

    public static void cancelAllTasks(Plugin plugin) {
        if (IS_FOLIA) {
            Bukkit.getGlobalRegionScheduler().cancelTasks(plugin);
            Bukkit.getAsyncScheduler().cancelTasks(plugin);
        } else {
            Bukkit.getScheduler().cancelTasks(plugin);
        }
    }
}
