package com.robomwm.mcware.microgames;

import com.robomwm.mcware.round.EventManager;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 5/21/2018.
 *
 * @author RoboMWM
 */
public class EliminateAPlayer extends Microgame implements Listener
{
    private JavaPlugin plugin;
    private Set<Player> winners;
    private EventManager eventManager;

    public EliminateAPlayer(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean start(Set<Player> players, double speed, EventManager eventManager)
    {
        if (players.size() < 2)
            return false;
        this.eventManager = eventManager;
        winners = new HashSet<>();
        players.iterator().next().getWorld().setPVP(true);
        eventManager.registerListeners(plugin, this);
        for (Player player : players)
            player.sendTitle("Eliminate a player!", "", 0, 40, 0);
        return true;
    }

    @Override
    public Collection<Player> end()
    {
        return winners;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    private void animation(PlayerAnimationEvent event)
    {
        event.setCancelled(event.getPlayer().getGameMode() == GameMode.SPECTATOR);
    }

    @EventHandler(ignoreCancelled = true)
    private void entityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if (event.getEntityType() != EntityType.PLAYER)
            return;
        if (!eventManager.isPlayer((Player)event.getEntity()))
            return;
        if (event.getDamager().getType() != EntityType.PLAYER)
            return;

        winners.add((Player)event.getDamager());
        Player player = (Player)event.getEntity();
        player.setGameMode(GameMode.SPECTATOR);
        player.setFlySpeed(0);
    }
}
