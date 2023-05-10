package me.solacekairos.improvedturtles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class Autofill implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cde, String arg, String[] args) {

        List<String> possible_autocompletions = new ArrayList<>();
        if(args.length == 1) {
            possible_autocompletions.add("reload");
        }

        return possible_autocompletions;
    }
}