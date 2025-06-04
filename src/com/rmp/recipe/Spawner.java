package com.rmp.recipe;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Spawner extends Recipe {
    public Spawner() {
        ItemStack spawnerStack = new ItemStack(Material.SPAWNER, 1);

        NamespacedKey key = getKey("spawner_recipe");

        ShapedRecipe recipe = new ShapedRecipe(key, spawnerStack);
        recipe.shape("AEA", "EAE", "AEA");

        recipe.setIngredient('A', Material.NETHERITE_INGOT);
        recipe.setIngredient('E', Material.NETHER_STAR);

        Bukkit.addRecipe(recipe);
    }
}
