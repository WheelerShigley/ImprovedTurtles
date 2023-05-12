package me.solacekairos.improvedturtles;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;

public class ReturnToDrop implements Listener {

    boolean drop_on_grow = true;
    public ReturnToDrop(ImprovedTurtles plugin) {
        drop_on_grow = plugin.getConfig().getBoolean("enable_scute_on_grow_up");
    }

    //Turtles nolonger drop scute
    @EventHandler
    public void PreventScuteDrop(EntityDropItemEvent drop_event) {
        if(drop_on_grow) { drop_event.setCancelled(false); return; }
        if( drop_event.getEntity().getType() != EntityType.TURTLE ) { return; }
        if( drop_event.getItemDrop().getItemStack().getType() == Material.SCUTE ) {
            //do things here
            drop_event.setCancelled(true);
        }
    }
}
