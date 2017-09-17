package me.robomwm.mcware;

import me.robomwm.mcware.manager.EventManager;
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
    private Set<TypeTheColor> microgames = new HashSet<>(); //TODO: generic microgame interface

    public void onEnable()
    {
        eventManager = new EventManager(this, getServer().getWorld("mcware"));

        new TypeTheColor(this);
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
