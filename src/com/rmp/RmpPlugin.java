package com.rmp;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.rmp.command.CommandWp;
import com.rmp.eventHandler.DropMobEggOnDeath;
import com.rmp.eventHandler.DropPlayerHeadOnDeath;
import com.rmp.eventHandler.Waypoint;
import com.rmp.recipe.Recipe;

public class RmpPlugin extends JavaPlugin {
    private PluginManager pluginManager = getServer().getPluginManager();

    @Override
    public void onEnable() {
        // register events
        pluginManager.registerEvents(new DropMobEggOnDeath(), this);
        pluginManager.registerEvents(new DropPlayerHeadOnDeath(), this);
        pluginManager.registerEvents(new Waypoint(), this);
        // register recipe
        new Recipe();
        // register command
        this.getCommand("wp").setExecutor(new CommandWp());
    }

    @Override
    public void onDisable() {
    }
}
