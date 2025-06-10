package com.rmp.signWaypoint;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.rmp.model.PlayerWaypoints;

public class WaypointManager {
    private static List<PlayerWaypoints> playerWaypointsList = new ArrayList<PlayerWaypoints>();

    public static List<PlayerWaypoints> getPlayerWaypointsList() {
        return playerWaypointsList;
    }

    /**
     * Used to create a PlayerWaypoints instance
     * 
     * If there is no playerWaypoints with the player in the list, he's added to the list.
     * @param waypoint
     */
    public static void addToList(UUID playerUuid) {
        for (PlayerWaypoints playerWaypoints : playerWaypointsList) {
            if (playerWaypoints.getPlayer() == playerUuid) {
                return;
            }
        }

        playerWaypointsList.add(new PlayerWaypoints(playerUuid));
    }

    /**
     * Get a specific PlayerWaypoints with the uuid of the player
     * @param playerId
     * @return
     */
    public static PlayerWaypoints getByPlayerId(UUID playerId) {
        return playerWaypointsList.stream()
            .filter(playerWaypoints -> playerWaypoints.getPlayer().equals(playerId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No waypoint list found for player: " + playerId.toString()));
    }
}
