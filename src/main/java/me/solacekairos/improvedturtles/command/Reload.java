package me.solacekairos.improvedturtles.command;

import me.solacekairos.improvedturtles.turtle.Drops;
import me.solacekairos.improvedturtles.recipies.Helmets;
import me.solacekairos.improvedturtles.ImprovedTurtles;
import me.solacekairos.improvedturtles.turtle.ReturnToDrop;
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
                //temporary, previous values
                int previous_maximum = reloadable_drops.roll_maximum, previous_total_maximum = reloadable_drops.drop_count_maximum;
                boolean did_drop = reloadable_drops.change_drops;
                String previous_material = reloadable_drops.drop_name;
                double previous_probability = reloadable_drops.probability;

                //get new values
                reloadable_drops.drop_name    = occurance.getConfig().getString("drop_material"); reloadable_drops.getMaterial(reloadable_drops.drop_name);
                reloadable_drops.change_drops = occurance.getConfig().getBoolean("change_turtles_drops");
                reloadable_drops.roll_maximum = occurance.getConfig().getInt("drop_roll_maximum");
                reloadable_drops.probability  = occurance.getConfig().getDouble("drop_probability");
                reloadable_drops.drop_count_maximum = occurance.getConfig().getInt("total_maximum"); if(reloadable_drops.drop_count_maximum == -1) { reloadable_drops.drop_count_maximum = reloadable_drops.MAXIMUM_32; }

                //compare with previous values, output if changes occured
                if ( (!did_drop && reloadable_drops.change_drops) || (previous_maximum != reloadable_drops.roll_maximum) || (previous_material != reloadable_drops.drop_name) || (previous_probability != reloadable_drops.probability) || (previous_total_maximum != reloadable_drops.drop_count_maximum) ) {
                    occurance.improved_turtles_logger.info("Turtles now drop "+100*reloadable_drops.probability+"% "+ reloadable_drops.roll_maximum + " "+reloadable_drops.drop_name+" per roll: [0,"+reloadable_drops.drop_count_maximum+"].");
                }
                if (did_drop && !reloadable_drops.change_drops) { occurance.improved_turtles_logger.info("Turtles drop [0,2] seagrass again (Vanilla)."); }
            }
            //reload helmets
            {
                //temporary, previous values
                boolean did_diamond = reloadable_upgrades.enable_diamond_upgrade, did_netherite = reloadable_upgrades.enable_both_upgrades;

                //get new values
                reloadable_upgrades.enable_diamond_upgrade = occurance.getConfig().getBoolean("enable_diamond_turtle_helmets");
                reloadable_upgrades.enable_both_upgrades = occurance.getConfig().getBoolean("enable_netherite_turtle_helmets");

                //compare with previous values, output if changes occured
                if( did_diamond && !reloadable_upgrades.enable_diamond_upgrade ) { occurance.improved_turtles_logger.info("Diamond Shell Upgrade now disabled."); }
                if( !did_diamond && reloadable_upgrades.enable_diamond_upgrade ) { occurance.improved_turtles_logger.info("Diamond Shell Upgrade now enabled."); }
                if( did_netherite && !reloadable_upgrades.enable_both_upgrades ) { occurance.improved_turtles_logger.info("Netherite Shell Upgrade now disabled."); }
                if( !did_netherite && reloadable_upgrades.enable_both_upgrades ) { occurance.improved_turtles_logger.info("Netherite Shell Upgrade now enabled."); }
            }
            //reload growing
            {
                //temporary, previous values
                boolean did_drop_on_grow =   reloadable_molting.drop_on_grow, did_return_on_grow = reloadable_molting.return_on_grow, did_molt_and_return_on_grow = reloadable_molting.return_and_molt_on_grow;
                int previous_minimum = reloadable_molting.minimum, previous_maximum = reloadable_molting.maximum;

                //get new values
                reloadable_molting.drop_on_grow =            occurance.getConfig().getBoolean("enable_scute_on_grow_up");
                reloadable_molting.return_on_grow =          occurance.getConfig().getBoolean("return_home_on_grow_up");
                reloadable_molting.return_and_molt_on_grow = occurance.getConfig().getBoolean("molt_when_return_home");
                reloadable_molting.minimum = occurance.getConfig().getInt("minimum_drop_quantity");
                reloadable_molting.maximum = occurance.getConfig().getInt("maximum_drop_quantity");

                //compare with previous values, output if changes occured
                if(did_drop_on_grow && !reloadable_molting.drop_on_grow) { occurance.improved_turtles_logger.info("Turtles nolonger molt."); }
                if(!did_drop_on_grow && reloadable_molting.drop_on_grow) { occurance.improved_turtles_logger.info("Turtles molt scute (vanilla)."); }
                if( (did_return_on_grow && !reloadable_molting.return_on_grow) && !(did_molt_and_return_on_grow) ) { occurance.improved_turtles_logger.info("Turtles nolonger return home when grown up."); }
                if( (!did_return_on_grow && reloadable_molting.return_on_grow) && !(did_molt_and_return_on_grow) ) { occurance.improved_turtles_logger.info("Turtles now return home when grown up."); }
                if(did_molt_and_return_on_grow && !reloadable_molting.return_and_molt_on_grow) { occurance.improved_turtles_logger.info("Turtles nolonger return home to molt when grown up."); }
                if(!did_molt_and_return_on_grow && reloadable_molting.return_and_molt_on_grow) { occurance.improved_turtles_logger.info("Turtles always return home to molt when grown up."); }
                if( (previous_minimum != reloadable_molting.minimum) || (previous_maximum != reloadable_molting.maximum) ) { occurance.improved_turtles_logger.info("Drop amount updated to: {" + reloadable_molting.minimum + " to " + reloadable_molting.maximum + "}"); }
            }

            if(sender instanceof Player) { sender.sendMessage("Reloaded turtles!"); }
            occurance.improved_turtles_logger.info("Reloaded config.");
            return true;
        }
        return true; //false
    }
}