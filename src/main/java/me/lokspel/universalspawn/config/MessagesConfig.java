package me.lokspel.universalspawn.config;

import me.lokspel.universalspawn.UniversalSpawn;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class MessagesConfig {

    private static final String PREFIX = "prefix";
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();

    private final FileConfiguration config;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public MessagesConfig(UniversalSpawn plugin) {
        this.config = load(plugin);
    }

    private FileConfiguration load(UniversalSpawn plugin) {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public void send(CommandSender sender, String key) {
        String text = config.getString(key, "")
                .replace("%prefix%", config.getString(PREFIX, ""));
        sender.sendMessage(LEGACY.serialize(miniMessage.deserialize(text)));
    }
}
