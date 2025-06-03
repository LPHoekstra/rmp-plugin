package com.rmp;

import java.util.Collection;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.loot.LootContext;

public class EventListener implements Listener {
    // drop player head on death
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

    // drop egg on death of the entity
    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        LivingEntity dyingEntity = event.getEntity();

        // check if a player was the killer
        if (dyingEntity.getKiller() == null) {
            return;
        }

        final Player player = dyingEntity.getKiller();

        LootContext context = new LootContext.Builder(dyingEntity.getLocation())
                .killer(player)
                .lootedEntity(dyingEntity)
                .build();
        final Collection<ItemStack> extraItems = new EggLoot(dyingEntity.getType()).populateLoot(new Random(),
                context);

        event.getDrops().addAll(extraItems);
    }

    // change item texture on rename
    // @EventHandler
    // public void onRenameItemAnvil(PrepareAnvilEvent event) {
    // // get the first player in the anvil
    // HumanEntity player = event.getViewers().get(0);

    // // for loop on getViers list
    // ItemStack newItem = event.getResult();

    // if (newItem != null) {
    // ItemMeta itemMeta = newItem.getItemMeta();

    // // is itemMeta null, and check if it's an armor
    // if (itemMeta != null && itemMeta instanceof ArmorMeta) {
    // // if specific name, got specific model
    // if (itemMeta.getDisplayName().equals("lotr")) {
    // player.sendMessage("set armure lotr");
    // itemMeta.setCustomModelData(1);

    // newItem.setItemMeta(itemMeta);
    // // reset default if not
    // } else {
    // itemMeta.setCustomModelData(0);

    // newItem.setItemMeta(itemMeta);
    // }
    // }
    // }
    // }
}
