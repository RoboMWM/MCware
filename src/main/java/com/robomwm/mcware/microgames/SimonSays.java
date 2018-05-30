package com.robomwm.mcware.microgames;

import com.robomwm.mcware.round.EventManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 5/29/2018.
 *
 * @author RoboMWM
 */
public class SimonSays extends Microgame
{
    private EventManager eventManager;
    private SimonAction simonAction;
    private boolean simonSaidIt;
    private JavaPlugin plugin;
    private Map<Player, Boolean> performers; //I'm very bad at naming

    public SimonSays(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean start(Set<Player> players, double speed, EventManager eventManager)
    {
        simonAction = SimonAction.values()[ThreadLocalRandom.current().nextInt(SimonAction.values().length)];
        simonSaidIt = ThreadLocalRandom.current().nextBoolean();

        StringBuilder instruction = new StringBuilder();
        if (simonSaidIt)
            instruction.append("Simon says ");
        else
            instruction.append("Someone says ");

        this.eventManager = eventManager;

        switch(simonAction)
        {

        }

        return false;
    }

    @Override
    public Collection<Player> end()
    {
        return null;
    }

    @EventHandler(ignoreCancelled = true)
    private void onSneak(PlayerToggleSneakEvent event)
    {
        if (event.isSneaking())
            return;
        if (!eventManager.isPlayer(event.getPlayer()))
            return;
    }
}
enum SimonAction
{
    SNEAK,
    JUMP
}
