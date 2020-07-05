package net.mattee.mcore.time;

import net.mattee.mcore.LED.LED;
import net.mattee.mcore.main.mCore;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Timer;
import java.util.TimerTask;

/*
This class updates the clock when a minute passes.
*/

public class Time {

    LED clock;

    public Time(LED display){
        this.clock = display;
        main();
    }

    void main(){
        mCore.PLUGIN.getServer().getScheduler().scheduleSyncRepeatingTask(mCore.PLUGIN, new Runnable() {
            @Override
            public void run() {
                Clock c = Clock.system(ZoneId.of("Europe/Budapest"));
                String text = c.instant().atOffset(ZoneOffset.ofHours(2)).toString().split("T")[1];
                String[] arr = text.split(":");
                text = arr[0] + ":" + arr[1];
                clock.write(text);
            }
        }, 0L, 20L);
    }
}
