package com.rmp.signWaypoint;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO add data persistence
public class PlayerWaypoints {
    // TODO can't have 2 RegisteredWaypoint with the same name
    private List<RegisteredWaypoint> list = new ArrayList<RegisteredWaypoint>();
    private UUID playerId;

    public PlayerWaypoints(UUID player) {
        this.playerId = player;
    }

    public UUID getPlayer() {
        return this.playerId;
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
}
