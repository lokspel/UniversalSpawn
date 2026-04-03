package me.lokspel.universalspawn.config;

import me.lokspel.universalspawn.UniversalSpawn;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class MessagesConfig {
    private final UniversalSpawn plugin;
    private final MiniMessage miniMessage;
    private FileConfiguration config;

    public MessagesConfig(UniversalSpawn plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        load();
    }

    public void load() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public Component getMessage(String path) {
        String message = config.getString(path, "<red>Message not found: " + path + "</red>");
        return miniMessage.deserialize(message);
    }

    public void send(CommandSender sender, String path) {
        sender.sendMessage(getMessage(path));
    }
}
