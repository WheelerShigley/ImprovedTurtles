package me.solacekairos.improvedturtles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Reload implements CommandExecutor {
    ImprovedTurtles occurance;
    Drops reloadable;

    public Reload(ImprovedTurtles plugin) {
        this.occurance = plugin;
        this.reloadable = plugin.Dropper;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 1 || 2 < args.length) { sender.sendMessage("§cInsufficient arguments, try \"/turtles reload\"."); return true; } //false

        //reload if argument is "reload" or "r"
        if( args[0].equals("reload") || args[0].equals("r") ) {
            occurance.reloadConfig();
            //reload drops
            {
                boolean did_drop = reloadable.change_drops;
                int previous_maximum = reloadable.roll_maximum;

                reloadable.change_drops = occurance.getConfig().getBoolean("turtles_drop_scute");
                reloadable.roll_maximum = occurance.getConfig().getInt("scute_roll_maximum");

                if ( (!did_drop && reloadable.change_drops) || (previous_maximum != reloadable.roll_maximum) ) { occurance.improved_turtles_logger.info("Turtles now drop " + reloadable.roll_maximum + " scute per roll."); }
                if (did_drop && !reloadable.change_drops) { occurance.improved_turtles_logger.info("Turtles drop seagrass again (Vanilla)."); }
            }
            if(sender instanceof Player) { sender.sendMessage("Reloaded turtles!"); }
            occurance.improved_turtles_logger.info("Reloaded config.");
            return true;
        }
        return true; //false
    }
}
