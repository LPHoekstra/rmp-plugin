package com.rmp.eventHandler;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
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

    // TODO need to test if another player can modify a waypoint
    @EventHandler
    public void onSignOpenEvent(PlayerSignOpenEvent event) {
        SignSide targetSide = event.getSign().getTargetSide(event.getPlayer());

        if (WaypointSign.isWaypointSign(event.getSign())) {
            if (WaypointSign.isSignBelongsToPlayer(event.getPlayer().getUniqueId(), targetSide)) {
                new PlayerModifyingSign(targetSide);        
            } else {
                event.setCancelled(true);
            }
        } else {
            new PlayerModifyingSign(targetSide);
        }
    }

    // TODO need to test if another player can destroy it
    // TODO permission for an op can anyway destroy it
    @EventHandler
    public void onBlockBreakByPlayerEvent(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Sign sign) {
            if (WaypointSign.isWaypointSign(sign)) {
                if (WaypointSign.isSignBelongsToPlayer(event.getPlayer().getUniqueId(), sign.getSide(Side.FRONT))) {
                    WaypointSign.removeWaypoint(event);    
                } else {
                    event.setCancelled(true);
                }
            } 
        }
    }

    // TODO handle when a sign explode
    @EventHandler
    public void onSignExplodeEvent(BlockExplodeEvent event) {
        if (event.getBlock().getState() instanceof Sign sign) {
            if (WaypointSign.isWaypointSign(sign)) {
                WaypointSign.removeWaypoint(null);
            }
        }
    }
}
