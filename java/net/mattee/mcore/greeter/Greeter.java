package net.mattee.mcore.greeter;

import com.destroystokyo.paper.Title;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Greeter implements Listener {

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent e){
        sendGreetMessage(e.getPlayer());
    }

    /*
    Sends a title to the player, and plays a little "bell" sound when the join event occurs.
    */
    void sendGreetMessage(Player p){
        BaseComponent[] message = new BaseComponent[3];
        message[0] = new TextComponent("Welcome, ");
        message[1] = new TextComponent(p.getDisplayName());
        message[2] = new TextComponent("!");
        message[1].setColor(ChatColor.RED);
        p.sendTitle(Title.builder().title(message).build());
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }

}
