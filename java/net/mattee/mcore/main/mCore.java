package net.mattee.mcore.main;

import net.mattee.mcore.Files.Files;
import net.mattee.mcore.LED.LED;
import net.mattee.mcore.greeter.Greeter;
import net.mattee.mcore.login.Login;
import net.mattee.mcore.login.LoginCommandHandler;
import net.mattee.mcore.time.Time;
import net.mattee.mcore.worlds.Worlds;
import net.mattee.mcore.worlds.WorldsCommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class mCore extends JavaPlugin implements Listener {

    /**
     * The global Plugin static used by other classes.
     */
    public static mCore PLUGIN;

    @Override
    public void onEnable(){
        PLUGIN = this;
        Bukkit.getLogger().log(Level.INFO, "Started mCore v2.\nWelcome!");
        registerEvents();
        registerCommands();
        initClasses();
        Worlds.WORLDS.createWorld("lobby", World.Environment.NORMAL, "empty");
        if(Bukkit.getWorld("lobby") != null){
            LED led = new LED(new Location(Bukkit.getWorld("lobby"), 12f, 99f, 65f));
            Time t = new Time(led);
        }
    }

    void initClasses(){
        new Worlds();
        new Files();
    }

    /**
     * Registers classes that listen to events.
     * Some classes may ony listen to events temporarily, thus registered inside the respective class.
     */
    void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new Greeter(), this);
        Bukkit.getPluginManager().registerEvents(new Login(), this);
    }

    /**
     * Registering commands provided by this plugin.
     */
    void registerCommands(){
        LoginCommandHandler lch = new LoginCommandHandler();
        WorldsCommandHandler wch = new WorldsCommandHandler();
        getCommand("register").setExecutor(lch);
        getCommand("login").setExecutor(lch);
        getCommand("mw").setExecutor(wch);
    }

}
