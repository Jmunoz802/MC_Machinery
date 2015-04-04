package me.Josan.MC_Machinery;

/**
 * Created by JosanM on 4/4/2015.
 */

import org.apache.logging.log4j.core.net.Priority;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class MC_MachineryPlugin extends JavaPlugin{

    protected ArrayList<Location> activeGrinders = new ArrayList<Location>();

    public void onDisable(){
        System.out.println(this.getDescription().getName() + " has been disabled!");
        //save to file all locations
    }

    public void onEnable(){
        MC_MachineryBlockListener listener = new MC_MachineryBlockListener(this);
        getLogger().info(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " is enabled!");
        getServer().getPluginManager().registerEvent(BlockPlaceEvent.class, listener, EventPriority.NORMAL, listener, this);
        getServer().getPluginManager().registerEvent(BlockBreakEvent.class, listener, EventPriority.NORMAL, listener, this);
    }
}
