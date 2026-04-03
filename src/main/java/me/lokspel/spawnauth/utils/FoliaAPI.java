package me.lokspel.spawnauth.utils;

import me.lokspel.spawnauth.SpawnAuth;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;

public final class FoliaAPI {
    private static final Map<String, Method> CACHED_METHODS = new ConcurrentHashMap<>();
    private static final Map<String, Class<?>> CACHED_CLASSES = new ConcurrentHashMap<>();

    private static final BukkitScheduler BUKKIT_SCHEDULER = Bukkit.getScheduler();
    private static final Object GLOBAL_REGION_SCHEDULER;
    private static final Object REGION_SCHEDULER;
    private static final Object ASYNC_SCHEDULER;
    private static final boolean IS_FOLIA;

    private static JavaPlugin plugin;

    static {
        cacheClasses();
        GLOBAL_REGION_SCHEDULER = getGlobalRegionScheduler();
        REGION_SCHEDULER = getRegionScheduler();
        ASYNC_SCHEDULER = getAsyncScheduler();
        IS_FOLIA = determineFolia();
        cacheMethods();
    }

    private FoliaAPI() {
    }

    public static void init(JavaPlugin javaPlugin) {
        plugin = javaPlugin;
    }

    public static boolean isFolia() {
        return IS_FOLIA;
    }

    private static void cacheClasses() {
        tryLoadClass("io.papermc.paper.threadedregions.RegionizedServer");
    }

    private static void tryLoadClass(String className) {
        try {
            CACHED_CLASSES.put(className, Class.forName(className));
        } catch (ClassNotFoundException | LinkageError ignored) {
        }
    }

    private static boolean determineFolia() {
        return CACHED_CLASSES.containsKey("io.papermc.paper.threadedregions.RegionizedServer")
                && GLOBAL_REGION_SCHEDULER != null
                && REGION_SCHEDULER != null;
    }

    private static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        if (clazz == null) {
            return null;
        }

        try {
            Method method = clazz.getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    private static void cacheMethods() {
        if (GLOBAL_REGION_SCHEDULER != null) {
            Class<?> schedulerClass = GLOBAL_REGION_SCHEDULER.getClass();
            cacheMethod("globalRegionScheduler.runAtFixedRate",
                    getMethod(schedulerClass, "runAtFixedRate", Plugin.class, Consumer.class, long.class, long.class));
            cacheMethod("globalRegionScheduler.run",
                    getMethod(schedulerClass, "run", Plugin.class, Consumer.class));
            cacheMethod("globalRegionScheduler.runDelayed",
                    getMethod(schedulerClass, "runDelayed", Plugin.class, Consumer.class, long.class));
            cacheMethod("globalRegionScheduler.cancelTasks",
                    getMethod(schedulerClass, "cancelTasks", Plugin.class));
        }

        if (REGION_SCHEDULER != null) {
            Class<?> schedulerClass = REGION_SCHEDULER.getClass();
            cacheMethod("regionScheduler.execute",
                    getMethod(schedulerClass, "execute", Plugin.class, World.class, int.class, int.class,
                            Runnable.class));
            cacheMethod("regionScheduler.executeLocation",
                    getMethod(schedulerClass, "execute", Plugin.class, Location.class, Runnable.class));
            cacheMethod("regionScheduler.runAtFixedRate",
                    getMethod(schedulerClass, "runAtFixedRate", Plugin.class, Location.class, Consumer.class,
                            long.class, long.class));
            cacheMethod("regionScheduler.runDelayed",
                    getMethod(schedulerClass, "runDelayed", Plugin.class, Location.class, Consumer.class,
                            long.class));
        }

        cacheMethod("entity.getScheduler", getMethod(Entity.class, "getScheduler"));
        cacheMethod("player.teleportAsync", getMethod(Player.class, "teleportAsync", Location.class));

        if (ASYNC_SCHEDULER != null) {
            Class<?> schedulerClass = ASYNC_SCHEDULER.getClass();
            cacheMethod("asyncScheduler.cancelTasks", getMethod(schedulerClass, "cancelTasks", Plugin.class));
            cacheMethod("asyncScheduler.runNow", getMethod(schedulerClass, "runNow", Plugin.class, Consumer.class));
            cacheMethod("asyncScheduler.runDelayed",
                    getMethod(schedulerClass, "runDelayed", Plugin.class, Consumer.class, long.class,
                            TimeUnit.class));
            cacheMethod("asyncScheduler.runAtFixedRate",
                    getMethod(schedulerClass, "runAtFixedRate", Plugin.class, Consumer.class, long.class,
                            long.class, TimeUnit.class));
        }
    }

    private static void cacheMethod(String key, Method method) {
        if (method != null) {
            CACHED_METHODS.put(key, method);
        }
    }

    private static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            if (method != null && target != null) {
                return method.invoke(target, args);
            }
        } catch (Exception exception) {
            SpawnAuth instance = SpawnAuth.getInstance();
            if (instance != null) {
                instance.getLogger().log(Level.SEVERE,
                        "A reflective Folia scheduler call failed. The running server API is not compatible.",
                        exception);
            } else {
                Bukkit.getLogger().log(Level.SEVERE,
                        "[SpawnAuth] A reflective Folia scheduler call failed. The running server API is not compatible.",
                        exception);
            }
        }

