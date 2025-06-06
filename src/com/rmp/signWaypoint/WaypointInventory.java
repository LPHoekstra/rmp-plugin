package com.rmp.signWaypoint;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.rmp.RmpPlugin;

public class WaypointInventory implements Listener {
    private Inventory inventory;
    
    public WaypointInventory(Player player) {
        this.inventory = Bukkit.createInventory(player, 27, "Waypoints");
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(RmpPlugin.class));
        
        setItemsInventory(createWaypointsItems(player));

        openInventory(player);
    }

    // TODO player can't place an item in this inventory
    @EventHandler
    private void onClickWaypointItem(InventoryClickEvent event) {
        if (inventory.equals(event.getInventory())) {
            ItemStack waypointItemStack = event.getCurrentItem();
            HumanEntity player = event.getWhoClicked();

            if (waypointItemStack != null) {
                // Iteration on WaypointManager to find player, then on registeredWaypointsList to get the Location.
                List<RegisteredWaypoint> registeredWaypoints = WaypointManager.getByPlayerId(player.getUniqueId()).getList();
                RegisteredWaypoint selectedRegisteredWaypoint = registeredWaypoints.stream()
                    .filter(waypoint -> waypoint.getName().equals(waypointItemStack.getItemMeta().getDisplayName()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Can't find location of the selected item"))
                ;    

                player.teleport(selectedRegisteredWaypoint.getLocation());
            }
        }
    }
    
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
