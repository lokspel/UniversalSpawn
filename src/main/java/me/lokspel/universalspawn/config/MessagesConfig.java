package me.lokspel.universalspawn.config;

import me.lokspel.universalspawn.UniversalSpawn;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class MessagesConfig {

    private static final String NOT_FOUND = "<red>Message not found: %s</red>";
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();

    private final FileConfiguration config;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public MessagesConfig(UniversalSpawn plugin) {
        this.config = YamlConfiguration.loadConfiguration(loadFile(plugin));
    }

    private static File loadFile(UniversalSpawn plugin) {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        return file;
    }

    public Component get(String key) {
        return miniMessage.deserialize(config.getString(key, NOT_FOUND.formatted(key)));
    }

    public void send(CommandSender sender, String key) {
        sender.sendMessage(LEGACY.serialize(get(key)));
    }
}