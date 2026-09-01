package me.lokspel.universalspawn.config;

import me.lokspel.universalspawn.UniversalSpawn;
import me.lokspel.universalspawn.config.section.DeathSection;
import me.lokspel.universalspawn.config.section.JoinSection;
import me.lokspel.universalspawn.config.section.VoidSection;

public final class MainConfig {

    private final UniversalSpawn plugin;
    private final JoinSection join;
    private final DeathSection death;
    private final VoidSection voidSection;
    private MessagesConfig messages;

    public MainConfig(UniversalSpawn plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.join = new JoinSection(plugin);
        this.death = new DeathSection(plugin);
        this.voidSection = new VoidSection(plugin);
        this.messages = new MessagesConfig(plugin);
    }

    public void load() {
        plugin.reloadConfig();
        messages = new MessagesConfig(plugin);
    }

    public JoinSection join() {
        return join;
    }

    public DeathSection death() {
        return death;
    }

    public VoidSection voidSection() {
        return voidSection;
    }

    public MessagesConfig messages() {
        return messages;
    }
}
