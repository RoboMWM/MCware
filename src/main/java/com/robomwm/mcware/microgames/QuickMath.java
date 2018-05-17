package com.robomwm.mcware.microgames;

import com.robomwm.mcware.round.EventManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 5/16/2018.
 *
 * @author RoboMWM
 */
public class QuickMath extends Microgame implements Listener
{
    private JavaPlugin plugin;
    private int result;
    private boolean add;
    private EventManager eventManager;
    private Map<Player, Boolean> rightOrWrong;

    public QuickMath(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean start(Set<Player> players, double speed, EventManager eventManager)
    {
        rightOrWrong = new HashMap<>();
        this.eventManager = eventManager;
        eventManager.registerListeners(plugin, this);

        int first = ThreadLocalRandom.current().nextInt(30);
        int second = ThreadLocalRandom.current().nextInt(20);
        add = ThreadLocalRandom.current().nextBoolean();
        if (add)
            result = first + second;
        else
            result = first - second;

        String operator = "-";
        if (add)
            operator = "+";


        for (Player player : players)
            player.sendTitle("Quick Maths", Integer.toString(first) + " " + operator + " " + second + " = ?", 0, 100, 0);

        return true;
    }

    @Override
    public Collection<Player> end()
    {
        Set<Player> winners = new HashSet<>();
        for (Player player : rightOrWrong.keySet())
        {
            if (!rightOrWrong.get(player))
                continue;
            winners.add(player);
        }

        return winners;
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent event)
    {
        if (!eventManager.isPlayer(event.getPlayer()))
            return;

        //Already attempted a guess
        if (rightOrWrong.containsKey(event.getPlayer()))
            return;

        try
        {
            if (Integer.parseInt(event.getMessage()) == result)
            {
                rightOrWrong.put(event.getPlayer(), true);
                event.getPlayer().sendMessage("wow quik maffs");
                event.setCancelled(true);
                return;
            }
        }
        catch (Throwable ignored){}

        rightOrWrong.put(event.getPlayer(), false);
    }
}
