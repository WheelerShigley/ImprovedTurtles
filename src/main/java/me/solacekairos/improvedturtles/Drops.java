package me.solacekairos.improvedturtles;

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
    int i, roll_count, drop_count;
    Ageable maybe_baby;
    Random prng = new Random();

    //get config values
    public boolean change_drops = false;
    public int roll_maximum = 0;

    public Drops(ImprovedTurtles plugin) {
        change_drops = plugin.getConfig().getBoolean("turtles_drop_scute");
        roll_maximum = plugin.getConfig().getInt("scute_roll_maximum");

        if(change_drops) { plugin.improved_turtles_logger.info("Turtles now drop scute @"+roll_maximum+" per roll."); }
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
        ItemStack drop = new ItemStack(Material.SCUTE);
        drop_count = 0; for(i = 0; i < roll_count; i++) { drop_count += prng.nextInt(roll_maximum+1); } //50% chance each roll
        drop.setAmount(drop_count);

        death.getDrops().clear(); death.getDrops().add(drop);
    }

}