package me.Josan.MC_Machinery;

/**
 * Created by JosanM on 4/4/2015.
 *
 */

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class MC_MachineryPlugin extends JavaPlugin{

    protected ArrayList<Location> activeGrinders = new ArrayList<Location>();
    protected Hashtable<Material, Material> grindProducts = new Hashtable<Material, Material>();

    public void onDisable() {
        System.out.println(this.getDescription().getName() + " has been disabled!");
        //TODO save to file all locations
        saveLocations();
    }

    public void onEnable(){
        File file = new File(this.getDataFolder(),"config.yml");
        if(!file.exists()){
            saveDefaultConfig();
        }

        MC_MachineryBlockListener listener = new MC_MachineryBlockListener(this);
        getLogger().info(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " is enabled!");
        getServer().getPluginManager().registerEvent(BlockPlaceEvent.class, listener, EventPriority.NORMAL, listener, this);
        getServer().getPluginManager().registerEvent(BlockBreakEvent.class, listener, EventPriority.NORMAL, listener, this);

        loadConfig();
        //loadLocations();
    }

    private void saveLocations(){
        File savefile = new File(this.getDataFolder(), "locations.yml");
        try {
            FileWriter fw = new FileWriter(savefile);

            for(int i = 0 ; i < activeGrinders.size(); ++i) {
                Location block = activeGrinders.get(i);
                fw.append(block.getWorld().getName() + ", " +
                          block.getBlockX() + ", " +
                          block.getBlockY() + ", " +
                          block.getBlockZ() + "\n");
            }

            fw.close();
        } catch(IOException e){
            getLogger().severe("MC_MachineryPlugin: Could not open 'locations.yml' to write");
        }
    }

    private void loadLocations(){
        try {
            BufferedReader fw = new BufferedReader(new FileReader("/MC_Machinery/locations.yml"));
            String line = fw.readLine();
            while(line != null || line.compareTo("") != 0) {
                String[] coords = line.split(", ");

                Location block = getServer().getWorld(coords[0]).getBlockAt(
                        Integer.parseInt(coords[1]),
                        Integer.parseInt(coords[2]),
                        Integer.parseInt(coords[3])).getLocation();

                activeGrinders.add(block);

                line = fw.readLine();
            }
            System.out.println(activeGrinders.toString());
            //fw.close();
        } catch(IOException e){
            getLogger().severe("MC_MachineryPlugin: Could not read 'locations.yml' ");
        }
    }

    private void loadConfig(){
        FileConfiguration fc = getConfig();

        final String pattern = "GrinderPatterns.pattern";
        int i = 0;
        while(fc.contains(pattern + ++i)){
            grindProducts.put(Material.getMaterial((String)fc.get(pattern + i + ".from")),
                              Material.getMaterial((String)fc.get(pattern + i + ".to")));
        }
    }

    protected void grindBlock(Block block){
        //PistonBaseMaterial piston = (PistonBaseMaterial)block.getRelative(BlockFace.UP).getState().getData()
        block.setType(grindProducts.get(block.getType()));
        block.breakNaturally();
    }

}
