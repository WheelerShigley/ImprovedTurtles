package me.solacekairos.improvedturtles;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public final class ImprovedTurtles extends JavaPlugin {

    public Logger improved_turtles_logger;
    public Drops Dropper; public Helmets Upgrades; public ReturnToDrop MoltAtHome;

    @Override
    public void onEnable() {
        improved_turtles_logger = getLogger();
        improved_turtles_logger.info("Improved Turtles!");

        saveDefaultConfig();

        PluginManager manager = Bukkit.getPluginManager();
        Dropper = new Drops(this);
        Upgrades = new Helmets(this);
        MoltAtHome = new ReturnToDrop(this);

        //events added:
        manager.registerEvents(Dropper, this);
        manager.registerEvents(Upgrades, this);
        manager.registerEvents(MoltAtHome, this);

        //recipies added:
        {
            SmithingRecipe diamond_shell = new SmithingRecipe(
                    new NamespacedKey(this, "Diamond_Shell"),
                    new ItemStack(Material.TURTLE_HELMET),
                    new RecipeChoice.MaterialChoice(Material.TURTLE_HELMET),
                    new RecipeChoice.MaterialChoice(Material.DIAMOND_HELMET)
            );
            SmithingRecipe netherite_shell = new SmithingRecipe(
                    new NamespacedKey(this, "Netherite_Shell"),
                    new ItemStack(Material.TURTLE_HELMET),
                    new RecipeChoice.MaterialChoice(Material.TURTLE_HELMET),
                    new RecipeChoice.MaterialChoice(Material.NETHERITE_HELMET)
            );
            SmithingRecipe shell_upgrade = new SmithingRecipe(
                    new NamespacedKey(this, "Shell_Upgrade"),
                    new ItemStack(Material.TURTLE_HELMET),
                    new RecipeChoice.MaterialChoice(Material.TURTLE_HELMET),
                    new RecipeChoice.MaterialChoice(Material.NETHERITE_INGOT)
            );
            Bukkit.addRecipe(diamond_shell);
            Bukkit.addRecipe(netherite_shell);
            Bukkit.addRecipe(shell_upgrade);
        }

        //commands added:
        getCommand("turtles").setExecutor( new Reload(this) );
        getCommand("turtles").setTabCompleter( new Autofill() );
    }

    @Override
    public void onDisable() {
        improved_turtles_logger.info("Un-improved Turtles.");
    }
}