package com.rmp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.java.JavaPlugin;

public class EggLoot implements LootTable {
    private final double EGG_DROP_CHANCE = .005; // default chance 0.5%
    private EntityType entityType;

    public EggLoot(EntityType entityType) {
        this.entityType = entityType;
    }

    @Override
    public Collection<ItemStack> populateLoot(Random random, LootContext lootContext) {
        final List<ItemStack> items = new ArrayList<>();

        if (random.nextDouble() <= EGG_DROP_CHANCE) {
            // We get the next double from the Random instance passed to the method.
            // If you only want on roll on all of the items you make a variable out of the
            // random.nextDouble() to keep the double consistent throughout the method.
            ItemFactory itemFactory = Bukkit.getServer().getItemFactory();
            Material entityEgg = itemFactory.getSpawnEgg(entityType);
            items.add(new ItemStack(entityEgg, 1));
        }

        return items;
    }

    @Override
    public void fillInventory(Inventory inventory, Random random, LootContext lootContext) {
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JavaPlugin.getProvidingPlugin(getClass()), "extra_egg_drops");
        // Register our LootTable to the server so the server knows what LootTable we
        // called.
    }
}
