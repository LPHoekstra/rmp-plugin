package com.rmp.sign;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.rmp.RmpPlugin;

public abstract class PlayerModifyingSign implements Listener {
    protected Sign sign;

    public PlayerModifyingSign(Sign sign) {
        this.sign = sign;
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(RmpPlugin.class));
    }

    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event) {
        String newName = event.getLine(1);

        handleEvent(event, newName);

        // to have a new value of currentSignSide
        SignChangeEvent.getHandlerList().unregister(this);
    }

    protected abstract void handleEvent(SignChangeEvent event, String newName);
}
