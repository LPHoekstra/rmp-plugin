package com.rmp.signWaypoint;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

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

    public static PlayerWaypoints getByPlayer(Player player) {
        return playerWaypointsList.stream()
            .filter(playerWaypoints -> playerWaypoints.getPlayer().equals(player))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No waypoint list found for player: " + player.getName()));
    }
}
