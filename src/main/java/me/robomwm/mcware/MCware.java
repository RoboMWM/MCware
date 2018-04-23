package me.robomwm.mcware;

import me.robomwm.mcware.round.EventManager;
import me.robomwm.mcware.microgames.Microgame;
import me.robomwm.mcware.microgames.TypeTheColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
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
