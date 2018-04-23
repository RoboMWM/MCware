package com.robomwm.mcware.round;

import com.robomwm.mcware.microgames.Microgame;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 4/22/2018.
 *
 * Prepares, starts, and ends microgames
 *
 * So, I initially was going to use some elaborate timer that was based off of System#currentTime instead of ticks
 * to mitigate issues where the server is lagging. However, I'd have to also account for TPS catchup; where it
 * might end a game before all the "catchup" ticks have passed and thus miss anything that occurred during a lag "spike."
 * Having to account for this as well means waiting at least a specified number of ticks to pass - which means I
 * effectively negate any "lag mitigation" that I attempted to accomplish.
 *
 * So to make things easy and expected, I just use ticks for timing. If your server can't maintain ~20tps then there's
 * other issues going on tbh, and I'd assume that the majority of lag is often in spikes, rather than over time.
 *
 * I see anything wrong with using anonymous runnables? So I'm going to be doing that until it makes sense to create separate classes for each task..?
 *
 * @author RoboMWM
 */
public class MicrogameDispatcher
{
    private JavaPlugin plugin;
    private EventManager eventManager;
    private List<Microgame> microgames = new ArrayList<>();
    private Microgame currentMicrogame;
    private ScoreboardWare scoreboard;
    private int gamesPlayed = 0;
    private double speed = 1.0D;

    public MicrogameDispatcher(JavaPlugin plugin, World mcwareWorld, Collection<Microgame> microgames)
    {
        this.plugin = plugin;
        scoreboard = new ScoreboardWare(mcwareWorld);
        eventManager = new EventManager(plugin, mcwareWorld);
        this.microgames.addAll(microgames);
    }

    //responsible for determining speed up, boss round, etc.
    public void prepareForNextMicrogame()
    {
        if (++gamesPlayed % 4 == 0)
            speed += 0.25;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
               startNextMicrogame(speed);
            }
        }.runTaskLater(plugin, (long)(100 / speed));
    }

    public void startNextMicrogame(double speed)
    {
        currentMicrogame = microgames.get(ThreadLocalRandom.current().nextInt(microgames.size()));

        while (!currentMicrogame.start(scoreboard.getPlayers(), speed, eventManager))
        {
            currentMicrogame = microgames.get(ThreadLocalRandom.current().nextInt(microgames.size()));
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                endCurrentMicrogame();
            }
        }.runTaskLater(plugin, (long)(80 / speed));
    }

    public void endCurrentMicrogame()
    {
        eventManager.unregisterAllListeners();
        scoreboard.addPoints(1, currentMicrogame.end()); //TODO: change for boss

        //TODO: Stop music

        //TODO: teleport players to main platform

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                prepareForNextMicrogame();
            }
        }.runTaskLater(plugin, (long)(40 / speed));
    }
}