package net.mattee.mcore.login;

import net.mattee.mcore.main.mCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class LoginCommandHandler implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(command.getName().equals("register")){
            Player p;
            if((p = (Player) commandSender) != null){
                if(!Login.LOGIN.isPlayerRegistered(p)){
                    if(args.length == 2){
                        if(args[0].equals(args[1])){
                            Login.LOGIN.registerPlayer(p, args[0]);
                            p.sendMessage("Registration successful.");
                            p.sendMessage("Log in using /login <password>");
                        } else p.sendMessage("The two passwords do not match!");
                    } else p.sendMessage("Usage: /register <password> <password>");
                } else p.sendMessage("You are already registered!");
            } else commandSender.sendMessage("This command can be only used by a player!");
        } else if(command.getName().equals("login")){
            Player p;
            if((p = (Player) commandSender) != null){
                if(args.length == 1){
                    if(Login.LOGIN.login(p, args[0])){
                        p.sendMessage("Login successful.");
                    } else{
                        p.sendMessage("Wrong password!");
                    }
                } else p.sendMessage("Usage: /login <password>");
            } else commandSender.sendMessage("This command can be only used by a player!");
        }
        return true;
    }
}
