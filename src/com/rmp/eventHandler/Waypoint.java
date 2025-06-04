package com.rmp.eventHandler;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import com.rmp.signWaypoint.ListWaypoint;
import com.rmp.signWaypoint.RegisteredWaypoint;

public class Waypoint implements Listener {

    // add a waypoint on sign validation
    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event) {
        // TODO handle when the name is change
        String firstLine = event.getLine(0);

        if (firstLine.equals("waypoint")) {
            String secondLine = event.getLine(1);
            Location location = event.getBlock().getLocation();

            // TODO improve name verification
            if (secondLine != "") {
                event.setLine(0, ChatColor.BLUE + firstLine);
                ListWaypoint.addToList(new RegisteredWaypoint(secondLine, location, event.getPlayer()));

                event.getPlayer().sendMessage("Waypoint " + secondLine + " créé");
            }
        }
    }

    // remove the waypoint on sign break
    @EventHandler
    public void onSignBreakEvent(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.OAK_SIGN) {
            // TODO verification if the Sign is a waypoint, to not iterate on the list
            int i = 0;
            boolean isRemoved = false;

            // iterate on the list till the element isRemoved or the list is completely
            // iterate
            while (i <= ListWaypoint.getList().size() || !isRemoved) {
                RegisteredWaypoint registeredWaypoint = ListWaypoint.getList().get(i);

                if (event.getBlock().getLocation().equals(registeredWaypoint.getLocation())) {
                    isRemoved = true;
                    ListWaypoint.removeFromList(registeredWaypoint);
                    event.getPlayer().sendMessage("waypoint détruit");
                }

                i++;
            }
        }
    }
}
