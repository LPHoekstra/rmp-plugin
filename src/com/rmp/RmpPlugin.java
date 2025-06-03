package com.rmp;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.rmp.command.CommandWp;
import com.rmp.signWaypoint.EventWaypoint;

public class RmpPlugin extends JavaPlugin {
    private PluginManager pluginManager = getServer().getPluginManager();

    @Override
    public void onEnable() {
        // register events
        pluginManager.registerEvents(new EventListener(), this);
        pluginManager.registerEvents(new EventWaypoint(), this);
        // register recipe
        new PluginRecipe();
        // register command
        this.getCommand("wp").setExecutor(new CommandWp());
    }

    @Override
    public void onDisable() {
    }
}
