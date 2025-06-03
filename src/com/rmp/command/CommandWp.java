package com.rmp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rmp.signWaypoint.ListWaypoint;
import com.rmp.signWaypoint.RegisteredWaypoint;

public class CommandWp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            // TODO: can be optimized

            // if there is argument
            if (args.length >= 1 && !ListWaypoint.getList().isEmpty()) {
                for (RegisteredWaypoint registeredWaypoint : ListWaypoint.getList()) {
                    if (args[0].equals(registeredWaypoint.getName())) {
                        // TODO: teleport the player to the waypoint
                        sender.sendMessage("tp to the waypoint");
                    }
                }
            }
            // otherwise show the available waypoint
            else {
                String msgToSend;

                // show the available waypoint
                if (!ListWaypoint.getList().isEmpty()) {
                    msgToSend = "Waypoint disponible: ";

                    for (RegisteredWaypoint registeredWaypoint : ListWaypoint.getList()) {
                        msgToSend = msgToSend + registeredWaypoint.getName() + ".";
                    }
                }
                // otherwise no waypoint available
                else {
                    msgToSend = "Aucun waypoint disponible";
                }

                sender.sendMessage(msgToSend);
            }

            return true;
        }

        sender.sendMessage("Invalid command sender, must be a player");
        return false;
    }
}
