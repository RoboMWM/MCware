package me.robomwm.mcware.manager;

import me.robomwm.mcware.MCware;
import me.robomwm.mcware.RoundTask;
import me.robomwm.mcware.microgames.TypeTheColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created on 9/15/2017.
 *
 * Are classes in the wrong packages/package names? Let me know!
 *
 * This manages the round - it:
 * - starts games
 * - keeps score
 * - sets speed
 * - determines winner at end of round
 * etc.
 *
 * Basically manages the entire minigame system more or less...
 *
 * Why an interface?
 * For special rounds (different ruleset), different themes, etc.
 *
 * @author RoboMWM
 */
public class RoundManager implements Listener
{
    MCware instance;
    TypeTheColor currentMicrogame;
    Map<Player, Integer> points = new HashMap<>();
    int level = 0;

    public RoundManager(MCware plugin)
    {
        instance = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        prepareForNextMicrogame(); //start round
    }

    public void endCurrentMicrogame()
    {
        instance.getEventManager().unregisterAllListeners();
        Set<Player> winners = currentMicrogame.onGameEnd();
        for (Player player : points.keySet())
        {
            if (winners.contains(player))
            {
                points.put(player, points.get(player) + 1);
                player.sendTitle("You Won!", "", 0, 40, 0);
                //play victory music
            }
            else
            {
                //play failure music
                player.sendTitle("You Failed...!", "", 0, 40, 0);
            }
        }

        //TODO: Stop music

        //TODO: teleport players to main platform

        prepareForNextMicrogame();
    }

    //responsible for determining speed up, boss round, etc.
    public void prepareForNextMicrogame()
    {
        //TODO: actually do the above...

        instance.scheduleTask(RoundTask.START_MICROGAME, 5000L);
    }

    public void startNextMicrogame()
    {
        //TODO: determine next microgame
        currentMicrogame = (TypeTheColor)instance.getMicrogames().toArray()[0];

        instance.scheduleTask(RoundTask.END_MICROGAME, 4000L);
        currentMicrogame.onGameStart(new HashSet<>(points.keySet()), 1);
    }

    //Listeners

    //Leaves game
    @EventHandler
    public void onPlayerLeaving(PlayerChangedWorldEvent event)
    {
        if (instance.getEventManager().isMCWareWorld(event.getFrom()))
            points.remove(event.getPlayer());
    }

    //Joins game
    @EventHandler
    public void onPlayerJoin(PlayerChangedWorldEvent event)
    {
        if (instance.getEventManager().isMCWareWorld(event.getPlayer().getWorld()))
            points.put(event.getPlayer(), 0);
    }
}
