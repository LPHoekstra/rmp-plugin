package com.rmp.signWaypoint;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WaypointInventory {
    private Inventory inventory;
    
    public WaypointInventory(Player player) {
        this.inventory = Bukkit.createInventory(player, 27, "Waypoints");
        
        setItemsInventory(createWaypointsItems(player));

        openInventory(player);
    }

    // TODO on selection of the item the player is teleported to the waypoint.
    // private void onClickWaypointItem() {

    // }
    
    private void setItemsInventory(ItemStack[] waypointsItemStacksList) {
        inventory.setStorageContents(waypointsItemStacksList);
    }
    
    private ItemStack[] createWaypointsItems(Player player) {
        List<RegisteredWaypoint> registeredWaypointsList = WaypointManager.getByPlayerId(player.getUniqueId()).getList();
        List<ItemStack> waypointsItemStacksList = new ArrayList<ItemStack>();

        for (RegisteredWaypoint registeredWaypoints : registeredWaypointsList) {
            List<String> coordinatesList = new ArrayList<String>();
            coordinatesList.add("X: " + registeredWaypoints.getLocation().getX());
            coordinatesList.add("Y: " + registeredWaypoints.getLocation().getY());
            coordinatesList.add("Z: " + registeredWaypoints.getLocation().getZ());
            
            ItemStack bookStack = new ItemStack(Material.BOOK, 1);
            ItemMeta bookMeta = bookStack.getItemMeta();
            bookMeta.setDisplayName(registeredWaypoints.getName());
            bookMeta.setLore(coordinatesList);
            
            bookStack.setItemMeta(bookMeta);

            waypointsItemStacksList.add(bookStack);
        }

        return waypointsItemStacksList.toArray(new ItemStack[waypointsItemStacksList.size()]);
    }
    
    private void openInventory(Player player) {
        player.openInventory(inventory);
    }
}
