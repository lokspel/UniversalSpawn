package me.lokspel.universalspawn.commands;

import me.lokspel.universalspawn.UniversalSpawn;
import me.lokspel.universalspawn.config.MessagesConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SetSpawnCommand implements SubCommand {

    private final UniversalSpawn plugin;
    private final MessagesConfig messages;

    public SetSpawnCommand(UniversalSpawn plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMainConfig().messages();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            messages.send(sender, "player-only");
            return true;
        }

        if (!player.hasPermission("universalspawn.spawn.set")) {
            messages.send(player, "no-permission");
            return true;
        }

        plugin.getSpawnLocation().setLocation(player.getLocation());
        plugin.getSpawnLocation().save(player.getLocation());
        messages.send(player, "spawn-set");
        return true;
    }
}
