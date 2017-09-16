package me.robomwm.mcware;

import com.destroystokyo.paper.event.entity.EntityPathfindEvent;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.command.UnknownCommandEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.hanging.HangingEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created on 9/16/2017.
 *
 * Idk where this should go or what it should be named, the "PseudoEventManagerRegisterWhatever"??
 *
 * @author RoboMWM
 */
public class MCwareEventThingy implements Listener
{
    World MCWARE_WORLD;

    public MCwareEventThingy(JavaPlugin plugin, World world)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        MCWARE_WORLD = world;
    }

    private void callMCwareEventWhateverMajig(Event event)
    {
        
    }

    //In case we want to expand on this - however, want to avoid teleporting across worlds - client takes time to load chunks and whatnot
    private boolean isMCWareWorld(World world)
    {
        return world == MCWARE_WORLD;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onBlockEvent(BlockEvent event)
    {
        if (event.getBlock() == null)
            return;
        if (isMCWareWorld(event.getBlock().getWorld()))
            callMCwareEventWhateverMajig(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onEntityEvent(EntityEvent event)
    {
        if (event.getEntity() == null)
            return;
        if (isMCWareWorld(event.getEntity().getWorld()))
            callMCwareEventWhateverMajig(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onEntityPathfindEvent(EntityPathfindEvent event)
    {
        if (event.getEntity() == null)
            return;
        if (isMCWareWorld(event.getEntity().getWorld()))
            callMCwareEventWhateverMajig(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onHangingEvent(HangingEvent event)
    {
        //If NPE checks are needed cuz plugins fire fake events well um PR 'em or file an issue cuz am getting tired of typing 'em out
        if (isMCWareWorld(event.getEntity().getWorld()))
            callMCwareEventWhateverMajig(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onInventoryEvent(InventoryEvent event)
    {
        for (HumanEntity viewer : event.getViewers())
        {
            if (isMCWareWorld(viewer.getWorld()))
            {
                callMCwareEventWhateverMajig(event);
                return;
            }
        }
    }

    //You don't need InventoryMoveItemEvent or InventoryPickupitemEvent, right...?

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerEvent(PlayerEvent event)
    {
        if (isMCWareWorld(event.getPlayer().getWorld()))
            callMCwareEventWhateverMajig(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerLeashEntityEvent(PlayerLeashEntityEvent event)
    {
        if (isMCWareWorld(event.getPlayer().getWorld()))
            callMCwareEventWhateverMajig(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onVehicleEvent(VehicleEvent event)
    {
        if (isMCWareWorld(event.getVehicle().getWorld()))
            callMCwareEventWhateverMajig(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onWeatherEvent(WeatherChangeEvent event)
    {
        if (isMCWareWorld(event.getWorld()))
            callMCwareEventWhateverMajig(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onWorldEvent(WorldEvent event)
    {
        if (isMCWareWorld(event.getWorld()))
            callMCwareEventWhateverMajig(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onTabCompleteEvent(TabCompleteEvent event)
    {
        if (event.getSender() instanceof Player)
            if (isMCWareWorld(((Player)event.getSender()).getWorld()))
                callMCwareEventWhateverMajig(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onUnknownCommandEvent(UnknownCommandEvent event)
    {
        if (event.getSender() instanceof Player)
            if (isMCWareWorld(((Player)event.getSender()).getWorld()))
                callMCwareEventWhateverMajig(event);
    }
}
