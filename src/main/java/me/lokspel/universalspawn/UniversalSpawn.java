package me.lokspel.universalspawn;

import me.lokspel.universalspawn.commands.CommandDispatcher;
import me.lokspel.universalspawn.commands.CommandDispatcher.RegisteredCommand;
import me.lokspel.universalspawn.commands.SetSpawnCommand;
import me.lokspel.universalspawn.commands.SpawnCommand;
import me.lokspel.universalspawn.config.MainConfig;
import me.lokspel.universalspawn.events.OnPlayerDeathEvent;
import me.lokspel.universalspawn.events.OnPlayerJoinEvent;
import me.lokspel.universalspawn.events.OnPlayerMoveEvent;
import me.lokspel.universalspawn.events.OnPlayerRespawnEvent;
import me.lokspel.universalspawn.world.SpawnLocation;
import com.tcoded.folialib.FoliaLib;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;

import java.util.List;
import java.util.Objects;

public final class UniversalSpawn extends JavaPlugin {

    private MainConfig mainConfig;
    private SpawnLocation spawnLocation;
    private FoliaLib foliaLib;

    @Override
    public void onEnable() {

        new Metrics(this, 33443);

        mainConfig = new MainConfig(this);

        foliaLib = new FoliaLib(this);

        spawnLocation = new SpawnLocation(this);
        spawnLocation.load();

        CommandDispatcher dispatcher = new CommandDispatcher(mainConfig, List.of(
                new RegisteredCommand("set", new SetSpawnCommand(this))
        ), new SpawnCommand(this));
        var spawnCmd = Objects.requireNonNull(getCommand("spawn"));
        spawnCmd.setExecutor(dispatcher);
        spawnCmd.setTabCompleter(dispatcher);

        getServer().getPluginManager().registerEvents(new OnPlayerDeathEvent(this), this);
        getServer().getPluginManager().registerEvents(new OnPlayerJoinEvent(this), this);
        getServer().getPluginManager().registerEvents(new OnPlayerMoveEvent(this), this);
        getServer().getPluginManager().registerEvents(new OnPlayerRespawnEvent(this), this);
    }

    @Override
    public void onDisable() {
        if (foliaLib != null) {
            foliaLib.getScheduler().cancelAllTasks();
        }
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public SpawnLocation getSpawnLocation() {
        return spawnLocation;
    }

    public FoliaLib getFoliaLib() {
        return foliaLib;
    }
}