        return null;
    }

    private static Object getGlobalRegionScheduler() {
        return invokeMethod(getMethod(Server.class, "getGlobalRegionScheduler"), Bukkit.getServer());
    }

    private static Object getRegionScheduler() {
        return invokeMethod(getMethod(Server.class, "getRegionScheduler"), Bukkit.getServer());
    }

    private static Object getAsyncScheduler() {
        return invokeMethod(getMethod(Server.class, "getAsyncScheduler"), Bukkit.getServer());
    }

    public static void runTask(Runnable runnable) {
        if (!IS_FOLIA) {
            BUKKIT_SCHEDULER.runTask(plugin, runnable);
            return;
        }

        invokeMethod(CACHED_METHODS.get("globalRegionScheduler.run"), GLOBAL_REGION_SCHEDULER, plugin,
                (Consumer<Object>) ignored -> runnable.run());
    }

    public static void runTaskLater(Runnable runnable, long delay) {
        if (!IS_FOLIA) {
            BUKKIT_SCHEDULER.runTaskLater(plugin, runnable, delay);
            return;
        }

        invokeMethod(CACHED_METHODS.get("globalRegionScheduler.runDelayed"), GLOBAL_REGION_SCHEDULER, plugin,
                (Consumer<Object>) ignored -> runnable.run(), delay);
    }

    public static void runTaskForEntity(Entity entity, Runnable runnable, long delay) {
        runTaskForEntity(entity, runnable, () -> {
        }, delay);
    }

    public static void runTaskForEntity(Entity entity, Runnable runnable, Runnable retired, long delay) {
        if (!IS_FOLIA) {
            if (delay == 0L && Bukkit.isPrimaryThread()) {
                runnable.run();
                return;
            }

            BUKKIT_SCHEDULER.runTaskLater(plugin, runnable, delay);
            return;
        }

        if (entity == null) {
            return;
        }

        Object entityScheduler = invokeMethod(CACHED_METHODS.get("entity.getScheduler"), entity);
        if (entityScheduler == null) {
            return;
        }

        Method executeMethod = CACHED_METHODS.get("entityScheduler.execute");
        if (executeMethod == null) {
            executeMethod = getMethod(entityScheduler.getClass(), "execute", Plugin.class, Runnable.class,
                    Runnable.class, long.class);
            cacheMethod("entityScheduler.execute", executeMethod);
        }

        invokeMethod(executeMethod, entityScheduler, plugin, runnable, retired, delay);
    }

    public static CompletableFuture<Boolean> teleportPlayer(Player player, Location location, boolean async) {
        return teleportPlayer(player, location, async, null);
    }

    public static CompletableFuture<Boolean> teleportPlayer(Player player, Location location, boolean async,
                                                            Runnable complete) {
        if (player == null || location == null) {
            if (complete != null) {
                complete.run();
            }
            return CompletableFuture.completedFuture(false);
        }

        if (!IS_FOLIA || !async) {
            runTask(() -> {
                player.teleport(location);
                if (complete != null) {
                    complete.run();
                }
            });
            return CompletableFuture.completedFuture(true);
        }

        Method teleportMethod = CACHED_METHODS.get("player.teleportAsync");
        runTaskForEntity(player, () -> {
            Object result = invokeMethod(teleportMethod, player, location);
            if (result instanceof CompletableFuture<?> future) {
                if (complete != null) {
                    future.whenComplete((ignored, throwable) -> complete.run());
                }
                return;
            }

            if (complete != null) {
                complete.run();
            }
        }, 1L);
        return CompletableFuture.completedFuture(true);
    }

    public static void cancelAllTasks() {
        if (plugin == null) {
            return;
        }

        if (!IS_FOLIA) {
            BUKKIT_SCHEDULER.cancelTasks(plugin);
            return;
        }

        invokeMethod(CACHED_METHODS.get("globalRegionScheduler.cancelTasks"), GLOBAL_REGION_SCHEDULER, plugin);
        invokeMethod(CACHED_METHODS.get("asyncScheduler.cancelTasks"), ASYNC_SCHEDULER, plugin);
    }

    public static void runTask(Chunk chunk, Runnable runnable) {
        if (!IS_FOLIA) {
            BUKKIT_SCHEDULER.runTask(plugin, runnable);
            return;
        }

        if (chunk != null) {
            runTaskForRegion(chunk.getWorld(), chunk.getX(), chunk.getZ(), runnable);
        }
    }

    public static void runTaskForRegion(World world, int chunkX, int chunkZ, Runnable runnable) {
        if (!IS_FOLIA) {
            BUKKIT_SCHEDULER.runTask(plugin, runnable);
            return;
        }

        if (world != null) {
            invokeMethod(CACHED_METHODS.get("regionScheduler.execute"), REGION_SCHEDULER, plugin, world, chunkX,
                    chunkZ, runnable);
        }
    }
}
