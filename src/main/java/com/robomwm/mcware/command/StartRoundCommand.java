package com.robomwm.mcware.command;

import com.robomwm.mcware.MCware;
import com.robomwm.mcware.round.MicrogameDispatcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created on 5/14/2018.
 *
 * @author RoboMWM
 */
public class StartRoundCommand implements CommandExecutor
{
    private MCware plugin;

    public StartRoundCommand(MCware plugin)
    {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.isOp()) //TODO: replace with permission
            return false;
        new MicrogameDispatcher(plugin, plugin.getServer().getWorld("mcware"), plugin.getMicrogames());
        return true;
    }
}
