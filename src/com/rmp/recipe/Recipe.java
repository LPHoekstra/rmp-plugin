package com.rmp.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class Recipe {

    public Recipe() {
        new Spawner();
        // new InvisibleItemFrames();
    }

    protected NamespacedKey getKey(String key) {
        return new NamespacedKey(JavaPlugin.getProvidingPlugin(getClass()), key);
    }
}
