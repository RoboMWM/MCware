package com.robomwm.mcware.round;

import org.bukkit.ChatColor;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;

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
public class ScoreboardWare
{
    private Map<Player, Integer> points = new HashMap<>();
    private World MCWARE_WORLD;

    public ScoreboardWare(World mcwareWorld)
    {
        this.MCWARE_WORLD = mcwareWorld;
        for (Player player : mcwareWorld.getPlayers())
            points.put(player, 0);
    }

    public void printWinner()
    {
        //I briefly thought of creating a class instead of using a hashmap
        //that implements comparable so they can be in a sorted list,
        //but I only need to determine the winner at the end so yea.
        Player winner = points.keySet().iterator().next();
        for (Player player : points.keySet())
            if (points.get(player) > points.get(winner))
                winner = player;
        //TODO: multi-winners support
        for (Player player : MCWARE_WORLD.getPlayers())
            player.sendMessage(player.getName() + " won with " + points.get(player));
    }

    public void addPoints(int amount, Collection<Player> winners)
    {
        for (Player player : points.keySet())
        {
            player.stopSound("");
            if (winners.contains(player))
            {
                points.put(player, points.get(player) + amount);
                player.sendTitle(ChatColor.DARK_GREEN + "You Won!", "", 0, 40, 0);
                player.playSound(player.getLocation(), "mcware.win", SoundCategory.BLOCKS, 3000000f, 1.0f);
            }
            else
            {
                player.sendTitle(ChatColor.DARK_RED + "You Failed...!", "", 0, 40, 0);
                player.playSound(player.getLocation(), "mcware.fail", SoundCategory.BLOCKS, 3000000f, 1.0f);
            }
        }
    }

    public void updatePlayers()
    {
        //Remove offline/players who left
        for (Player player : points.keySet())
        {
            isPlayer(player);
        }

        //Add players who joined
        for (Player player : MCWARE_WORLD.getPlayers())
        {
            points.putIfAbsent(player, 0);
        }
    }

    public Set<Player> getPlayers()
    {
        for (Player player : points.keySet())
            isPlayer(player);
        return Collections.unmodifiableSet(points.keySet());
    }

    public boolean isPlayer(Player player)
    {
        //Check if online or in same world, remove if not so
        if (!player.isOnline() || !player.getServer().getOnlinePlayers().contains(player)
                || player.getWorld() != MCWARE_WORLD)
            points.remove(player);

        return points.containsKey(player);
    }
}
