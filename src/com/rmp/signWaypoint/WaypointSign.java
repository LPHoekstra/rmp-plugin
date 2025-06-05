package com.rmp.signWaypoint;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class WaypointSign {
    public static final String WAYPOINT_IDENTIFIER = "waypoint";
    public static final ChatColor WAYPOINT_COLOR = ChatColor.BLUE;
    public static final String WAYPOINT_FIRSTLINE = WAYPOINT_COLOR + WAYPOINT_IDENTIFIER;

    public WaypointSign() {
    }

    public static boolean isWaypointRename(SignSide currentSignSide) {
        return currentSignSide.getLine(0).equals(WAYPOINT_FIRSTLINE);
    }

    public static boolean isNewWaypoint(SignChangeEvent event) {
        return event.getLine(0).equals(WAYPOINT_IDENTIFIER);
    }

    public static boolean isWaypointSign(Sign sign) {
        return sign.getSide(Side.BACK).getLine(0).equals(WaypointSign.WAYPOINT_FIRSTLINE)
                || sign.getSide(Side.FRONT).getLine(0).equals(WaypointSign.WAYPOINT_FIRSTLINE);
    }

    public static void renameWaypoint(SignChangeEvent event, String newName, SignSide currentSignSide) {
        String oldName = currentSignSide.getLine(1);

        boolean isFound = false;
        int i = 0;

        while (!isFound || i < ListWaypoint.getList().size()) {
            RegisteredWaypoint registeredWaypoints = ListWaypoint.getList().get(i);

            if (registeredWaypoints.getName().equals(oldName)) {
                isFound = true;
                registeredWaypoints.setName(newName);
                // to keep the blue color
                event.setLine(0, WAYPOINT_FIRSTLINE);

                event.getPlayer().sendMessage("name changed from: " + oldName + " to: " + newName);
            }

            i++;
        }
    }

    public static void createWaypoint(SignChangeEvent event, String name) {
        Location location = event.getBlock().getLocation();

        if (!name.trim().isEmpty()) {
            event.setLine(0, WAYPOINT_FIRSTLINE);
            ListWaypoint.addToList(new RegisteredWaypoint(name, location, event.getPlayer()));

            event.getPlayer().sendMessage("Waypoint " + name + " créé");
        }
    }

    public static void removeWaypoint(BlockBreakEvent event) {
        int i = 0;
        boolean isRemoved = false;

        /**
         * iterate on the list till the element isRemoved or the list is completely
         * iterate
         */
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