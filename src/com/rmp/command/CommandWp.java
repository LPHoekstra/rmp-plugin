package com.rmp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rmp.model.PlayerWaypoint;
import com.rmp.model.RegisteredWaypoint;
import com.rmp.waypoint.WaypointManager;

public class CommandWp implements CommandExecutor {
    private String msgToSend = "";
    private PlayerWaypoint playerWaypoint = null;
    private String[] argument = null;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            playerWaypoint = WaypointManager.getByPlayerId(player.getUniqueId());
            argument = args;

            if (isArgumentInCommand()) {
                teleportToWaypoint(player);
            }
            // otherwise show the available waypoint
            else {
                if (playerWaypoint.getRegisteredWaypointList().isEmpty()) {
                    msgToSend = "Aucun waypoint disponible";
                } 
                else {
                    getAvailableWaypoint();
                }
            }

            sender.sendMessage(msgToSend);
            return true;
        }

        sender.sendMessage("Invalid command sender, must be a player");
        return false;
    }

    private boolean isArgumentInCommand() {
        return argument.length >= 1 && !playerWaypoint.getRegisteredWaypointList().isEmpty();
    }

    private void teleportToWaypoint(Player player) {
        RegisteredWaypoint waypointToTeleport = playerWaypoint.getRegisteredWaypointList().stream()
            .filter(waypoint -> waypoint.getName().equals(argument[0]))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Can't teleport because " + argument[0] + " is not valid"))
        ;
        
        player.teleport(waypointToTeleport.getLocation());
        msgToSend = "Tp au waypoint: " + waypointToTeleport.getName();
    }

    private void getAvailableWaypoint() {
        msgToSend = "Waypoint disponible: ";

        for (RegisteredWaypoint registeredWaypoint : playerWaypoint.getRegisteredWaypointList()) {
            msgToSend = msgToSend + registeredWaypoint.getName() + ". ";
        }
    }
}
