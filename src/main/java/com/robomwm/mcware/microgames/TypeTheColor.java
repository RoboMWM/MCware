package com.robomwm.mcware.microgames;

import com.robomwm.mcware.MCware;
import com.robomwm.mcware.round.EventManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

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
    List<ChatColor> colors;
    private ChatColor color;
    private ChatColor word;
    private boolean useColor;

    public TypeTheColor(JavaPlugin plugin)
    {
        this.plugin = plugin;
        colors = new ArrayList<>();
        for (ChatColor color : ChatColor.values())
        {
            switch (color)
            {
                case GOLD:
                case GRAY:
                    continue;
            }
            if (color.isColor() && !color.name().contains("DARK"))
                colors.add(color);
        }
    }

    public boolean start(Set<Player> players, double speed, EventManager eventManager)
    {
        this.eventManager = eventManager;
        //instantiate stuff
        rightOrWrong = new HashMap<>();


        color = colors.get(ThreadLocalRandom.current().nextInt(colors.size()));
        word = colors.get(ThreadLocalRandom.current().nextInt(colors.size()));
        useColor = ThreadLocalRandom.current().nextBoolean();
        String instruction;

        if (useColor)
            instruction = "Type the color!";
        else
            instruction = "Type the word!";
        //Instruction
            for (Player player : players)
                player.sendTitle(instruction, color + word.name().toLowerCase(), 0, 100, 0);


        //Register listener
        eventManager.registerListeners(plugin, this);
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

        if (useColor && message.equalsIgnoreCase(color.name())
                || (!useColor && message.equalsIgnoreCase(word.name())))
        {
            rightOrWrong.put(event.getPlayer(), true);
            event.getPlayer().sendMessage("gud job");
            event.setCancelled(true);
        }
        else
            rightOrWrong.put(event.getPlayer(), false);
    }
}
