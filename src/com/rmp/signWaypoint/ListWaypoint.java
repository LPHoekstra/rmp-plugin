package com.rmp.signWaypoint;

import java.util.ArrayList;
import java.util.List;

public class ListWaypoint {
    private static List<RegisteredWaypoint> list = new ArrayList<RegisteredWaypoint>();

    public static List<RegisteredWaypoint> getList() {
        return list;
    }

    public static void addToList(RegisteredWaypoint waypoint) {
        list.add(waypoint);
    }

    public static void removeFromList(RegisteredWaypoint waypoint) {
        list.remove(waypoint);
    }
}
