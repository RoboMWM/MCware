package com.robomwm.mcware.microgames;

import com.robomwm.mcware.MCware;
import com.robomwm.mcware.round.EventManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Example/default microgames
 *
 * Created on 9/16/2017.
 *
 * @author RoboMWM
 */
public class TypeTheColor extends Microgame implements Listener
{
    private JavaPlugin plugin;
    private Map<Player, Boolean> rightOrWrong;
    private EventManager eventManager;

    public TypeTheColor(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }

    public boolean start(Set<Player> players, double speed, EventManager eventManager)
    {
        this.eventManager = eventManager;
        //instantiate stuff
        rightOrWrong = new HashMap<>();

        //Instruction
        for (Player player : players)
            player.sendTitle("Type the color!", ChatColor.GREEN + "Blue", 0, 100, 0);

        //Register listener
        eventManager.registerListeners(this);
        return true;
    }

    public Set<Player> end()
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

        String message = event.getMessage().toUpperCase();

        if (message.equals(ChatColor.GREEN.name()))
        {
            rightOrWrong.put(event.getPlayer(), true);
            event.setCancelled(true);
        }
        else
            rightOrWrong.put(event.getPlayer(), false);
    }
}
