package com.rmp.signWaypoint;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

// TODO add data persistence
public class PlayerWaypoints {
    // TODO each player must have is own list of registeredWaypoint
    // TODO can't have 2 RegisteredWaypoint with the same name
    private List<RegisteredWaypoint> list = new ArrayList<RegisteredWaypoint>();
    // TODO need to change to UUID ?
    private Player player;

    public PlayerWaypoints(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
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
