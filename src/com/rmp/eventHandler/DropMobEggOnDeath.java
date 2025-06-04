package com.rmp.eventHandler;

import java.util.Collection;
import java.util.Random;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;

import com.rmp.lootTable.EggLoot;

public class DropMobEggOnDeath implements Listener {

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
}