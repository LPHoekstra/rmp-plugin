package com.rmp.eventHandler;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * not init for now
 */
public class ChangeItemTextureOnRename implements Listener {
    // change item texture on rename
    @EventHandler
    public void onRenameItemAnvil(PrepareAnvilEvent event) {
        // get the first player in the anvil
        HumanEntity player = event.getViewers().get(0);

        // for loop on getViers list
        ItemStack newItem = event.getResult();

        if (newItem != null) {
            ItemMeta itemMeta = newItem.getItemMeta();

            // is itemMeta null, and check if it's an armor
            if (itemMeta != null && itemMeta instanceof ArmorMeta) {
                // if specific name, got specific model
                if (itemMeta.getDisplayName().equals("lotr")) {
                    player.sendMessage("set armure lotr");
                    itemMeta.setCustomModelData(1);

                    newItem.setItemMeta(itemMeta);
                    // reset default if not
                } else {
                    itemMeta.setCustomModelData(0);

                    newItem.setItemMeta(itemMeta);
                }
            }
        }
    }
}
