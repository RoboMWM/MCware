package com.robomwm.mcware.round;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.hanging.HangingEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.weather.WeatherEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.plugin.EventExecutor;
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
public class EventManager
{
    private ScoreboardWare scoreboardWare;
    private Set<Listener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private World MCWARE_WORLD;
    private JavaPlugin plugin;

    public EventManager(World world, ScoreboardWare scoreboard, JavaPlugin plugin)
    {
        MCWARE_WORLD = world;
        scoreboardWare = scoreboard;
        this.plugin = plugin;
    }

    //Convenience method
    public boolean isPlayer(Player player)
    {
        return scoreboardWare.isPlayer(player);
    }

    //In case we want to expand on this - however, want to avoid teleporting across worlds - client takes time to load chunks and whatnot
    public boolean isMCwareWorld(World world)
    {
        return world == MCWARE_WORLD;
    }

    public void cleanup()
    {
        unregisterAllListeners();
        MCWARE_WORLD = null;
        scoreboardWare = null;
    }

    /**
     * Registers listeners which only fire for players/events that happen in the designated world.
     * Unregisters right before endMicrogame is called.
     * @param classOfListeners
     * @param plugin
     */
    public void registerListeners(JavaPlugin plugin, Listener... classOfListeners)
    {
//        for (Listener listener : classOfListeners)
//        {
//            listeners.add(listener);
//            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
//        }

        for (Listener listenerClass : listeners)
        {
            listeners.add(listenerClass);
            for (Method listener : listenerClass.getClass().getMethods())
            {
                if (!listener.isAnnotationPresent(EventHandler.class) //Has @EventHandler
                        || listener.getParameterCount() != 1 //Has only one input argument
                        || !listener.getParameterTypes()[0].isAssignableFrom(Event.class)) //is listening to an event
                    continue;

                listener.setAccessible(true); //In case it's private or w/e

                plugin.getServer().getPluginManager().registerEvent((Class<? extends Event>)listener.getParameterTypes()[0],
                        listenerClass, //uuuuuhhhhhhhhhhmmmmmmmmmmm
                        listener.getAnnotation(EventHandler.class).priority(),
                        new MCWareEventExecutor(), plugin, listener.getAnnotation(EventHandler.class).ignoreCancelled());
            }
        }
    }

    /**
     * @deprecated Should only be used by non-plugin-based addons (i.e. ones that don't extend JavaPlugin)
     * Since the mechanism for loading such doesn't exist right now, this does absolutely nothing.
     * So you shouldn't be using it!
     * @param classOfListeners
     */
    @Deprecated
    public void registerListeners(Listener... classOfListeners)
    {
        //registerListeners(plugin, classOfListeners);
    }


    public void unregisterAllListeners()
    {
        for (Listener listener : listeners)
            HandlerList.unregisterAll(listener);
        listeners.clear();
    }

    class MCWareEventExecutor implements EventExecutor
    {
        @Override
        public void execute(Listener listenerClass, Event event) throws EventException
        {
            if (event instanceof BlockEvent)
            {
                if (((BlockEvent)event).getBlock().getWorld() != MCWARE_WORLD)
                    return;
            }
            else if (event instanceof EntityEvent)
            {
                if (((EntityEvent)event).getEntity().getWorld() != MCWARE_WORLD)
                    return;
            }
            else if (event instanceof HangingEvent)
            {
                if (((HangingEvent)event).getEntity().getWorld() != MCWARE_WORLD)
                    return;
            }
            else if (event instanceof InventoryEvent)
            {
                if (((InventoryEvent)event).getInventory().getLocation() == null
                        || ((InventoryEvent)event).getInventory().getLocation().getWorld() != MCWARE_WORLD)
                    return;
            }
            else if (event instanceof InventoryMoveItemEvent)
            {
                if (((InventoryMoveItemEvent)event).getInitiator().getLocation().getWorld() != MCWARE_WORLD)
                    return;
            }
            else if (event instanceof InventoryPickupItemEvent)
            {
                if (((InventoryPickupItemEvent)event).getItem().getWorld() != MCWARE_WORLD)
                    return;
            }
            else if (event instanceof PlayerEvent)
            {
                if (!isPlayer(((PlayerEvent)event).getPlayer()))
                    return;
            }
            else if (event instanceof PlayerLeashEntityEvent)
            {
                if (!isPlayer(((PlayerLeashEntityEvent)event).getPlayer()))
                    return;
            }
            else if (event instanceof VehicleEvent)
            {
                if (((VehicleEvent)event).getVehicle().getWorld() != MCWARE_WORLD)
                    return;
            }
            else if (event instanceof WeatherEvent)
            {
                if (((WeatherEvent)event).getWorld() != MCWARE_WORLD)
                    return;
            }
            else if (event instanceof WorldEvent)
            {
                if (((WorldEvent) event).getWorld() != MCWARE_WORLD)
                    return;
            }
            for (Method listener : listenerClass.getClass().getMethods())
            {
                if (!listener.isAnnotationPresent(EventHandler.class) //Has @EventHandler
                        || !listener.getParameterTypes()[0].isAssignableFrom(event.getClass())) //event applies to this listener
                    continue;

                //Don't call if event is canceled, and listener doesn't care about canceled events
                if (event instanceof Cancellable && ((Cancellable)event).isCancelled() && listener.getAnnotation(EventHandler.class).ignoreCancelled())
                    continue;

                listener.setAccessible(true); //In case it's private or w/e

                try
                {
                    listener.invoke(listenerClass, event);
                }
                catch (Exception e)
                {
                    plugin.getLogger().severe(listenerClass.getClass().getName() + " Encountered an exception while attempting to handle " + event.getEventName());
                    e.printStackTrace();
                }
            }
        }
    }
}