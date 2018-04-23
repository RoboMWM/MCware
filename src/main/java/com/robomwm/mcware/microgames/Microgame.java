package com.robomwm.mcware.microgames;

import com.robomwm.mcware.round.EventManager;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * Created on 9/24/2017.
 *
 * @author RoboMWM
 */
public interface Microgame
{
    Map<String, Double> getSettings();

    boolean start(Set<Player> players, double speed, EventManager eventManager);

    /**
     *
     * @return players who won/should be awarded points
     */
    Collection<Player> end();
}
