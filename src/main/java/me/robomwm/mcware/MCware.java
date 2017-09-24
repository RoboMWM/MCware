package me.robomwm.mcware;

import me.robomwm.mcware.manager.EventManager;
import me.robomwm.mcware.manager.RoundManager;
import me.robomwm.mcware.microgames.Microgame;
import me.robomwm.mcware.microgames.TypeTheColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created on 9/15/2017.
 *
 * And so it begins... The long journey to making microgames great again
 *
 * @author RoboMWM
 */
public class MCware extends JavaPlugin
{
    private EventManager eventManager;
    private RoundManager roundManager;
    private Set<Microgame> microgames = new HashSet<>(); //TODO: generic microgame interface
    private Map<RoundTask, Long> scheduledTasks = new HashMap<>();

    public EventManager getEventManager()
    {
        return eventManager;
    }

    public void onEnable()
    {
        //Start timer, cuz yea idk
        timer();

        eventManager = new EventManager(this, getServer().getWorld("mcware"));

        //load default microgames
        new TypeTheColor(this);

        roundManager = new RoundManager(this);

    }

    public void scheduleTask(RoundTask taskName, long delay)
    {
        scheduledTasks.put(taskName, System.currentTimeMillis() + delay);
    }

    //Could put in its own class, idk right now
    private void timer()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (RoundTask task : scheduledTasks.keySet())
                {
                    if (scheduledTasks.get(task) > System.currentTimeMillis())
                        continue;
                    scheduledTasks.remove(task);
                    switch(task)
                    {
                        case END_MICROGAME:
                            roundManager.endCurrentMicrogame();
                            break;
                        case START_MICROGAME:
                            roundManager.startNextMicrogame();
                            break;
                    }
                }
            }
        }.runTaskTimer(this, 1L, 1L);
    }

    public void registerMicrogame(Microgame game)
    {
        microgames.add(game);
    }

    public Set<Microgame> getMicrogames()
    {
        return new HashSet<>(microgames);
    }
}
