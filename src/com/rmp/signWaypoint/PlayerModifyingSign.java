package com.rmp.signWaypoint;

import org.bukkit.Bukkit;
import org.bukkit.block.sign.SignSide;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.rmp.RmpPlugin;

public class PlayerModifyingSign implements Listener {
    private SignSide currentSignSide;

    public PlayerModifyingSign(SignSide targetSide) {
        this.currentSignSide = targetSide;
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(RmpPlugin.class));
    }

    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event) {
        String newName = event.getLine(1);

        // handle the rename
        if (WaypointSign.isWaypointRename(currentSignSide)) {
            WaypointSign.renameWaypoint(event, newName, currentSignSide);
        }

        // create a new waypoint
        else if (WaypointSign.isNewWaypoint(event)) {
            WaypointSign.createWaypoint(event, newName);
        }

        // to have a new value of currentSignSide
        SignChangeEvent.getHandlerList().unregister(this);
    }
}
