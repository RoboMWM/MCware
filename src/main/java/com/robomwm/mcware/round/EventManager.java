package com.robomwm.mcware.round;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
     * Note: You <b>must</b> check result of isPlayer() or isMCwareWorld() in each listener.
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
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    /**
     * @deprecated Should only be used by non-plugin-based addons (i.e. ones that don't extend JavaPlugin)
     * @param classOfListeners
     */
    @Deprecated
    public void registerListeners(Listener... classOfListeners)
    {
        registerListeners(plugin, classOfListeners);
    }


    public void unregisterAllListeners()
    {
        for (Listener listener : listeners)
            HandlerList.unregisterAll(listener);
        listeners.clear();
    }
}
