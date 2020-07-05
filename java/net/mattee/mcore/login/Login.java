package net.mattee.mcore.login;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import net.mattee.mcore.main.mCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * This class is responsible for the login system.
 * The player gets freezed after joining, until they log in.
 */
public class Login implements Listener {

    /**
     * The path to the login data file (only directories).
     * Used for creating the required folders.
     */
    public static String FILEPATH;
    /**
     * The path to the login data file (including the filename).
     */
    public static String FILEDIR;
    /**
     * The global Login static.
     * Mainly used by the LoginCommandHandler class.
     */
    public static Login LOGIN;
    /**
     * A list for storing players that are logged in (using UUID).
     */
    List<UUID> loggedInPlayers = new ArrayList<UUID>();
    /**
     * The freeze function needs to listen to many events per user.
     * This hashmap contains the listener objects (per user, identified by UUID).
     * Used for unregistering the mentioned event listeners.
     */
    HashMap<UUID, Listener> waitingList = new HashMap<UUID, Listener>();

    public Login(){
        FILEDIR = mCore.PLUGIN.getDataFolder() + File.separator + "users";
        FILEPATH = FILEDIR + File.separator + "login.data";
        LOGIN = this;
        initFile();
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent e){
        final Player p = e.getPlayer();
        //Make sure that the player is not stuck in the freeze state from their last session,
        //because that could prevent the plugin to unfreeze them.
        unfreezePlayer(p);
        freezePlayer(p);
        if(!isPlayerRegistered(p)){
            p.sendMessage("Please register using /register <password> <password>");
        } else p.sendMessage("Log in using /login <password>");
    }

    //The player is removed from both lists to start their next session clean.
    @EventHandler
    void onPlayerDisconnect(PlayerQuitEvent e){
        loggedInPlayers.remove(e.getPlayer().getUniqueId());
        unfreezePlayer(e.getPlayer());
        waitingList.remove(e.getPlayer().getUniqueId());
    }

    /**
     * Removes the blindness effects and unregisters the event listeners.
     * @param p The player to unfreeze.
     */
    void unfreezePlayer(Player p){
        p.removePotionEffect(PotionEffectType.BLINDNESS);
        if(waitingList.containsKey(p.getUniqueId())){
            Listener listener = waitingList.get(p.getUniqueId());
            waitingList.remove(p.getUniqueId());
            PlayerMoveEvent.getHandlerList().unregister(listener);
            PlayerInteractEvent.getHandlerList().unregister(listener);
            EntityDamageEvent.getHandlerList().unregister(listener);
            PlayerAttemptPickupItemEvent.getHandlerList().unregister(listener);
            PlayerDropItemEvent.getHandlerList().unregister(listener);
            PlayerItemConsumeEvent.getHandlerList().unregister(listener);
            AsyncPlayerChatEvent.getHandlerList().unregister(listener);
            PlayerCommandPreprocessEvent.getHandlerList().unregister(listener);
            EntityDismountEvent.getHandlerList().unregister(listener);
            FoodLevelChangeEvent.getHandlerList().unregister(listener);
            InventoryClickEvent.getHandlerList().unregister(listener);
        }
    }

