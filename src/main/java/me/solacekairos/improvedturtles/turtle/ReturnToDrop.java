package me.solacekairos.improvedturtles.turtle;

import me.solacekairos.improvedturtles.ImprovedTurtles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Turtle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import java.util.Random;
import java.util.logging.Logger;

public class ReturnToDrop implements Listener {

    Logger improved_turtles_logger;
    BukkitScheduler scheduler;
    Plugin improved_turtles;
    Random prng;

    //instance variables
    int minimum = 1;
    public void setMinimum(int min) {
        minimum = min;
        if(maximum < minimum) { maximum = min; }
    }
    public int getMinimum() { return minimum; }

    int maximum = 1;
    public void setMaximum(int max) {
        maximum = max;
        if(maximum < minimum) { minimum = max; }
    }
    public int getMaximum() { return maximum; }

    boolean drop_on_grow = true;
    public void setDoGrowthDrop(boolean do_drop) { drop_on_grow = do_drop; }
    public boolean getDoGrowthDrop() { return drop_on_grow; }

    boolean return_on_grow = false;
    public void setReturnOnGrow(boolean do_return) { return_on_grow = do_return; }
    public boolean getReturnOnGrow() { return return_on_grow; }

    boolean return_and_molt_on_grow = false;
    public void setDoReturnToMolt(boolean do_return_to_molt) { return_on_grow = do_return_to_molt; }
    public boolean getDoReturnToMolt() { return return_on_grow; }

    public ReturnToDrop(ImprovedTurtles plugin) {
        prng = new Random();

        improved_turtles = plugin;
        scheduler = plugin.getServer().getScheduler();
        improved_turtles_logger = plugin.improved_turtles_logger;

        reloadGrowthDrop(plugin);
    }

    public void reloadGrowthDrop(ImprovedTurtles plugin) {
        int previous_minimum = minimum,
            previous_maximum = maximum;
        boolean previous_drop_on_grow = drop_on_grow,
                previous_return_on_grow = return_on_grow,
                previous_return_and_molt_on_grow = return_and_molt_on_grow;

        setMinimum(         plugin.getConfig().getInt("minimum_drop_quantity")      );
        setMaximum(         plugin.getConfig().getInt("maximum_drop_quantity")      );
        setDoGrowthDrop(    plugin.getConfig().getBoolean("enable_drop_on_grow_up") );
        setReturnOnGrow(    plugin.getConfig().getBoolean("return_home_on_grow_up") );
        setDoReturnToMolt(  plugin.getConfig().getBoolean("molt_when_return_home")  );

        if(previous_drop_on_grow && !drop_on_grow) { improved_turtles_logger.info("Turtles nolonger molt."); }
        if(!previous_drop_on_grow && drop_on_grow) { improved_turtles_logger.info("Turtles molt scute (vanilla)."); }
        if( (previous_return_on_grow && !return_on_grow) && !(previous_return_and_molt_on_grow) ) { improved_turtles_logger.info("Turtles nolonger return home when grown up."); }
        if( (!previous_return_on_grow && return_on_grow) && !(previous_return_and_molt_on_grow) ) { improved_turtles_logger.info("Turtles now return home when grown up."); }
        if(previous_return_and_molt_on_grow && !return_and_molt_on_grow) { improved_turtles_logger.info("Turtles nolonger return home to molt when grown up."); }
        if(!previous_return_and_molt_on_grow && return_and_molt_on_grow) { improved_turtles_logger.info("Turtles always return home to molt when grown up."); }
        if( (previous_minimum != minimum) || (previous_maximum != maximum) ) { improved_turtles_logger.info("Molting drop-amount updated to: [" + minimum + "," + maximum + "]."); }
    }

    private void go_home(boolean molt_when_home, Entity entity, Location home, int amount) {

        if( entity.isDead() ) { return; }

        boolean rerun = false;
        if( 2.0 < entity.getLocation().distance(home) ) {
            ( (Turtle)entity ).getPathfinder().moveTo(home);
            rerun = true;
        }
        if(!rerun && molt_when_home) {
            entity.getWorld().dropItem( entity.getLocation(), new ItemStack(Material.SCUTE, amount) );
        }

        if(rerun) {
            scheduler.scheduleSyncDelayedTask( improved_turtles, () -> go_home(return_and_molt_on_grow, entity, home, amount), 120L );
        }
    }

    //Turtles nolonger drop scute
    @EventHandler
    public void PreventScuteDrop(EntityDropItemEvent drop_event) {
        if( drop_event.getEntity().getType() != EntityType.TURTLE ) { return; }

        int quantity = prng.nextInt(maximum-minimum)+minimum;
        if(drop_on_grow) {
            drop_event.getItemDrop().getItemStack().setAmount(quantity);
        }

        //ensure scute is only droped upon request
        if(!drop_on_grow || return_and_molt_on_grow) { drop_event.setCancelled(true); }

        //return home if requested; afterwards, molt if requested:
        if(return_on_grow || return_and_molt_on_grow) {
            Location home = ( (Turtle)drop_event.getEntity() ).getHome();
            go_home(return_and_molt_on_grow, drop_event.getEntity(), home, quantity);
        }
    }
}
