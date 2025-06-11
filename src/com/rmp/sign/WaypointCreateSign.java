package com.rmp.sign;

import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import com.rmp.waypoint.WaypointSign;

public class WaypointCreateSign extends PlayerModifyingSign {

    public WaypointCreateSign(Sign sign) {
        super(sign);
    }

    @Override
    protected void handleEvent(SignChangeEvent event, String newName) {
        if (WaypointSign.isNewWaypoint(sign, event.getLine(0))) {
            WaypointSign.createWaypoint(event.getLine(1), event.getBlock().getLocation(), event.getPlayer());

            setWaypointLines(event);
        }
    }
}
