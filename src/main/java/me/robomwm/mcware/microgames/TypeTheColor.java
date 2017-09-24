package me.robomwm.mcware.microgames;

import me.robomwm.mcware.MCware;
import me.robomwm.mcware.manager.EventManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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
public class TypeTheColor implements Listener
{
    private MCware instance;
    private Map<String, Double> settings = new HashMap<>();
    private Map<Player, Boolean> rightOrWrong = new HashMap<>();

    public TypeTheColor(MCware mCware)
    {
        mCware.registerMicrogame(this);
        settings.put("seconds", 4D);
    }

    public Map<String, Double> getSettings()
    {
        return settings;
    }

    public void onGameStart(Set<Player> players, double speed)
    {
        //Instruction
        for (Player player : players)
            player.sendTitle("Type the color!", ChatColor.GREEN + "Blue", 0, 100, 0);
        //Register listener
        instance.getEventManager().registerListeners(this);
    }

    public Set<Player> onGameEnd()
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

        return winners;
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent event)
    {
        //Already attempted a guess
        if (rightOrWrong.containsKey(event.getPlayer()))
            return;

        String message = event.getMessage().toLowerCase();

        if (message.equals(ChatColor.BLUE.name()))
        {
            rightOrWrong.put(event.getPlayer(), true);
            event.setCancelled(true);
        }
        else
            rightOrWrong.put(event.getPlayer(), false);
    }
}
