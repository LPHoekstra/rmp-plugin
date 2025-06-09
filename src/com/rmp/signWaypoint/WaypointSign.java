package com.rmp.signWaypoint;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class WaypointSign {
    public static final String WAYPOINT_IDENTIFIER = "waypoint";
    public static final ChatColor WAYPOINT_COLOR = ChatColor.BLUE;
    public static final String WAYPOINT_FIRSTLINE = WAYPOINT_COLOR + WAYPOINT_IDENTIFIER;

    public WaypointSign() {
    }

    public static boolean isWaypointRename(SignSide currentSignSide) {
        return currentSignSide.getLine(0).trim().equals(WAYPOINT_FIRSTLINE);
    }

    public static boolean isNewWaypoint(SignChangeEvent event) {
        return event.getLine(0).trim().equals(WAYPOINT_IDENTIFIER);
    }

    public static boolean isWaypointSign(Sign sign) {
        return sign.getSide(Side.BACK).getLine(0).equals(WaypointSign.WAYPOINT_FIRSTLINE)
                || sign.getSide(Side.FRONT).getLine(0).equals(WaypointSign.WAYPOINT_FIRSTLINE);
    }

    /**
     * Verify if the player have a waypoint named like the sign name  
     * @param player
     * @param targetSide
     */
    public static boolean isSignBelongsToPlayer(UUID playerUUID, SignSide targetSide) {
        List<RegisteredWaypoint> registeredWaypointsList = WaypointManager.getByPlayerId(playerUUID).getList();
        Optional<RegisteredWaypoint> registeredWaypointOptional = registeredWaypointsList.stream()
            .filter(waypoint -> waypoint.getName().equals(targetSide.getLine(1)))
            .findFirst();

        if (registeredWaypointOptional.isEmpty()) {
            return false;
        }

        return true;
    }

    // TODO handle when another player, adding the name of the player on the sign ?
    public static void renameWaypoint(SignChangeEvent event, String newName, SignSide currentSignSide) {
        String oldName = currentSignSide.getLine(1);
        Player player = event.getPlayer();

        List<RegisteredWaypoint> playerWaypointsList = WaypointManager.getByPlayerId(player.getUniqueId()).getList();

        RegisteredWaypoint registeredWaypointToRename = playerWaypointsList.stream()
            .filter(playerWaypoint -> playerWaypoint.getName().equals(oldName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(""))
        ;

        registeredWaypointToRename.setName(newName);
        // to keep the blue color
        event.setLine(0, WAYPOINT_FIRSTLINE);
        event.setLine(2, player.getName());

        player.sendMessage("Nom changer de: " + oldName + " a: " + newName);
    }

    // TODO a sign must belong to a player and only this player remove or rename the sign
    public static void createWaypoint(SignChangeEvent event, String name) {
        Location location = event.getBlock().getLocation();

        if (!name.trim().isEmpty()) {
            event.setLine(0, WAYPOINT_FIRSTLINE);
            PlayerWaypoints playerWaypoints = WaypointManager.getByPlayerId(event.getPlayer().getUniqueId());
            playerWaypoints.addToList(new RegisteredWaypoint(name, location, event.getPlayer()));

            // set player name 
            event.setLine(2, event.getPlayer().getName());

            event.getPlayer().sendMessage("Waypoint " + name + " créé");
        }
    }

    // TODO handle when the sign destroyed from another source
    public static void removeWaypoint(BlockBreakEvent event) {
        PlayerWaypoints playerWaypoints = WaypointManager.getByPlayerId(event.getPlayer().getUniqueId());

        RegisteredWaypoint registeredWaypointToRemove = playerWaypoints.getList().stream()
            .filter(waypoint -> waypoint.getLocation().equals(event.getBlock().getLocation()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Waypoint not in the list"))
        ;

        playerWaypoints.removeFromList(registeredWaypointToRemove);
        event.getPlayer().sendMessage("Waypoint " + registeredWaypointToRemove.getName() + " supprimer");
    }
}