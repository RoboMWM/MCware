package me.robomwm.mcware;

import me.robomwm.mcware.manager.EventManager;
import me.robomwm.mcware.manager.RoundManager;
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
    private Set<TypeTheColor> microgames = new HashSet<>(); //TODO: generic microgame interface
    private Map<RoundTask, Long> scheduledTasks = new HashMap<>();

    public void onEnable()
    {
        eventManager = new EventManager(this, getServer().getWorld("mcware"));

        //load default microgames
        new TypeTheColor(this);

        //Start timer, cuz yea idk
        timer();
    }

    public void scheduleTask(RoundTask taskName, long delay)
    {
        scheduledTasks.put(taskName, System.currentTimeMillis() + delay);
    }

    //Could put in its own class, idk right now
    public void timer()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (RoundTask task : scheduledTasks.keySet())
                {
                    if (scheduledTasks.get(task) < System.currentTimeMillis())
                        continue;
                    scheduledTasks.remove(task);
                    switch(task)
                    {
                        case END_MICROGAME:
                            roundManager.endCurrentMicrogame();
                            break;
                    }
                }
            }
        }.runTaskTimer(this, 1L, 1L);
    }

    public void registerMicrogame(TypeTheColor game)
    {
        microgames.add(game);
    }

    public Set<TypeTheColor> getMicrogames()
    {
        return new HashSet<>(microgames);
    }
}
