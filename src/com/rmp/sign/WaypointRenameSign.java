package com.rmp.sign;

import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import com.rmp.waypoint.WaypointSign;

public class WaypointRenameSign extends PlayerModifyingSign {
    
    public WaypointRenameSign(Sign sign) {
        super(sign);
    }

    @Override
    protected void handleEvent(SignChangeEvent event, String newName) {
        WaypointSign.renameWaypoint(sign, newName, event.getPlayer());
    }
}
