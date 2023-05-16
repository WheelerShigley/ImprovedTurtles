package me.solacekairos.improvedturtles.command;

import me.solacekairos.improvedturtles.turtle.Drops;
import me.solacekairos.improvedturtles.recipies.Helmets;
import me.solacekairos.improvedturtles.ImprovedTurtles;
import me.solacekairos.improvedturtles.turtle.ReturnToDrop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Reload implements TabExecutor {
    ImprovedTurtles occurance;
    Drops reloadable_drops; Helmets reloadable_upgrades; ReturnToDrop reloadable_molting;

    public Reload(ImprovedTurtles plugin) {
        this.occurance = plugin;
        this.reloadable_drops = plugin.Dropper;
        this.reloadable_upgrades = plugin.Upgrades;
        this.reloadable_molting = plugin.moltAtHome;
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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> possible_autocompletions = new ArrayList<>();
        if(args.length == 1) { possible_autocompletions.add("reload"); }

        return possible_autocompletions;
    }
}