package net.mattee.mcore.LED;

import net.mattee.mcore.main.mCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
This class is a "LED" interface.
It only works in a specific location.
It can display only numbers (because it is only used as a clock at the moment).
TODO: 
    Support multiple displays and use configuration files for coordinates.
    Support letters.
*/
public class LED {
    Location zero;
    String last = "";
    public LED(Location zero){
        this.zero = zero;
    }
    /*
    Writes a string to the display.
    */
    public void write(String text){
        char[] c = text.toCharArray();
        char[] lastc = last.toCharArray();
        if(last.equals("")){
            last = text;
            writeChar(zero, c[0], 0, new int[]{-1, 0, 0});
            writeChar(zero, c[1], 6, new int[]{-1, 0, 0});
            writeChar(zero, c[3], 14, new int[]{-1, 0, 0});
            writeChar(zero, c[4], 20, new int[]{-1, 0, 0});
        } else{
            if(lastc[0] != c[0]) writeChar(zero, c[0], 0, new int[]{-1, 0, 0});
            if(lastc[1] != c[1]) writeChar(zero, c[1], 6, new int[]{-1, 0, 0});
            if(lastc[3] != c[3]) writeChar(zero, c[3], 14, new int[]{-1, 0, 0});
            if(lastc[4] != c[4]) writeChar(zero, c[4], 20, new int[]{-1, 0, 0});
            last = text;
        }
    }
    /*
    Writes a signle character to the display.
    */
    void writeChar(Location zero, char c, int offset, int[] direction){
        for(int i = 0; i < 7; ++i){
            for(int k = 0; k < 5; ++k){
                Location loc = new Location(zero.getWorld(), (double) zero.getBlockX() + (offset * direction[0]) + (k * direction[0]), (double) zero.getBlockY() - i, (double) zero.getBlockZ() + (offset * direction[2]) + (k * direction[2]));
                zero.getWorld().getBlockAt(loc).setType(Material.AIR);
            }
        }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(mCore.PLUGIN, new Runnable() {
            @Override
            public void run() {
                String shape = "";
                switch(c){
                    case '0':
                        shape = chars.n0;
                        break;
                    case '1':
                        shape = chars.n1;
                        break;
                    case '2':
                        shape = chars.n2;
                        break;
                    case '3':
                        shape = chars.n3;
                        break;
                    case '4':
                        shape = chars.n4;
                        break;
                    case '5':
                        shape = chars.n5;
                        break;
                    case '6':
                        shape = chars.n6;
                        break;
                    case '7':
                        shape = chars.n7;
                        break;
                    case '8':
                        shape = chars.n8;
                        break;
                    case '9':
                        shape = chars.n9;
                        break;
                    default:
                        shape = chars.a;
                        break;
                }
                int vertOffset = 7 - (shape.toCharArray().length / 5);
                int charVertOffset = 0;
                int charHorOffset = 0;
                for(char ch : shape.toCharArray()){
                    if(ch == '#'){
                        Location loc = new Location(zero.getWorld(), (double) zero.getBlockX() + (offset * direction[0]) + (charHorOffset * direction[0]), (double) zero.getBlockY() - vertOffset - charVertOffset, (double) zero.getBlockZ() + (offset * direction[2]) + (charHorOffset * direction[2]));
                        zero.getWorld().getBlockAt(loc).setType(Material.REDSTONE_BLOCK);
                    }
                    ++charHorOffset;
                    if(charHorOffset == 5){
                        charHorOffset = 0;
                        ++charVertOffset;
                    }
                }
            }
        }, 10L);
    }
}

final class chars{
    public static String n0 =
            " ### " +
            "#   #" +
            "#  ##" +
            "# # #" +
            "##  #" +
            "#   #" +
            " ### ";
    public static String n1 =
            "  #  " +
            " ##  " +
            "  #  " +
            "  #  " +
            "  #  " +
            "  #  " +
            "#####";
    public static String n2 =
            " ### " +
            "#   #" +
            "    #" +
            "  ## " +
            " #   " +
            "#   #" +
            "#####";
    public static String n3 =
            " ### " +
            "#   #" +
            "    #" +
            "  ## " +
            "    #" +
            "#   #" +
            " ### ";
    public static String n4 =
            "   ##" +
            "  # #" +
            " #  #" +
            "#   #" +
            "#####" +
            "    #" +
            "    #";
    public static String n5 =
            "#####" +
            "#    " +
            "#### " +
            "    #" +
            "    #" +
            "#   #" +
            " ### ";
    public static String n6 =
            "  ## " +
            " #   " +
            "#    " +
            "#### " +
            "#   #" +
            "#   #" +
            " ### ";
    public static String n7 =
            "#####" +
            "#   #" +
            "    #" +
            "   # " +
            "  #  " +
            "  #  " +
            "  #  ";
    public static String n8 =
            " ### " +
            "#   #" +
            "#   #" +
            " ### " +
            "#   #" +
            "#   #" +
            " ### ";
    public static String n9 =
            " ### " +
            "#   #" +
            "#   #" +
            " ####" +
            "    #" +
            "   # " +
            " ##  ";
    public static String a =
            " ### " +
            "    #" +
            " ####" +
            "#   #" +
            " ####";
    public static String b =
            "#    " +
            "#    " +
            "# ## " +
            "##  #" +
            "#   #" +
            "#   #" +
            "#### ";
    public static String c =
            " ### " +
            "#   #" +
            "#    " +
            "#   #" +
            " ###";
    public static String d =
            "    #" +
            "    #" +
            " ## #" +
            "#  ##" +
            "#   #" +
            "#   #" +
            " ####";
    public static String e =
            " ### " +
            "#   #" +
            "#####" +
            "#    " +
            " ####";
    public static String f =
            "  ## " +
            " #   " +
            "#### " +
            " #   " +
            " #   " +
            " #   " +
            " #   ";
    public static String g =
            " ####" +
            "#   #" +
            "#   #" +
            " ####" +
            "    #" +
            "#### ";
    public static String colon =
            " " +
            " " +
            "#" +
            " " +
            "#" +
            " " +
            " ";
}