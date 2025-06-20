package com.rmp.waypoint;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.rmp.model.PlayerWaypoint;
import com.rmp.model.RegisteredWaypoint;

// must be a singleton
public class WaypointManager {
    private static List<PlayerWaypoint> playerWaypointsList = new ArrayList<PlayerWaypoint>();

    public static List<PlayerWaypoint> getPlayerWaypointsList() {
        return playerWaypointsList;
    }

    public static void setList(List<PlayerWaypoint> playerWaypoints) {
        Bukkit.getLogger().info(playerWaypoints.toString());
        playerWaypointsList = playerWaypoints;
    }

    /**
     * Used to create a PlayerWaypoints instance.
     * 
     * If there is no playerWaypoints with the player in the list, he's added to the list.
     * @param waypoint
     */
    public static void addToList(Player player) {
        boolean isPlayerNotInList = playerWaypointsList.stream()
            .filter(playerWaypoint -> playerWaypoint.getPlayerUuid().equals(player.getUniqueId()))
            .findFirst()
            .isEmpty()
        ;
        
        if (isPlayerNotInList) {
            playerWaypointsList.add(new PlayerWaypoint(new ArrayList<RegisteredWaypoint>(), player.getUniqueId(), player.getName()));
        }
    }

    /**
     * Get a specific PlayerWaypoints with the uuid of the player
     * @param playerId
     * @return
     */
    public static PlayerWaypoint getByPlayerId(UUID playerId) {
        return playerWaypointsList.stream()
            .filter(playerWaypoints -> playerWaypoints.getPlayerUuid().equals(playerId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No waypoint list found for player: " + playerId.toString()));
    }

    /**
     * Get a specific PlayerWaypoints with the player name
     * @param playerName
     * @return
     */
    public static PlayerWaypoint getByPlayerName(String playerName) {
        return playerWaypointsList.stream()
            .filter(playerWaypoints -> playerWaypoints.getPlayerName().equals(playerName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No waypoint list found for player: " + playerName));
    }
}
