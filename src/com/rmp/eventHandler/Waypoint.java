package com.rmp.eventHandler;

import org.bukkit.block.Sign;
import org.bukkit.block.sign.SignSide;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerSignOpenEvent;

import com.rmp.signWaypoint.PlayerModifyingSign;
import com.rmp.signWaypoint.WaypointSign;

public class Waypoint implements Listener {

    @EventHandler
    public void onSignOpenEvent(PlayerSignOpenEvent event) {
        SignSide targetSide = event.getSign().getTargetSide(event.getPlayer());

        new PlayerModifyingSign(targetSide);
    }

    @EventHandler
    public void onSignBreakEvent(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Sign sign) {

            if (WaypointSign.isWaypointSign(sign)) {
                WaypointSign.removeWaypoint(event);
            }
        }
    }
}
