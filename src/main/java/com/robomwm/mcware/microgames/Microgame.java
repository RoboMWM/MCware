package com.robomwm.mcware.microgames;

import com.robomwm.mcware.round.EventManager;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Set;

/**
 *
 * Created on 9/24/2017.
 *
 * @author RoboMWM
 */
public abstract class Microgame implements Cloneable
{
    /**
     *
     * @param players
     * @param speed
     * @return Whether this microgame can be started (i.e. return false if not enough players, etc.)
     */
    public abstract boolean start(Set<Player> players, double speed, EventManager eventManager);

    /**
     *
     * @return players who won/should be awarded points
     */
    public abstract Collection<Player> end();

    public Microgame clone()
    {
        try
        {
            return (Microgame)super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new AssertionError();
        }
    }
}
