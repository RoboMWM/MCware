package com.robomwm.mcware;

import com.robomwm.mcware.microgames.Microgame;
import com.robomwm.mcware.microgames.TypeTheColor;
import com.robomwm.mcware.round.EventManager;
import com.robomwm.mcware.round.MicrogameDispatcher;
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
    private Set<Microgame> microgames = new HashSet<>();

    public void onEnable()
    {
        //load bundled microgames
        registerMicrogame(new TypeTheColor(this));

        new MicrogameDispatcher(this, getServer().getWorld("mcware"), microgames);
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
