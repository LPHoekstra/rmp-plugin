package com.rmp.repository;

import java.util.List;

import com.rmp.model.PlayerWaypoint;
import com.rmp.utils.FileHandler;
import com.rmp.waypoint.WaypointManager;

public class WaypointRepository implements Repository<PlayerWaypoint> {
    private static final String FILENAME = "waypoint.json";

    // TODO if an exception is catch, the file is not changed.

    // TODO to implement
    public boolean saveAll() {
        WaypointManager.getPlayerWaypointsList();
        String test = "to implement";

        new FileHandler(FILENAME).writeFile(test);

        return true;
    }

    public List<PlayerWaypoint> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
}
