package me.robomwm.mcware.manager;

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
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 9/16/2017.
 *
 * Idk where this should go or what it should be named, the "PseudoEventManagerRegisterWhatever"??
 *
 * @author RoboMWM
 */
public class EventManager implements Listener
{
    private JavaPlugin instance;
    private World MCWARE_WORLD;

    private Set<Object> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public EventManager(JavaPlugin plugin, World world)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        instance = plugin;
        MCWARE_WORLD = world;
    }

    /**
     * Registers a class to listen to events applicable within the MCware world only
     * @param classOfListeners
     * @return
     */
    public void registerListeners(Object classOfListeners)
    {
        if (!(classOfListeners instanceof Listener))
            return;
        listeners.add(classOfListeners);
    }

    public boolean unregisterListeners(Object classOfListeners)
    {
        return listeners.remove(classOfListeners);
    }

    public void unregisterAllListeners()
    {
        listeners.clear();
    }

    private void callMCwareEventWhateverMajig(Event event)
    {
        for (EventPriority priority : EventPriority.values())
        {
            for (Object listenerClass : listeners)
            {
                for (Method listener : listenerClass.getClass().getMethods())
                {
                    if (!listener.isAnnotationPresent(EventHandler.class) //Has @EventHandler
                            || listener.getAnnotation(EventHandler.class).priority() != priority //currently calling this priority
                            || listener.getParameterCount() != 1 //Has only one input argument
                            || !listener.getParameterTypes()[0].isAssignableFrom(event.getClass())) //event applies to this listener
                        continue;

                    listener.setAccessible(true); //In case it's private or w/e

                    try
                    {
                        listener.invoke(listenerClass, event);
                    }
                    catch (Exception e)
                    {
                        instance.getLogger().severe(listenerClass.getClass().getName() + "Encountered an exception while attempting to handle " + event.getEventName());
                        e.printStackTrace();
                    }
                }
            }
        }
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
