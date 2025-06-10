package com.rmp.waypoint;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import com.rmp.model.PlayerWaypoints;
import com.rmp.model.RegisteredWaypoint;

public class WaypointSign {
    public static final String WAYPOINT_IDENTIFIER = "waypoint";
    public static final ChatColor WAYPOINT_COLOR = ChatColor.BLUE;
    public static final String WAYPOINT_FIRSTLINE = WAYPOINT_COLOR + WAYPOINT_IDENTIFIER;

    public WaypointSign() {
    }

    public static boolean isNewWaypoint(Sign sign, String firstLine) {
        return !isWallSign(sign) && 
            firstLine.trim().equals(WAYPOINT_IDENTIFIER);
    }

    public static boolean isWaypointSign(Sign sign) {
        return !isWallSign(sign) && 
            sign.getSide(Side.FRONT).getLine(0).trim().equals(WAYPOINT_FIRSTLINE);
    }

    // TODO need to test
    private static boolean isWallSign(Sign sign) {
        return sign instanceof WallSign;
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

    // TODO need to test if the event is needed to set Lines
    public static void renameWaypoint(Sign sign, String newName, Player player) {
        String oldName = sign.getSide(Side.FRONT).getLine(1);

        List<RegisteredWaypoint> playerWaypointsList = WaypointManager.getByPlayerId(player.getUniqueId()).getList();

        RegisteredWaypoint registeredWaypointToRename = playerWaypointsList.stream()
            .filter(playerWaypoint -> playerWaypoint.getLocation().equals(sign.getLocation()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Waypoint not find"))
        ;

        registeredWaypointToRename.setName(newName);
        // to keep the blue color and player name on sign
        sign.getSide(Side.FRONT).setLine(0, WAYPOINT_FIRSTLINE);
        sign.getSide(Side.FRONT).setLine(2, player.getName());

        player.sendMessage("Nom changer de: " + oldName + " a: " + newName);
    }

    /**
     * 
     * @param sign
     * @param waypointName
     * @param event must be an event to set new value for sign lines
     */
    public static void createWaypoint(String waypointName, SignChangeEvent event) {
        Player player = event.getPlayer();
          
        if (!waypointName.trim().isEmpty()) {
            
            PlayerWaypoints playerWaypoints = WaypointManager.getByPlayerId(player.getUniqueId());
            playerWaypoints.addToList(new RegisteredWaypoint(waypointName, event.getBlock().getLocation(), player));
            
            // set first line and player name
            event.setLine(0, WAYPOINT_FIRSTLINE);
            event.setLine(2, player.getName());
        
            player.sendMessage("Waypoint " + waypointName + " créé");
        }
    }

    // TODO a player can remove waypoint with a command
    // TODO handle when the sign destroyed from another source
    public static void removeWaypoint(Sign sign, Player player) {
        PlayerWaypoints playerWaypoints = WaypointManager.getByPlayerId(player.getUniqueId());

        RegisteredWaypoint registeredWaypointToRemove = playerWaypoints.getList().stream()
            .filter(waypoint -> waypoint.getLocation().equals(sign.getBlock().getLocation()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Waypoint not in the list"))
        ;

        playerWaypoints.removeFromList(registeredWaypointToRemove);
        player.sendMessage("Waypoint " + registeredWaypointToRemove.getName() + " supprimer");
    }
}