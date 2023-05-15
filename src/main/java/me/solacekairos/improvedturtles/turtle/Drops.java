package me.solacekairos.improvedturtles.turtle;

import me.solacekairos.improvedturtles.ImprovedTurtles;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import java.util.Random;

public class Drops implements Listener {
    public final int MAXIMUM_32 = 0b01111111111111111111111111111111; //maximum 32-bit integer

    int i, roll_count, drop_count;
    Ageable maybe_baby;
    Random prng = new Random();

    //get config values
    public boolean change_drops = false;
    public int roll_maximum = 0;
    public double probability = 0.5;
    public String drop_name = "SEAGRASS"; Material drop = Material.SEAGRASS;
    public int drop_count_maximum = 1;

    public Drops(ImprovedTurtles plugin) {
        drop_name    = plugin.getConfig().getString("drop_material"); getMaterial(drop_name);
        change_drops = plugin.getConfig().getBoolean("change_turtles_drops");
        probability  = plugin.getConfig().getDouble("drop_probability");
        roll_maximum = plugin.getConfig().getInt("drop_roll_maximum");
        drop_count_maximum = plugin.getConfig().getInt("total_maximum"); if(drop_count_maximum == -1) { drop_count_maximum = MAXIMUM_32; }

        if(change_drops) { plugin.improved_turtles_logger.info("Turtles now drop "+drop.toString()+" @"+roll_maximum+" per roll."); }
    }

    public void getMaterial(String name) {
        //manual switch statement
        String temporary_name = name.toUpperCase();

        if( temporary_name.equals("TURTLE_HELMET") || temporary_name.equals("TURTLE_SHELL") ) { drop = Material.TURTLE_HELMET; return; }
        if( temporary_name.equals("SCUTE") ) { drop = Material.SCUTE; return; }
        if( !( temporary_name.equals("TURTLE_HELMET") || temporary_name.equals("TURTLE_SHELL") || temporary_name.equals("SCUTE") ) ) { drop = Material.SEAGRASS; /*drop_name = "SEAGRASS";*/ }
    }

    @EventHandler
    public void onTurtleKill(EntityDeathEvent death) {
        //only enable if configured to be enabled
        if(!change_drops) { return; }

        //ensure entity is adult turtle
        if(death.getEntity().getType() != EntityType.TURTLE) { return; }
        if(death.getEntity() instanceof Ageable) {
            maybe_baby = (Ageable)death.getEntity();
            if( !maybe_baby.isAdult() ) { return; }
        }

        //roll_count = looting_level + 1;
        roll_count = 1 + death.getEntity().getKiller().getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
        if(roll_count < 1) { roll_count = 1; }
        //if(10 < roll_count) { roll_count = 10; } //maximum looting level + 1 ?

        //add drops, DO NOT add eggs as drop: they cracked through violence
        double linear_probability = (double)( prng.nextInt() ) / (double)(MAXIMUM_32);

        ItemStack is_drop = new ItemStack(drop);
        drop_count = 0; for(i = 0; i < roll_count; i++) { if(linear_probability <= probability) { drop_count++; }  } //50% chance each roll
        if(drop_count_maximum < drop_count) { drop_count = drop_count_maximum; }
        is_drop.setAmount(drop_count);

        death.getDrops().clear(); death.getDrops().add(is_drop);
    }

}
