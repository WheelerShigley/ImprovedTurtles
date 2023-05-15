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
    Logger improved_turtles_logger; BukkitScheduler scheduler; Plugin improved_turtles;
    Random prng;

    public int minimum = 1, maximum = 1;
    public boolean drop_on_grow = true, return_on_grow = false, return_and_molt_on_grow = false;
    public ReturnToDrop(ImprovedTurtles plugin) {
        prng = new Random();

        improved_turtles = plugin;
        scheduler = plugin.getServer().getScheduler();
        improved_turtles_logger = plugin.improved_turtles_logger;

        minimum = plugin.getConfig().getInt("minimum_drop_quantity");
        maximum = plugin.getConfig().getInt("maximum_drop_quantity");

        drop_on_grow =            plugin.getConfig().getBoolean("enable_scute_on_grow_up");
        return_on_grow =          plugin.getConfig().getBoolean("return_home_on_grow_up");
        return_and_molt_on_grow = plugin.getConfig().getBoolean("molt_when_return_home");
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
