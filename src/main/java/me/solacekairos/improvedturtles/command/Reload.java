package me.solacekairos.improvedturtles.command;

import me.solacekairos.improvedturtles.turtle.Drops;
import me.solacekairos.improvedturtles.recipies.Helmets;
import me.solacekairos.improvedturtles.ImprovedTurtles;
import me.solacekairos.improvedturtles.turtle.ReturnToDrop;
import org.bukkit.Material;
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
        if(args.length < 1 || 2 < args.length) { sender.sendMessage("§cInsufficient arguments, try \"/turtles reload\"."); return true; /*fake false*/ }

        //reload if argument is "reload" or "r"
        if( args[0].equals("reload") || args[0].equals("r") ) {

            occurance.reloadConfig();
            reloadable_drops.reloadDrops(        occurance );
            reloadable_molting.reloadGrowthDrop( occurance );
            reloadable_upgrades.reloadHelmets(   occurance );

            if(sender instanceof Player) { sender.sendMessage("Reloaded turtles!"); }
            occurance.improved_turtles_logger.info("Reloaded config.");
            return true;
        }

        return true; /*fake false*/
    }
}