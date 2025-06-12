package com.rmp.eventHandler;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSignOpenEvent;

import com.rmp.sign.WaypointCreateSign;
import com.rmp.sign.WaypointRenameSign;
import com.rmp.waypoint.WaypointInventory;
import com.rmp.waypoint.WaypointManager;
import com.rmp.waypoint.WaypointSign;

public class Waypoint implements Listener {

    @EventHandler
    public void onInteractWithACompass(PlayerInteractEvent event) {
        if (event.getMaterial().equals(Material.COMPASS)) {
            new WaypointInventory(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        WaypointManager.addToList(event.getPlayer().getUniqueId());
    }

    // TODO need to test if another player can modify a waypoint
    @EventHandler
    public void onSignOpenEvent(PlayerSignOpenEvent event) {
        SignSide targetSide = event.getSign().getTargetSide(event.getPlayer());
        Sign sign = event.getSign();

        if (WaypointSign.isWaypointSign(sign)) {
            if (WaypointSign.isSignBelongsToPlayer(event.getPlayer().getUniqueId(), targetSide)) {
                new WaypointRenameSign(sign);        
            } else {
                event.setCancelled(true);
            }
        } else {
            new WaypointCreateSign(sign);
        }
    }

    // TODO need to test if another player can destroy it
    // TODO protect the sign from burning
    @EventHandler
    public void onBlockBreakByPlayerEvent(BlockBreakEvent event) {
        if (isTryingBreakUnderWaypoint(event) || isTryingBreakWaypoint(event) ) {
            event.setCancelled(true);
        }
    }

    /**
     * if the player try to break the block under a waypoint sign
     * @return true if the block above is a waypoint sign, otherwise return false.
     */ 
    private boolean isTryingBreakUnderWaypoint(BlockBreakEvent event) {
        Block aboveBlock = event.getBlock().getWorld().getBlockAt(event.getBlock().getX(), event.getBlock().getY() + 1, event.getBlock().getZ());
        if (aboveBlock.getState() instanceof Sign sign) {
            if (WaypointSign.isWaypointSign(sign)) {
                event.getPlayer().sendMessage("Block support d'un waypoint!");
                return true;
            }
        }

        return false;
    }
    
    /**
     * if the player try to break a waypoint sign
     * @return true if the waypoint doesn't belongs to the player,
     * otherwise return false.
     */
    private boolean isTryingBreakWaypoint(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Sign sign) {
            if (WaypointSign.isWaypointSign(sign)) {
                Player player = event.getPlayer();
        
                if (WaypointSign.isSignBelongsToPlayer(player.getUniqueId(), sign.getSide(Side.FRONT))) {
                    WaypointSign.removeWaypoint(sign.getLocation(), player);    
                } else {
                    player.sendMessage("Ce waypoint ne t'appartiens pas!");
                    return true;
                }
            } 
        }
        
        return false;
    }

    /**
     * protect a sign from an explosion
     */
    @EventHandler
    public void onSignExplodeEvent(EntityExplodeEvent event) {
        List<Block> blockExplosionList = event.blockList();

        for (Block block : blockExplosionList) {
            // if a waypoint sign is in the list
            if (block.getState() instanceof Sign sign) {
                if (WaypointSign.isWaypointSign(sign)) {
                    blockExplosionList.remove(block);
                    
                    Block underSignBlock = event.getEntity().getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ());
                    
                    // if the block under the sign is in the list, he's removed
                    if (blockExplosionList.contains(underSignBlock)) {
                        blockExplosionList.remove(underSignBlock);
                    }
                }
            }
        }
    }
}
