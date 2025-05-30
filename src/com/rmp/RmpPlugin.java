package com.rmp;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RmpPlugin extends JavaPlugin {
    private PluginManager pluginManager = getServer().getPluginManager();

    @Override
    public void onEnable() {
        pluginManager.registerEvents(new EventListener(), this);
        new PluginRecipe();
    }

    @Override
    public void onDisable() {
    }
}
