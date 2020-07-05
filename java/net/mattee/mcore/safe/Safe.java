package net.mattee.mcore.safe;

import net.mattee.mcore.main.mCore;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/*
WORK IN PROGRESS
This class will allow users to lock their chests.
These chests then can be opened by PIN code, password or pattern.
An inventory based UI will be used for these three mechanisms.
*/

public class Safe implements Listener {

    public static Safe SAFE;
    public static String FILEDIR = mCore.PLUGIN.getDataFolder() + File.separator + "users";
    public static String FILEPATH = FILEDIR + File.separator + "safe.data";
    private HashMap<UUID, Listener> safePlayers = new HashMap<UUID, Listener>();

    public Safe(){
        SAFE = this;
        initFile();
    }

    void initFile(){
        File fd = new File(FILEDIR);
        if(!fd.exists()) fd.mkdirs();
        File f = new File(FILEPATH);
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void setEnabled(boolean toggle){
        if(toggle){
            
        }
    }

}
