package com.rmp.signWaypoint;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RegisteredWaypoint {
    private Location location;
    private String name;
    private Player player;

    public RegisteredWaypoint(String name, Location location, Player player) {
        this.name = name;
        this.location = location;
        this.player = player;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return this.player;
    }
}
