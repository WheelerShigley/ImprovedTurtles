package me.solacekairos.improvedturtles;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public final class ImprovedTurtles extends JavaPlugin {

    private Logger improved_turtles_logger;

    //THINGS TO DO:
    /*
    Implement reload command
    Add diamond turtle helmet
    Add netherite turtle helmet
     */

    @Override
    public void onEnable() {
        improved_turtles_logger = getLogger();
        improved_turtles_logger.info("Improved Turtles!");

        saveDefaultConfig();

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new Drops(this, improved_turtles_logger), this);
    }

    @Override
    public void onDisable() {
        improved_turtles_logger.info("Un-improved Turtles.");
    }
}
