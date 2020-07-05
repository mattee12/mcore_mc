package net.mattee.mcore.worlds;

import net.mattee.mcore.Files.Files;
import net.mattee.mcore.login.Login;
import net.mattee.mcore.main.mCore;
import org.bukkit.*;
import org.bukkit.generator.ChunkGenerator;

import java.io.*;
import java.util.Random;

public class Worlds {

    public static Worlds WORLDS;
    public static String DIR = mCore.PLUGIN.getDataFolder() + File.separator + "worlds";
    public static String WORLDSFILE = DIR + File.separator + "worlds.yml";

    public Worlds(){
        WORLDS = this;
        initFile();
        createWorld("skypvp", World.Environment.NORMAL, "empty");
    }

    void initFile(){
        File dir = new File(DIR);
        if(!dir.exists()) dir.mkdirs();
        File f = new File(WORLDSFILE);
        if(!f.exists()) {
            Files.FILES.copyResource("worlds.yml", f);
        }
    }

    boolean loadWorld(String name){
        return false;
    }

    boolean folderExists(String name){
        File f = new File(Bukkit.getWorldContainer() + File.separator + name + File.separator + "uid.dat");
        if(f.exists()) return true;
        return false;
    }

    boolean createWorld(String name, World.Environment type){
        //if(folderExists(name)) return false;
        WorldCreator wc = new WorldCreator(name);
        wc.environment(type);
        Bukkit.createWorld(wc);
        return false;
    }

    public boolean createWorld(String name, World.Environment type, String generator){
       // if(folderExists(name)) return false;
        WorldCreator wc = new WorldCreator(name);
        wc.environment(type);
        if(generator.equals("empty")){
            wc.generator(new EmptyChunkGenerator());
        }
        Bukkit.createWorld(wc);
        return false;
    }

}

class EmptyChunkGenerator extends ChunkGenerator{
    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        return createChunkData(world);
    }
    @Override
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        final Location spawnLocation = new Location(world, 0.0D, 64.0D, 0.0D);
        final Location blockLocation = spawnLocation.clone().subtract(0D, 1D, 0D);
        blockLocation.getBlock().setType(Material.BEDROCK);
        return spawnLocation;
    }
}
