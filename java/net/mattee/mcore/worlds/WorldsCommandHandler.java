package net.mattee.mcore.worlds;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class WorldsCommandHandler implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(command.getName().equals("mw")){
            if(args.length == 1){
                if(args[0].equals("list")){
                    String msg = "List of worlds:";
                    for(World w : Bukkit.getWorlds()){
                        msg += "\n   - " + w.getName();
                    }
                    commandSender.sendMessage(msg);
                }
            }
            else if(args.length == 2){
                if(args[0].equals("load")){
                    commandSender.sendMessage(args[1] + " nevű világ betőltése...");
                    Worlds.WORLDS.loadWorld(args[1]);
                    commandSender.sendMessage("Világ betöltve.");
                }
            }
            if(args.length >= 1){
                if(args[0].equals("create")){
                    if(args.length >= 2){
                        if(!Worlds.WORLDS.folderExists(args[1])){
                            if(args.length >= 3){
                                World.Environment type = null;
                                if(args[2].equals("normal")){
                                    type = World.Environment.NORMAL;
                                } else if(args[2].equals("end")){
                                    type = World.Environment.THE_END;
                                } else if(args[2].equals("nether")){
                                    type = World.Environment.NETHER;
                                }
                                if(args.length >= 4){
                                    if(args[3].equals("empty")){
                                        Worlds.WORLDS.createWorld(args[1], type, "empty");
                                    }
                                }
                                Worlds.WORLDS.createWorld(args[1], type);
                            }
                        }
                    }
                }
                if(args[0].equals("tp")){
                    if(args.length == 2){
                        if(Worlds.WORLDS.folderExists(args[1])){
                            if((Player) commandSender != null){
                                Player p = (Player) commandSender;
                                p.teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
