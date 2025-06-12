package com.rmp.waypoint;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import com.rmp.model.PlayerWaypoints;
import com.rmp.model.RegisteredWaypoint;

// TODO a player can "discover" a new waypoint that belongs to others players
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

    private static boolean isWallSign(Sign sign) {
        return sign.getBlockData() instanceof WallSign;
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

    public static void renameWaypoint(String newName, Location waypointLocation, Player player) {
        List<RegisteredWaypoint> playerWaypointsList = WaypointManager.getByPlayerId(player.getUniqueId()).getList();

        RegisteredWaypoint registeredWaypointToRename = playerWaypointsList.stream()
            .filter(playerWaypoint -> playerWaypoint.getLocation().equals(waypointLocation))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Waypoint not find"))
        ;

        registeredWaypointToRename.setName(newName);

        player.sendMessage("Nom du waypoint modifié: " + newName);
    }

    public static void createWaypoint(String waypointName, Location waypointLocation, Player player) {       
        if (!waypointName.trim().isEmpty()) {
            
            PlayerWaypoints playerWaypoints = WaypointManager.getByPlayerId(player.getUniqueId());
            playerWaypoints.addToList(new RegisteredWaypoint(waypointName, waypointLocation, player));
        
            player.sendMessage("Waypoint " + waypointName + " créé");
        }
    }

    // TODO a player can remove waypoint with a command
    public static void removeWaypoint(Location waypointLocation, Player player) {
        PlayerWaypoints playerWaypoints = WaypointManager.getByPlayerId(player.getUniqueId());

        RegisteredWaypoint registeredWaypointToRemove = playerWaypoints.getList().stream()
            .filter(waypoint -> waypoint.getLocation().equals(waypointLocation))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Waypoint not in the list"))
        ;

        playerWaypoints.removeFromList(registeredWaypointToRemove);
        player.sendMessage("Waypoint " + registeredWaypointToRemove.getName() + " supprimé");
    }
}