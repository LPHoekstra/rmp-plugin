package com.rmp.repository;

import java.util.List;

import com.rmp.model.PlayerWaypoint;
import com.rmp.utils.FileHandler;
import com.rmp.utils.Json;
import com.rmp.waypoint.WaypointManager;

public class WaypointRepository implements Repository<PlayerWaypoint> {
    private static final String FILENAME = "waypoint.json";

    // TODO if an exception is catch, the file is not changed.
    @Override
    public boolean saveAll() {
        List<PlayerWaypoint> playerWaypointsList  = WaypointManager.getPlayerWaypointsList();

        String playerWaypointsJson = Json.createJsonFromList(playerWaypointsList);

        if (playerWaypointsJson == "") {
            return false;
        }

        boolean isFileWrite = new FileHandler(FILENAME).writeFile(playerWaypointsJson);

        if (!isFileWrite) {
            return false;
        }

        return true;
    }

    @Override
    public List<PlayerWaypoint> findAll() {
        String fileData = new FileHandler(FILENAME).getFileData();
        return Json.readJsonToList(fileData);
    }
}
