package com.rmp.model;

import java.util.UUID;

import org.bukkit.Location;

public class RegisteredWaypoint {
    private Location location;
    private String name;
    private UUID playerId;

    public RegisteredWaypoint(String name, Location location, UUID playerId) {
        this.name = name;
        this.location = location;
        this.playerId = playerId;
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

    public UUID getPlayerId() {
        return this.playerId;
    }
}
