package me.robomwm.mcware.manager;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created on 9/15/2017.
 *
 * Are classes in the wrong packages/package names? Let me know!
 *
 * This manages the round - it:
 * - starts games
 * - keeps score
 * - sets speed
 * - determines winner at end of round
 * etc.
 *
 * Basically manages the entire minigame system more or less...
 *
 * Why an interface?
 * For special rounds (different ruleset), different themes, etc.
 *
 * @author RoboMWM
 */
public class RoundManager implements Listener
{
    public RoundManager(JavaPlugin plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
