package com.rmp.signWaypoint;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

/**
 * TODO Open a chest inventory with a compass, this chest contain items
 * corresponding to the waypoint of the player. Those items have the name of the
 * waypoint as title and have the coordinates in description. On selection of
 * the item the player is teleported to the waypoint.
 */
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

        PlayerWaypoints playerWaypoints = WaypointManager.getByPlayerId(event.getPlayer().getUniqueId());
        List<RegisteredWaypoint> playerWaypointsList = playerWaypoints.getList();

        while (!isFound || i < playerWaypointsList.size()) {
            RegisteredWaypoint registeredWaypoints = playerWaypointsList.get(i);

            if (registeredWaypoints.getName().equals(oldName)) {
                isFound = true;
                registeredWaypoints.setName(newName);
                // to keep the blue color
                event.setLine(0, WAYPOINT_FIRSTLINE);

                event.getPlayer().sendMessage("Nom changer de: " + oldName + " a: " + newName);
            }

            i++;
        }
    }

    public static void createWaypoint(SignChangeEvent event, String name) {
        Location location = event.getBlock().getLocation();

        if (!name.trim().isEmpty()) {
            event.setLine(0, WAYPOINT_FIRSTLINE);
            PlayerWaypoints playerWaypoints = WaypointManager.getByPlayerId(event.getPlayer().getUniqueId());
            playerWaypoints.addToList(new RegisteredWaypoint(name, location, event.getPlayer()));

            event.getPlayer().sendMessage("Waypoint " + name + " créé");
        }
    }

    public static void removeWaypoint(BlockBreakEvent event) {
        int i = 0;
        boolean isRemoved = false;

        PlayerWaypoints playerWaypoints = WaypointManager.getByPlayerId(event.getPlayer().getUniqueId());

        /**
         * iterate on the list till the element isRemoved or the list is completely
         * iterate
         */
        while (i <= playerWaypoints.getList().size() || !isRemoved) {
            RegisteredWaypoint registeredWaypoint = playerWaypoints.getList().get(i);

            if (event.getBlock().getLocation().equals(registeredWaypoint.getLocation())) {
                isRemoved = true;
                playerWaypoints.removeFromList(registeredWaypoint);
                event.getPlayer().sendMessage("waypoint détruit");
            }

            i++;
        }
    }
}