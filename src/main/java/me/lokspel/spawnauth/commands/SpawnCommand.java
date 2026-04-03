package me.lokspel.spawnauth.commands;

import me.lokspel.spawnauth.SpawnAuth;
import me.lokspel.spawnauth.utils.FoliaAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SpawnCommand implements CommandExecutor {
    private final SpawnAuth plugin;

    public SpawnCommand(SpawnAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.getMessagesConfig().send(sender, "only-player");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("set")) {
            if (!player.hasPermission("universalspawn.spawn.set")) {
                plugin.getMessagesConfig().send(player, "no-permission");
                return true;
            }

            plugin.getSpawnLocation().setLocation(player.getLocation());
            plugin.getSpawnLocation().save(player.getLocation());
            plugin.getMessagesConfig().send(player, "spawn-set");
            return true;
        }

        if (!player.hasPermission("universalspawn.spawn.use")) {
            plugin.getMessagesConfig().send(player, "no-spawn-permission");
            return true;
        }

        if (plugin.getSpawnLocation().getLocation() == null) {
            plugin.getMessagesConfig().send(player, "spawn-missing");
            return true;
        }

        FoliaAPI.teleportPlayer(player, plugin.getSpawnLocation().getLocation(), true,
                () -> plugin.getMessagesConfig().send(player, "spawn-teleported"));
        return true;
    }
}
