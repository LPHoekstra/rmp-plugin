package com.rmp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;

// TODO add data persistence
public class PlayerWaypoints {
    private List<RegisteredWaypoint> list = new ArrayList<RegisteredWaypoint>();
    private UUID playerId;
    private String playerName;

    public PlayerWaypoints(UUID player, String playerName) {
        this.playerId = player;
        this.playerName = playerName;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public List<RegisteredWaypoint> getList() {
        return this.list;
    }

    public void addToList(RegisteredWaypoint waypoint) {
        this.list.add(waypoint);
    }

    public void removeFromList(RegisteredWaypoint waypoint) {
        this.list.remove(waypoint);
    }

    /**
     * Find a RegisteredWaypoint from a PlayerWaypoints by the location
     * @param playerWaypoints from belongs the waypoint 
     * @param location the location of the waypoint to find
     * @return
     */
    public static RegisteredWaypoint getRegisteredWaypointByLocation(PlayerWaypoints playerWaypoints, Location location) {
        return playerWaypoints.getList().stream()
            .filter(playerWaypoint -> playerWaypoint.getLocation().equals(location))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No waypoint found for location: " + location.toString()));
    }
}
