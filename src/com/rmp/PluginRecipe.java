package com.rmp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginRecipe {
    public PluginRecipe() {
        spawnerRecipe();
    }

    private void spawnerRecipe() {
        ItemStack spawnerStack = new ItemStack(Material.SPAWNER, 1);

        NamespacedKey key = getKey("spawner_recipe");

        ShapedRecipe recipe = new ShapedRecipe(key, spawnerStack);
        recipe.shape("AEA", "EAE", "AEA");

        recipe.setIngredient('A', Material.NETHERITE_INGOT);
        recipe.setIngredient('E', Material.NETHER_STAR);

        Bukkit.addRecipe(recipe);
    }

    private NamespacedKey getKey(String key) {
        return new NamespacedKey(JavaPlugin.getProvidingPlugin(getClass()), key);
    }
}
