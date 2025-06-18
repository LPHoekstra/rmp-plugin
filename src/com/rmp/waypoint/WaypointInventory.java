package com.rmp.waypoint;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.rmp.RmpPlugin;
import com.rmp.model.RegisteredWaypoint;

public class WaypointInventory implements Listener {
    private static final Material WAYPOINT_ITEM_MATERIAL = Material.BOOK;
    private Inventory inventory;
    private Player player;
    
    public WaypointInventory(Player player) {
        this.inventory = Bukkit.createInventory(player, 27, "Waypoints");
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(RmpPlugin.class));
        
        setItemsInventory(createWaypointsItems());

        openInventory();
    }

    @EventHandler
    private void onClickWaypointItem(InventoryClickEvent event) {
        // when a player click on item from WaypointInventory
        if (event.getCurrentItem() != null && event.getClickedInventory() != null) {
            if (isClickedOnWaypointItem(event.getClickedInventory(), event.getCurrentItem().getType())) {
                teleportPlayerToWaypoint(event.getCurrentItem());
                return;
            }
        }

        if (isMovingItemsInWaypointInventory(event)) {
            event.setCancelled(true);
        }
    }

    private boolean isMovingItemsInWaypointInventory(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (
                // click waypoint invetory for placing an item
                (isClickedWaypointInventory(event.getClickedInventory()) && 
                    (event.getAction().equals(InventoryAction.PLACE_ALL) ||
                        event.getAction().equals(InventoryAction.PLACE_ONE)
                    )
                )
                ||
                // shift click
                event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)
            ) {
                return true;
            }
        }
        
        return false;
    }
    
    private void teleportPlayerToWaypoint(ItemStack currentItem) {       
        // Iteration on WaypointManager to find player, then on registeredWaypointsList to get the Location.
        List<RegisteredWaypoint> registeredWaypoints = WaypointManager.getByPlayerId(getPlayer().getUniqueId()).getRegisteredWaypointList();
        RegisteredWaypoint selectedRegisteredWaypoint = registeredWaypoints.stream()
        .filter(waypoint -> waypoint.getName().equals(currentItem.getItemMeta().getDisplayName()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Can't find location of the selected item"))
        ;    
        
        getPlayer().teleport(selectedRegisteredWaypoint.getLocation());
    }
    
    private ItemStack[] createWaypointsItems() {
        List<RegisteredWaypoint> registeredWaypointsList = WaypointManager.getByPlayerId(getPlayer().getUniqueId()).getRegisteredWaypointList();
        List<ItemStack> waypointsItemStacksList = new ArrayList<ItemStack>();
        
        for (RegisteredWaypoint registeredWaypoints : registeredWaypointsList) {
            List<String> coordinatesList = new ArrayList<String>();
            coordinatesList.add("X: " + registeredWaypoints.getLocation().getX());
            coordinatesList.add("Y: " + registeredWaypoints.getLocation().getY());
            coordinatesList.add("Z: " + registeredWaypoints.getLocation().getZ());
            
            ItemStack bookStack = new ItemStack(WAYPOINT_ITEM_MATERIAL, 1);
            ItemMeta bookMeta = bookStack.getItemMeta();
            bookMeta.setDisplayName(registeredWaypoints.getName());
            bookMeta.setLore(coordinatesList);
            
            bookStack.setItemMeta(bookMeta);
            
            waypointsItemStacksList.add(bookStack);
        }
        
        return waypointsItemStacksList.toArray(new ItemStack[waypointsItemStacksList.size()]);
    }

    private boolean isClickedWaypointInventory(Inventory clickedInventory) {
        return getInventory().equals(clickedInventory);
    }

    private boolean isClickedOnWaypointItem(Inventory clickedInventory, Material clickedItem) {
        return isClickedWaypointInventory(clickedInventory) && clickedItem.equals(WAYPOINT_ITEM_MATERIAL);
    }

    private void setItemsInventory(ItemStack[] waypointsItemStacksList) {
        getInventory().setStorageContents(waypointsItemStacksList);
    }
    
    private void openInventory() {
        getPlayer().openInventory(getInventory());
    }
    
    private Inventory getInventory() {
        return this.inventory;
    }
    
    private Player getPlayer() {
        return this.player;
    }
}
