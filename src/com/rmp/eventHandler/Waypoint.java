package com.rmp.eventHandler;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.SignSide;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSignOpenEvent;

import com.rmp.signWaypoint.PlayerWaypoints;
import com.rmp.signWaypoint.WaypointInventory;
import com.rmp.signWaypoint.WaypointManager;
import com.rmp.signWaypoint.PlayerModifyingSign;
import com.rmp.signWaypoint.WaypointSign;

public class Waypoint implements Listener {

    @EventHandler
    public void onInteractWithACompass(PlayerInteractEvent event) {
        if (event.getMaterial().equals(Material.COMPASS)) {
            new WaypointInventory(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        WaypointManager.addToList(new PlayerWaypoints(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onSignOpenEvent(PlayerSignOpenEvent event) {
        SignSide targetSide = event.getSign().getTargetSide(event.getPlayer());

        new PlayerModifyingSign(targetSide);
    }

    // TODO left click with a sword must not execute the code
    @EventHandler
    public void onSignBreakEvent(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Sign sign) {

            if (WaypointSign.isWaypointSign(sign)) {
                WaypointSign.removeWaypoint(event);
            }
        }
    }
}
