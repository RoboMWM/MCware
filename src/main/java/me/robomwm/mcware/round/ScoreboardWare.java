package me.robomwm.mcware.round;

import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created on 4/22/2018.
 *
 * Cuz Bukkit already has Scoreboard so w/e
 *
 * Tracks players playing in the round and their points
 *
 * @author RoboMWM
 */
public class ScoreboardWare implements Listener
{
    private Map<Player, Integer> points = new HashMap<>();
    private World MCWARE_WORLD;

    public ScoreboardWare(World mcwareWorld)
    {
        this.MCWARE_WORLD = mcwareWorld;
        for (Player player : mcwareWorld.getPlayers())
            points.put(player, 0);
    }

    public void addPoints(int amount, Collection<Player> winners)
    {
        for (Player player : points.keySet())
        {
            if (winners.contains(player))
            {
                points.put(player, points.get(player) + amount);
                player.sendTitle("You Won!", "", 0, 40, 0);
                player.playSound(player.getLocation(), "mcware.win", SoundCategory.BLOCKS, 3000000f, 1.0f);
            }
            else
            {
                player.sendTitle("You Failed...!", "", 0, 40, 0);
                player.playSound(player.getLocation(), "mcware.fail", SoundCategory.BLOCKS, 3000000f, 1.0f);
            }
        }
    }

    public Set<Player> getPlayers()
    {
        return Collections.unmodifiableSet(points.keySet());
    }

    //Leaves or joins game
    @EventHandler
    private void onPlayerJoin(PlayerChangedWorldEvent event)
    {
        if (event.getPlayer().getWorld() == MCWARE_WORLD)
            points.putIfAbsent(event.getPlayer(), 0);
        else if (event.getFrom() == MCWARE_WORLD)
            points.remove(event.getPlayer());
    }
}
