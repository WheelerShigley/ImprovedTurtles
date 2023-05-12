package me.solacekairos.improvedturtles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Reload implements CommandExecutor {
    ImprovedTurtles occurance;
    Drops reloadable_drops; Helmets reloadable_upgrades; ReturnToDrop reloadable_molting;

    public Reload(ImprovedTurtles plugin) {
        this.occurance = plugin;
        this.reloadable_drops = plugin.Dropper;
        this.reloadable_upgrades = plugin.Upgrades;
        this.reloadable_molting = plugin.MoltAtHome;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 1 || 2 < args.length) { sender.sendMessage("§cInsufficient arguments, try \"/turtles reload\"."); return true; } //false

        //reload if argument is "reload" or "r"
        if( args[0].equals("reload") || args[0].equals("r") ) {
            occurance.reloadConfig();

            //reload drops
            {
                boolean did_drop = reloadable_drops.change_drops;
                int previous_maximum = reloadable_drops.roll_maximum;

                reloadable_drops.change_drops = occurance.getConfig().getBoolean("turtles_drop_scute");
                reloadable_drops.roll_maximum = occurance.getConfig().getInt("scute_roll_maximum");

                if ( (!did_drop && reloadable_drops.change_drops) || (previous_maximum != reloadable_drops.roll_maximum) ) { occurance.improved_turtles_logger.info("Turtles now drop " + reloadable_drops.roll_maximum + " scute per roll."); }
                if (did_drop && !reloadable_drops.change_drops) { occurance.improved_turtles_logger.info("Turtles drop seagrass again (Vanilla)."); }
            }
            //reload helmets
            {
                boolean did_diamond = reloadable_upgrades.enable_diamond_upgrade;
                boolean did_netherite = reloadable_upgrades.enable_both_upgrades;

                reloadable_upgrades.enable_diamond_upgrade = occurance.getConfig().getBoolean("enable_diamond_turtle_helmets");
                reloadable_upgrades.enable_both_upgrades = occurance.getConfig().getBoolean("enable_netherite_turtle_helmets");

                if( did_diamond && !reloadable_upgrades.enable_diamond_upgrade ) { occurance.improved_turtles_logger.info("Diamond Shell Upgrade now disabled."); }
                if( !did_diamond && reloadable_upgrades.enable_diamond_upgrade ) { occurance.improved_turtles_logger.info("Diamond Shell Upgrade now enabled."); }
                if( did_netherite && !reloadable_upgrades.enable_both_upgrades ) { occurance.improved_turtles_logger.info("Netherite Shell Upgrade now disabled."); }
                if( !did_netherite && reloadable_upgrades.enable_both_upgrades ) { occurance.improved_turtles_logger.info("Netherite Shell Upgrade now enabled."); }
            }
            //reload growing
            {
                boolean did_drop_on_grow = reloadable_molting.drop_on_grow;

                reloadable_molting.drop_on_grow = occurance.getConfig().getBoolean("enable_scute_on_grow_up");

                if(did_drop_on_grow && !reloadable_molting.drop_on_grow) { occurance.improved_turtles_logger.info("Turtles nolonger molt."); }
                if(!did_drop_on_grow && reloadable_molting.drop_on_grow) { occurance.improved_turtles_logger.info("Turtles molt scute (vanilla)."); }
            }

            if(sender instanceof Player) { sender.sendMessage("Reloaded turtles!"); }
            occurance.improved_turtles_logger.info("Reloaded config.");
            return true;
        }
        return true; //false
    }
}
