package com.robomwm.mcware.round;

import com.destroystokyo.paper.event.entity.EntityPathfindEvent;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
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
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.RegisteredListener;
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

    private Set<Listener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());

    //In case we want to expand on this - however, want to avoid teleporting across worlds - client takes time to load chunks and whatnot
    public boolean isMCWareWorld(World world)
    {
        return world == MCWARE_WORLD;
    }

    public EventManager(JavaPlugin plugin, World world)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        instance = plugin;
        MCWARE_WORLD = world;
    }

//    /**
//     * Registers a class to listen to events <s>applicable within the MCware world only</s>
//     * @param classOfListeners
//     * @return
//     */
//    public void registerListeners(Object classOfListeners)
//    {
//        if (!(classOfListeners instanceof Listener))
//            return;
//        listeners.add(classOfListeners);
//    }

    /**
     * Note: You <b>must</b> check if player is contained in the player's set that's passed to you on microgame start
     * or if player's world == isMCWareWorld.
     * Because life is short and I don't have time to figure out how to listen to generic bukkit events without typing out each and every event.
     * (Getting all handlers won't work unless someone is listening to every event, and I'll figure out reflection later... or a PR!)
     * @param classOfListeners
     * @param plugin
     */
    public void registerListeners(JavaPlugin plugin, Listener... classOfListeners)
    {
        for (Listener listener : classOfListeners)
        {
            listeners.add(listener);
            instance.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public void unregisterAllListeners()
    {
        for (Listener listener : listeners)
            HandlerList.unregisterAll(listener);
        listeners.clear();
    }

//    private void callMCwareEventWhateverMajig(Event event)
//    {
//        for (EventPriority priority : EventPriority.values())
//        {
//            for (Object listenerClass : listeners)
//            {
//                for (Method listener : listenerClass.getClass().getMethods())
//                {
//                    if (!listener.isAnnotationPresent(EventHandler.class) //Has @EventHandler
//                            || listener.getAnnotation(EventHandler.class).priority() != priority //currently calling this priority
//                            || listener.getParameterCount() != 1 //Has only one input argument
//                            || !listener.getParameterTypes()[0].isAssignableFrom(event.getClass())) //event applies to this listener
//                        continue;
//
//                    //Don't call if event is canceled, and listener doesn't care about canceled events
//                    if (event instanceof Cancellable && ((Cancellable)event).isCancelled() && listener.getAnnotation(EventHandler.class).ignoreCancelled())
//                        continue;
//
//                    listener.setAccessible(true); //In case it's private or w/e
//
//                    try
//                    {
//                        listener.invoke(listenerClass, event);
//                    }
//                    catch (Exception e)
//                    {
//                        instance.getLogger().severe(listenerClass.getClass().getName() + "Encountered an exception while attempting to handle " + event.getEventName());
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }


    //This. Does. Not. Work. Cuz. HandlerLists.

//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onBlockEvent(BlockEvent event)
//    {
//        if (event.getBlock() == null)
//            return;
//        if (isMCWareWorld(event.getBlock().getWorld()))
//            callMCwareEventWhateverMajig(event);
//    }
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onEntityEvent(EntityEvent event)
//    {
//        if (event.getEntity() == null)
//            return;
//        if (isMCWareWorld(event.getEntity().getWorld()))
//            callMCwareEventWhateverMajig(event);
//    }
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onEntityPathfindEvent(EntityPathfindEvent event)
//    {
//        if (event.getEntity() == null)
//            return;
//        if (isMCWareWorld(event.getEntity().getWorld()))
//            callMCwareEventWhateverMajig(event);
//    }
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onHangingEvent(HangingEvent event)
//    {
//        //If NPE checks are needed cuz plugins fire fake events well um PR 'em or file an issue cuz am getting tired of typing 'em out
//        if (isMCWareWorld(event.getEntity().getWorld()))
//            callMCwareEventWhateverMajig(event);
//    }
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onInventoryEvent(InventoryEvent event)
//    {
//        for (HumanEntity viewer : event.getViewers())
//        {
//            if (isMCWareWorld(viewer.getWorld()))
//            {
//                callMCwareEventWhateverMajig(event);
//                return;
//            }
//        }
//    }
//
//    //You don't need InventoryMoveItemEvent or InventoryPickupitemEvent, right...?
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onPlayerEvent(PlayerEvent event)
//    {
//        if (isMCWareWorld(event.getPlayer().getWorld()))
//            callMCwareEventWhateverMajig(event);
//    }
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onPlayerLeashEntityEvent(PlayerLeashEntityEvent event)
//    {
//        if (isMCWareWorld(event.getPlayer().getWorld()))
//            callMCwareEventWhateverMajig(event);
//    }
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onVehicleEvent(VehicleEvent event)
//    {
//        if (isMCWareWorld(event.getVehicle().getWorld()))
//            callMCwareEventWhateverMajig(event);
//    }
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onWeatherEvent(WeatherChangeEvent event)
//    {
//        if (isMCWareWorld(event.getWorld()))
//            callMCwareEventWhateverMajig(event);
//    }
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onWorldEvent(WorldEvent event)
//    {
//        if (isMCWareWorld(event.getWorld()))
//            callMCwareEventWhateverMajig(event);
//    }
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onTabCompleteEvent(TabCompleteEvent event)
//    {
//        if (event.getSender() instanceof Player)
//            if (isMCWareWorld(((Player)event.getSender()).getWorld()))
//                callMCwareEventWhateverMajig(event);
//    }
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onUnknownCommandEvent(UnknownCommandEvent event)
//    {
//        if (event.getSender() instanceof Player)
//            if (isMCWareWorld(((Player)event.getSender()).getWorld()))
//                callMCwareEventWhateverMajig(event);
//    }
}
