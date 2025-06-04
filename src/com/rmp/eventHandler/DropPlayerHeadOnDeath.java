package com.rmp.eventHandler;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class DropPlayerHeadOnDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        World world = event.getEntity().getWorld();

        ItemStack playerHeadStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerHeadMeta = (SkullMeta) playerHeadStack.getItemMeta();

        playerHeadMeta.setOwningPlayer(player);
        if (playerHeadMeta.hasOwner()) {
            playerHeadStack.setItemMeta(playerHeadMeta);

            world.dropItem(player.getLocation(), playerHeadStack);
        }
    }
}
