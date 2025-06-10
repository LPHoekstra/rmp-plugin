package com.rmp.waypoint;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.rmp.RmpPlugin;
import com.rmp.model.RegisteredWaypoint;

public class WaypointInventory implements Listener {
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
        if (isWaypointInventory(event.getClickedInventory())) {
            teleportPlayerToWaypoint(event.getCurrentItem());
            return;
        }

        // when a player move an item to waypoint inventory
        if (isMovingItemsInInventory(event)) {
            event.setCancelled(true);
        }
    }

    // TODO improve it by allowing player to move item in is own inventory
    private boolean isMovingItemsInInventory(InventoryClickEvent event) {
        return event.getInventory().equals(getInventory()) &&
            event.getClickedInventory().equals(getPlayer().getInventory());
    }
    
    private void teleportPlayerToWaypoint(ItemStack currentItem) {       
        if (currentItem != null) {
            // Iteration on WaypointManager to find player, then on registeredWaypointsList to get the Location.
            List<RegisteredWaypoint> registeredWaypoints = WaypointManager.getByPlayerId(getPlayer().getUniqueId()).getList();
            RegisteredWaypoint selectedRegisteredWaypoint = registeredWaypoints.stream()
            .filter(waypoint -> waypoint.getName().equals(currentItem.getItemMeta().getDisplayName()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Can't find location of the selected item"))
            ;    
            
            getPlayer().teleport(selectedRegisteredWaypoint.getLocation());
        }
    }
    
    private ItemStack[] createWaypointsItems() {
        List<RegisteredWaypoint> registeredWaypointsList = WaypointManager.getByPlayerId(getPlayer().getUniqueId()).getList();
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

    private boolean isWaypointInventory(Inventory clickInventory) {
        return getInventory().equals(clickInventory);
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
