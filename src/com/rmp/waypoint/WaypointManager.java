package com.rmp.waypoint;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.rmp.model.PlayerWaypoints;

public class WaypointManager {
    private static List<PlayerWaypoints> playerWaypointsList = new ArrayList<PlayerWaypoints>();

    public static List<PlayerWaypoints> getPlayerWaypointsList() {
        return playerWaypointsList;
    }

    /**
     * Used to create a PlayerWaypoints instance.
     * 
     * If there is no playerWaypoints with the player in the list, he's added to the list.
     * @param waypoint
     */
    public static void addToList(Player player) {
        for (PlayerWaypoints playerWaypoints : playerWaypointsList) {
            if (playerWaypoints.getPlayerId() == player.getUniqueId()) {
                return;
            }
        }

        playerWaypointsList.add(new PlayerWaypoints(player.getUniqueId(), player.getName()));
    }

    /**
     * Get a specific PlayerWaypoints with the uuid of the player
     * @param playerId
     * @return
     */
    public static PlayerWaypoints getByPlayerId(UUID playerId) {
        return playerWaypointsList.stream()
            .filter(playerWaypoints -> playerWaypoints.getPlayerId().equals(playerId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No waypoint list found for player: " + playerId.toString()));
    }

    /**
     * Get a specific PlayerWaypoints with the player name
     * @param playerName
     * @return
     */
    public static PlayerWaypoints getByPlayerName(String playerName) {
        return playerWaypointsList.stream()
            .filter(playerWaypoints -> playerWaypoints.getPlayerName().equals(playerName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No waypoint list found for player: " + playerName));
    }
}
