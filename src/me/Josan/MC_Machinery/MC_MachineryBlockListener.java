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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.PistonBaseMaterial;
import org.bukkit.plugin.EventExecutor;

/**
 * Created by JosanM on 4/4/2015.
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
            if(relativePiston.getType() == Material.PISTON_BASE){
                PistonBaseMaterial pistonMat = new PistonBaseMaterial(relativePiston.getType());
                //System.out.println("Hopper sees piston_base 2blocks above!");
                if(pistonMat.getFacing() == BlockFace.DOWN) {
                    System.out.println("New Grinder at: " + blockplaced.getRelative(BlockFace.UP).getLocation());
                    instance.activeGrinders.add(blockplaced.getRelative(BlockFace.UP).getLocation());
                }
            }
        }
        //Adding new active grinder to plugin, top -> bottom
        if(blockMaterial == Material.PISTON_BASE){
            //System.out.println(blockMaterial.getData().toString());
            /*PistonBaseMaterial pistonMat = blockMaterial.getData().;//new PistonBaseMaterial(blockMaterial);
            System.out.println("pistonMat: " + pistonMat.getFacing().toString());
            if(pistonMat.getFacing() == BlockFace.DOWN && blockplaced.getRelative(BlockFace.DOWN, 2).getType() == Material.HOPPER) {
                //System.out.println("Piston sees hopper 2blocks above!");
                System.out.println("New Grinder at: " + blockplaced.getRelative(BlockFace.DOWN).getLocation());
                instance.activeGrinders.add(blockplaced.getRelative(BlockFace.DOWN).getLocation());
            }*/
        }

        //Checking location to be on top of active grinder
        if(blockMaterial == Material.GRAVEL || blockMaterial == Material.COBBLESTONE){
            if(instance.activeGrinders.contains(event.getBlockPlaced().getLocation())){
                //doSomething
                System.out.println("THERE'S GOOD BLOCK IN ACTIVE GRINDER!");
            }
        }
    }

    @EventHandler
    public void onBlockBreak( BlockBreakEvent event ){
        //Removing grinder on hopper break;
        if(event.getBlock().getType() == Material.HOPPER){
            Location grinderCheck = event.getBlock().getRelative(BlockFace.UP).getLocation();
            if(instance.activeGrinders.contains(grinderCheck)){
                instance.activeGrinders.remove(grinderCheck);
            }
        }

        if(event.getBlock().getType() == Material.PISTON_BASE){
            Location grinderCheck = event.getBlock().getRelative(BlockFace.DOWN).getLocation();
            if(instance.activeGrinders.contains(grinderCheck)){
                instance.activeGrinders.remove(grinderCheck);
            }
        }

    }

}
