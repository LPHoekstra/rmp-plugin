package com.rmp.eventHandler;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerSignOpenEvent;
import com.rmp.signWaypoint.ListWaypoint;
import com.rmp.signWaypoint.RegisteredWaypoint;
import com.rmp.signWaypoint.WaypointSign;

// TODO refactoring
public class Waypoint implements Listener {

    @EventHandler
    public void onSignOpenEvent(PlayerSignOpenEvent event) {
        SignSide targetSide = event.getSign().getTargetSide(event.getPlayer());

        new WaypointSign(targetSide);
    }

    // remove the waypoint on sign break
    @EventHandler
    public void onSignBreakEvent(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Sign sign) {

            if (sign.getSide(Side.BACK).getLine(0).equals(ChatColor.BLUE + "waypoint") ||
                    sign.getSide(Side.FRONT).getLine(0).equals(ChatColor.BLUE + "waypoint")) {

                int i = 0;
                boolean isRemoved = false;

                // iterate on the list till the element isRemoved or the list is completely
                // iterate
                while (i <= ListWaypoint.getList().size() || !isRemoved) {
                    RegisteredWaypoint registeredWaypoint = ListWaypoint.getList().get(i);

                    if (event.getBlock().getLocation().equals(registeredWaypoint.getLocation())) {
                        isRemoved = true;
                        ListWaypoint.removeFromList(registeredWaypoint);
                        event.getPlayer().sendMessage("waypoint détruit");
                    }

                    i++;
                }
            }
        }
    }
}
