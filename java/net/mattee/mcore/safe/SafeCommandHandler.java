package net.mattee.mcore.safe;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SafeCommandHandler implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(command.getName().equals("safe")){

        }
        return true;
    }
}
