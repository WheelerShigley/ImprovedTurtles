package me.solacekairos.improvedturtles;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public final class ImprovedTurtles extends JavaPlugin {

    public Logger improved_turtles_logger;
    public Drops Dropper;

    //THINGS TO DO:
    /*
    Implement reload command (with permission)
    Add diamond turtle helmet
    Add netherite turtle helmet
     */

    @Override
    public void onEnable() {
        improved_turtles_logger = getLogger();
        improved_turtles_logger.info("Improved Turtles!");

        saveDefaultConfig();

        PluginManager manager = Bukkit.getPluginManager();
        Dropper = new Drops(this);
        manager.registerEvents(Dropper, this);


        getCommand("turtles").setExecutor( new Reload(this) );
        getCommand("turtles").setTabCompleter( new Autofill() );
    }

    @Override
    public void onDisable() {
        improved_turtles_logger.info("Un-improved Turtles.");
    }
}