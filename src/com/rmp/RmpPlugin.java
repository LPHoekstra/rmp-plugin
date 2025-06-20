package com.rmp;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.rmp.command.CommandWp;
import com.rmp.eventHandler.DropMobEggOnDeath;
import com.rmp.eventHandler.DropPlayerHeadOnDeath;
import com.rmp.eventHandler.Waypoint;
import com.rmp.recipe.Recipe;
import com.rmp.repository.Json;
import com.rmp.repository.WaypointRepository;
import com.rmp.waypoint.WaypointManager;

public class RmpPlugin extends JavaPlugin {
    private PluginManager pluginManager = getServer().getPluginManager();

    @Override
    public void onEnable() {
        // must fetch the json data from waypoint.json
        String waypointData = new WaypointRepository(getLogger()).getFileData();
        Json.readJsonToPlayerWaypointsList(waypointData);
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
        // must save the data from WaypointManager list in waypoint.json
        // new WaypointRepository(getLogger()).writeFile(WaypointManager.getPlayerWaypointsList());;
    }
}