    /**
     * Called by the login command. It matches the given password with the one in the login data file,
     * and performs the login process,
     * @param p The player to log in.
     * @param password The password given by the player.
     * @return Return true if the login is successful.
     */
    boolean login(Player p, String password){
        File f = new File(FILEPATH);
        try {
            BufferedReader fr = new BufferedReader(new FileReader(f));
            String line;
            while((line = fr.readLine()) != null){
                String[] data = line.split(":");
                if(data[1].equals(p.getUniqueId().toString())){
                    if(data[2].equals(password)){
                        loggedInPlayers.add(p.getUniqueId());
                        unfreezePlayer(p);
                        fr.close();
                        return true;
                    }
                }
            }
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Called by the register command. If the player is not found in the login data file, it appends
     * the player's name, UUID and password to it in a new line.
     * @param p The player to register.
     * @param password The password chosen by the player.
     * @return Return true if successful.
     */
    //
    boolean registerPlayer(Player p, String password){
        String name = p.getName();
        String uuid = p.getUniqueId().toString();
        if(!isPlayerRegistered(p)){
            File f = new File(FILEPATH);
            try {
                BufferedWriter fw = new BufferedWriter(new FileWriter(f, true));
                String entry = name + ":" + uuid + ":" + password;
                fw.write(entry);
                fw.newLine();
                fw.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //If the file or its path doesn't exist, this function creates them.
    void initFile() {
        File path = new File(FILEDIR);
        File f = new File(FILEPATH);
        if(!path.exists()) path.mkdirs();
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Looks for the player in the login data file.
     * @param p
     * @return
     */
    boolean isPlayerRegistered(Player p) {
        File f = new File(FILEPATH);
        try {
            BufferedReader fr = new BufferedReader(new FileReader(f));
            String line;
            while((line = fr.readLine()) != null){
                if(line.split(":")[1].equals(p.getUniqueId().toString())) {
                    fr.close();
                    return true;
                }
            }
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param p The player to check.
     * @return Returns true if the given player is logged in.
     */
    boolean isPlayerLoggedIn(Player p){
        if(loggedInPlayers.contains(p.getUniqueId())) return true;
        return false;
    }

    /**
     * Freezes the player (almost any kind of control is refused), by listening
     * to events called by the player's actions and cancelling it.
     * @param p The player to freeze.
     */
    void freezePlayer(final Player p){
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 2));
        Listener uniqueListener = new Listener() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        };
        waitingList.put(p.getUniqueId(), uniqueListener);
        //Moving.
        Bukkit.getPluginManager().registerEvent(PlayerMoveEvent.class, uniqueListener, EventPriority.HIGHEST, new EventExecutor(){
            public void execute(Listener listener, Event event) throws EventException {
                PlayerMoveEvent e = (PlayerMoveEvent) event;
                if(p.getUniqueId().equals(e.getPlayer().getUniqueId())) e.setCancelled(true);
            }
        }, mCore.PLUGIN);
        //Breaking/placing blocks, etc.
        Bukkit.getPluginManager().registerEvent(PlayerInteractEvent.class, uniqueListener, EventPriority.HIGHEST, new EventExecutor(){
            public void execute(Listener listener, Event event) throws EventException {
                PlayerInteractEvent e = (PlayerInteractEvent) event;
                if(p.getUniqueId().equals(e.getPlayer().getUniqueId())) e.setCancelled(true);
            }
        }, mCore.PLUGIN);
        //Taking damage.
        Bukkit.getPluginManager().registerEvent(EntityDamageEvent.class, uniqueListener, EventPriority.HIGHEST, new EventExecutor(){
            public void execute(Listener listener, Event event) throws EventException {
                EntityDamageEvent e = (EntityDamageEvent) event;
                if(e.getEntityType().equals(EntityType.PLAYER)){
                    Player player = (Player) e.getEntity();
                    if(p.getUniqueId().equals(player.getUniqueId())) e.setCancelled(true);
                }
            }
        }, mCore.PLUGIN);
        //Picking up items.
        Bukkit.getPluginManager().registerEvent(PlayerAttemptPickupItemEvent.class, uniqueListener, EventPriority.HIGHEST, new EventExecutor(){
            public void execute(Listener listener, Event event) throws EventException {
                PlayerAttemptPickupItemEvent e = (PlayerAttemptPickupItemEvent) event;
                if(p.getUniqueId().equals(e.getPlayer().getUniqueId())) e.setCancelled(true);
            }
        }, mCore.PLUGIN);
        //Dropping items.
        Bukkit.getPluginManager().registerEvent(PlayerDropItemEvent.class, uniqueListener, EventPriority.HIGHEST, new EventExecutor(){
            public void execute(Listener listener, Event event) throws EventException {
                PlayerDropItemEvent e = (PlayerDropItemEvent) event;
                if(p.getUniqueId().equals(e.getPlayer().getUniqueId())) e.setCancelled(true);
            }
        }, mCore.PLUGIN);
        //Consuming items.
        Bukkit.getPluginManager().registerEvent(PlayerItemConsumeEvent.class, uniqueListener, EventPriority.HIGHEST, new EventExecutor(){
            public void execute(Listener listener, Event event) throws EventException {
                PlayerItemConsumeEvent e = (PlayerItemConsumeEvent) event;
                if(p.getUniqueId().equals(e.getPlayer().getUniqueId())) e.setCancelled(true);
            }
        }, mCore.PLUGIN);
        //Sending messages to the chat.
        Bukkit.getPluginManager().registerEvent(AsyncPlayerChatEvent.class, uniqueListener, EventPriority.HIGHEST, new EventExecutor(){
            public void execute(Listener listener, Event event) throws EventException {
                AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
                if(p.getUniqueId().equals(e.getPlayer().getUniqueId())) e.setCancelled(true);
            }
        }, mCore.PLUGIN);
        //Running commands,
        Bukkit.getPluginManager().registerEvent(PlayerCommandPreprocessEvent.class, uniqueListener, EventPriority.HIGHEST, new EventExecutor() {
            public void execute(Listener listener, Event event) throws EventException {
                PlayerCommandPreprocessEvent e = (PlayerCommandPreprocessEvent) event;
                String command = e.getMessage().split(" ")[0].substring(1);
                if(!(command.equals("login") || command.equals("register"))){
                    if(p.getUniqueId().equals(e.getPlayer().getUniqueId())) e.setCancelled(true);
                }
            }
        }, mCore.PLUGIN);
        //Dismount entities.
        Bukkit.getPluginManager().registerEvent(EntityDismountEvent.class, uniqueListener, EventPriority.HIGHEST, new EventExecutor(){
            public void execute(Listener listener, Event event) throws EventException {
                EntityDismountEvent e = (EntityDismountEvent) event;
                if(e.getEntity().getType().equals(EntityType.PLAYER)){
                    Player player = (Player) e.getEntity();
                    if(p.getUniqueId().equals(player.getUniqueId())) e.setCancelled(true);
                }
            }
        }, mCore.PLUGIN);
        //Hunger.
        Bukkit.getPluginManager().registerEvent(FoodLevelChangeEvent.class, uniqueListener, EventPriority.HIGHEST, new EventExecutor() {
            public void execute(Listener listener, Event event) throws EventException {
                FoodLevelChangeEvent e = (FoodLevelChangeEvent) event;
                if(e.getEntityType().equals(EntityType.PLAYER)){
                    Player player = (Player) e.getEntity();
                    if(p.getUniqueId().equals(player.getUniqueId())) e.setCancelled(true);
                }
            }
        }, mCore.PLUGIN);
        //Inventory actions.
        Bukkit.getPluginManager().registerEvent(InventoryClickEvent.class, uniqueListener, EventPriority.HIGHEST, new EventExecutor(){
            public void execute(Listener listener, Event event) throws EventException {
                InventoryClickEvent e = (InventoryClickEvent) event;
                if(p.getUniqueId().equals(e.getWhoClicked().getUniqueId())) e.setCancelled(true);
            }
        }, mCore.PLUGIN);
    }
}
