package me.lokspel.spawnauth;

import me.lokspel.spawnauth.config.MessagesConfig;
import me.lokspel.spawnauth.config.SettingsConfig;
import me.lokspel.spawnauth.commands.SpawnCommand;
import me.lokspel.spawnauth.events.OnPlayerDeathEvent;
import me.lokspel.spawnauth.events.OnPlayerJoinEvent;
import me.lokspel.spawnauth.events.OnPlayerMoveEvent;
import me.lokspel.spawnauth.events.OnPlayerRespawnEvent;
import me.lokspel.spawnauth.utils.FoliaAPI;
import me.lokspel.spawnauth.world.SpawnLocation;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpawnAuth extends JavaPlugin {
    private static SpawnAuth instance;

    private SettingsConfig settingsConfig;
    private MessagesConfig messagesConfig;
    private SpawnLocation spawnLocation;

    @Override
    public void onEnable() {
        instance = this;
        FoliaAPI.init(this);

        saveDefaultConfig();
        reloadConfig();

        settingsConfig = new SettingsConfig(this);
        settingsConfig.load();
        messagesConfig = new MessagesConfig(this);
        spawnLocation = new SpawnLocation(this);

        spawnLocation.load();

        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getServer().getPluginManager().registerEvents(new OnPlayerDeathEvent(this), this);
        getServer().getPluginManager().registerEvents(new OnPlayerJoinEvent(this), this);
        getServer().getPluginManager().registerEvents(new OnPlayerMoveEvent(this), this);
        getServer().getPluginManager().registerEvents(new OnPlayerRespawnEvent(this), this);
    }

    @Override
    public void onDisable() {
        FoliaAPI.cancelAllTasks();
        instance = null;
    }

    public static SpawnAuth getInstance() {
        return instance;
    }

    public SpawnLocation getSpawnLocation() {
        return spawnLocation;
    }

    public SettingsConfig getSettingsConfig() {
        return settingsConfig;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }
}
