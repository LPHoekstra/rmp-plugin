package com.rmp.signWaypoint;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.sign.SignSide;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.rmp.RmpPlugin;

// TODO refactoring
public class WaypointSign implements Listener {
    private SignSide currentSignSide;

    public WaypointSign(SignSide signSide) {
        this.currentSignSide = signSide;
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(RmpPlugin.class));
    }

    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event) {
        String firstLine = event.getLine(0);

        // handle the rename
        if (currentSignSide.getLine(0).equals(ChatColor.BLUE + "waypoint")) {
            boolean isFound = false;
            int i = 0;

            while (!isFound || i < ListWaypoint.getList().size()) {
                RegisteredWaypoint registeredWaypoints = ListWaypoint.getList().get(i);

                if (registeredWaypoints.getName().equals(currentSignSide.getLine(1))) {
                    isFound = true;
                    registeredWaypoints.setName(event.getLine(1));
                    // to keep the blue color
                    event.setLine(0, ChatColor.BLUE + "waypoint");

                    event.getPlayer().sendMessage(
                            "name changed from: " + currentSignSide.getLine(1) + " to: " + event.getLine(1));
                }

                i++;
            }
        }

        // create a new waypoint
        else if (firstLine.equals("waypoint")) {
            String secondLine = event.getLine(1);
            Location location = event.getBlock().getLocation();

            // TODO improve name verification
            if (secondLine != "") {
                event.setLine(0, ChatColor.BLUE + firstLine);
                ListWaypoint.addToList(new RegisteredWaypoint(secondLine, location, event.getPlayer()));

                event.getPlayer().sendMessage("Waypoint " + secondLine + " créé");
            }
        }

        SignChangeEvent.getHandlerList().unregister(this);
    }
}