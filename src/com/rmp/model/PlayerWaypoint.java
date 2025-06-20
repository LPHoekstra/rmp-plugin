package com.rmp.model;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;

// TODO add data persistence. In player entity with PersistentDataContainer ? or writting a json file
// gonna write in a new file to get some experience with it and seems to suit better for the need.
// if stored in player entity, can have problem on waypoint removal when player are not connected.
public class PlayerWaypoint {
    private List<RegisteredWaypoint> registeredWaypointList;
    private UUID playerUuid;
    private String playerName;

    public PlayerWaypoint(List<RegisteredWaypoint> registeredWaypointList, UUID playerId, String playerName) {
        this.registeredWaypointList = registeredWaypointList;
        this.playerUuid = playerId;
        this.playerName = playerName;
    }

    public List<RegisteredWaypoint> getRegisteredWaypointList() {
        return this.registeredWaypointList;
    }

    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void addToList(RegisteredWaypoint waypoint) {
        this.registeredWaypointList.add(waypoint);
    }

    public void removeFromList(RegisteredWaypoint waypoint) {
        this.registeredWaypointList.remove(waypoint);
    }

    /**
     * Find a RegisteredWaypoint from a PlayerWaypoints by the location
     * @param playerWaypoints from belongs the waypoint 
     * @param location the location of the waypoint to find
     * @return
     */
    public static RegisteredWaypoint getRegisteredWaypointByLocation(PlayerWaypoint playerWaypoints, Location location) {
        return playerWaypoints.getRegisteredWaypointList().stream()
            .filter(playerWaypoint -> playerWaypoint.getLocation().equals(location))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No waypoint found for location: " + location.toString()));
    }
}
