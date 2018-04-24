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
public class TypeTheColor implements Microgame, Listener
{
    private JavaPlugin plugin;

    private Map<Player, Boolean> rightOrWrong;

    public TypeTheColor(MCware mCware)
    {
        plugin = mCware;
        mCware.registerMicrogame(this);
    }

    public boolean start(Set<Player> players, double speed)
    {
        //instantiate stuff
        rightOrWrong = new HashMap<>();

        //Instruction
        for (Player player : players)
            player.sendTitle("Type the color!", ChatColor.GREEN + "Blue", 0, 100, 0);

        //Register listener
        EventManager.registerListeners(plugin, this);
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

        //cleanup
        rightOrWrong.clear();
        rightOrWrong = null;

        return winners;
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent event)
    {
        if (!EventManager.isPlayer(event.getPlayer()))
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
