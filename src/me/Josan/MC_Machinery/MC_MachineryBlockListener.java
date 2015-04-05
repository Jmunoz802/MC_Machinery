package me.Josan.MC_Machinery;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.PistonBaseMaterial;
import org.bukkit.plugin.EventExecutor;

/**
 * Created by JosanM on 4/4/2015.
 * MC_MachineryBlockListener handles all event calls for BlockPlaceEvent and BlockBreakEvent
 * to detect when a grinder has been created or destroyed. Additionally checks for valid
 * materials used within MC_Machinery plugin
 */
public class MC_MachineryBlockListener implements Listener, EventExecutor {

    protected MC_MachineryPlugin instance;

    public MC_MachineryBlockListener(MC_MachineryPlugin plugin){
       instance = plugin;
    }

    public void execute(Listener listener, Event event){
        if(listener instanceof MC_MachineryBlockListener && event instanceof BlockPlaceEvent){
            ((MC_MachineryBlockListener) listener).onBlockPlace((BlockPlaceEvent)event);
        }
        if(listener instanceof MC_MachineryBlockListener && event instanceof BlockBreakEvent){
            ((MC_MachineryBlockListener) listener).onBlockBreak((BlockBreakEvent)event);
        }
    }

    @EventHandler
    public void onBlockPlace( BlockPlaceEvent event ){
        Block blockplaced = event.getBlockPlaced();
        Material blockMaterial = event.getBlockPlaced().getType();
        //Adding new active grinder to plugin, bottom -> up
        if(blockMaterial == Material.HOPPER){
            Block relativePiston = blockplaced.getRelative(BlockFace.UP, 2);
            if(relativePiston.getType() == Material.PISTON_BASE && relativePiston.getState().getData() instanceof  PistonBaseMaterial){
                PistonBaseMaterial pistonMat = (PistonBaseMaterial)relativePiston.getState().getData();
                if(pistonMat.getFacing() == BlockFace.DOWN) {
                    instance.activeGrinders.add(blockplaced.getRelative(BlockFace.UP).getLocation());
                }
            }
        }
        //Adding new active grinder to plugin, top -> bottom
        if(blockMaterial == Material.PISTON_BASE && blockplaced.getState().getData() instanceof PistonBaseMaterial){
            PistonBaseMaterial pistonData = (PistonBaseMaterial) blockplaced.getState().getData();
            if(pistonData.getFacing() == BlockFace.DOWN && blockplaced.getRelative(BlockFace.DOWN, 2).getType() == Material.HOPPER){
                instance.activeGrinders.add(blockplaced.getRelative(BlockFace.DOWN).getLocation());
            }
        }

        //Checking location to be on top of active grinder
        if(blockMaterial == Material.GRAVEL || blockMaterial == Material.COBBLESTONE){
            if(instance.activeGrinders.contains(event.getBlockPlaced().getLocation())){
                //Pass block to main plugin to 'grind' the block
                instance.grindBlock(blockplaced);
            }
        }
    }

    @EventHandler
    public void onBlockBreak( BlockBreakEvent event ){
        //Removing grinder on hopper break
        if(event.getBlock().getType() == Material.HOPPER){
            Location grinderCheck = event.getBlock().getRelative(BlockFace.UP).getLocation();
            if(instance.activeGrinders.contains(grinderCheck)){
                instance.activeGrinders.remove(grinderCheck);
            }
        }
        //Removing grinder on Piston break
        if(event.getBlock().getType() == Material.PISTON_BASE){
            Location grinderCheck = event.getBlock().getRelative(BlockFace.DOWN).getLocation();
            if(instance.activeGrinders.contains(grinderCheck)){
                instance.activeGrinders.remove(grinderCheck);
            }
        }
    }
}
