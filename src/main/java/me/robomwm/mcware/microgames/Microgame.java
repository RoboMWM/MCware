package me.robomwm.mcware.microgames;

import org.bukkit.entity.Player;

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

    void onGameStart(Set<Player> players, double speed);

    Set<Player> onGameEnd();
}
