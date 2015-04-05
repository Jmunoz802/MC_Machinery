package me.Josan.MC_Machinery;

/**
 * Created by JosanM on 4/4/2015.
 *
 */

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Hashtable;

public class MC_MachineryPlugin extends JavaPlugin{

    protected ArrayList<Location> activeGrinders = new ArrayList<Location>();
    protected Hashtable<Material, Material> grindProducts = new Hashtable<Material, Material>();

    public void onDisable(){
        System.out.println(this.getDescription().getName() + " has been disabled!");
        //TODO save to file all locations
    }

    public void onEnable(){
        MC_MachineryBlockListener listener = new MC_MachineryBlockListener(this);
        getLogger().info(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " is enabled!");
        getServer().getPluginManager().registerEvent(BlockPlaceEvent.class, listener, EventPriority.NORMAL, listener, this);
        getServer().getPluginManager().registerEvent(BlockBreakEvent.class, listener, EventPriority.NORMAL, listener, this);
        //TODO move grinding relationships to config
        grindProducts.put(Material.COBBLESTONE, Material.GRAVEL);
        grindProducts.put(Material.GRAVEL, Material.SAND);
    }

    protected void grindBlock(Block block){
        //PistonBaseMaterial piston = (PistonBaseMaterial)block.getRelative(BlockFace.UP).getState().getData();
        block.setType(grindProducts.get(block.getType()));
        block.breakNaturally();
    }

}
