package com.rmp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rmp.signWaypoint.PlayerWaypoints;
import com.rmp.signWaypoint.WaypointManager;
import com.rmp.signWaypoint.RegisteredWaypoint;

public class CommandWp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            // TODO: can be optimized
            String msgToSend = "";

            PlayerWaypoints playerWaypoints = WaypointManager.getByPlayerId(player.getUniqueId());

            // if there is argument
            if (args.length >= 1 && !playerWaypoints.getList().isEmpty()) {
                for (RegisteredWaypoint registeredWaypoint : playerWaypoints.getList()) {
                    String waypointName = registeredWaypoint.getName();

                    if (args[0].equals(waypointName)) {
                        player.teleport(registeredWaypoint.getLocation());
                        msgToSend = "Tp au waypoint: " + waypointName;
                    }
                }
            }
            // otherwise show the available waypoint
            else {

                // show the available waypoint
                if (!playerWaypoints.getList().isEmpty()) {
                    msgToSend = "Waypoint disponible: ";

                    for (RegisteredWaypoint registeredWaypoint : playerWaypoints.getList()) {
                        msgToSend = msgToSend + registeredWaypoint.getName() + ". ";
                    }
                }
                // otherwise no waypoint available
                else {
                    msgToSend = "Aucun waypoint disponible";
                }
            }

            sender.sendMessage(msgToSend);

            return true;
        }

        sender.sendMessage("Invalid command sender, must be a player");
        return false;
    }
}
