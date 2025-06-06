package com.rmp.signWaypoint;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WaypointManager {
    private static List<PlayerWaypoints> playerWaypointsList = new ArrayList<PlayerWaypoints>();

    public static List<PlayerWaypoints> getPlayerWaypointsList() {
        return playerWaypointsList;
    }

    /**
     * If no playerWaypoints with the player is in list, the waypoints are added.
     * @param waypoint
     */
    public static void addToList(PlayerWaypoints waypoint) {
        for (PlayerWaypoints playerWaypoints : playerWaypointsList) {
            if (playerWaypoints.getPlayer() == waypoint.getPlayer()) {
                return;
            }
        }

        playerWaypointsList.add(waypoint);
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
