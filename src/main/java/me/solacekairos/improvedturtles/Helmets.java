package me.solacekairos.improvedturtles;

import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import static org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER;
import static org.bukkit.inventory.EquipmentSlot.HEAD;

public class Helmets implements Listener {

    public boolean enable_diamond_upgrade = true, enable_both_upgrades = true;
    public Helmets(ImprovedTurtles plugin) {
        enable_diamond_upgrade = plugin.getConfig().getBoolean("enable_diamond_turtle_helmets");
        enable_both_upgrades = plugin.getConfig().getBoolean("enable_netherite_turtle_helmets");

        if(enable_diamond_upgrade) { plugin.improved_turtles_logger.info("Turtle Shells are now upgradable to Diamond Shells."); }
        if(enable_both_upgrades) { plugin.improved_turtles_logger.info("Turtle Shells are now upgradable to Netherite Shells."); }
    }

    @EventHandler
    void onSmithingTableEvent(PrepareSmithingEvent smith) {
        String prefix = "§r§b", name = "Turtle Shell";
        boolean enable = false;
        double armor = 2.0, toughness = 0.0, knockback_resistence = 0.0; //defaults

        SmithingInventory inventory = smith.getInventory();

        ItemStack item = inventory.getItem(0);
        ItemStack modifier = inventory.getItem(1);

        if(item == null || modifier == null) { return; }
        if(item.getType() != Material.TURTLE_HELMET) { return; }
        ItemMeta meta = item.getItemMeta();

        //get current attributes
        if( meta.hasDisplayName() ) { name = meta.getDisplayName(); }
        {
            String temp = "";
            for (int i = 0; i < name.length(); i++) {
                if (!(name.charAt(i) == '§')) {
                    temp += name.charAt(i);
                } else {
                    i++;
                }
            }
            name = temp;
        }

        Multimap<Attribute, AttributeModifier> modifications = meta.getAttributeModifiers();
        if( modifications != null && !modifications.isEmpty() ) {
            Collection<AttributeModifier> collection = modifications.values();
            for( AttributeModifier local_am : collection) {
                switch( local_am.getName() ) {
                    case "Generic.Armor":               armor = local_am.getAmount(); break;
                    case "Generic.Armor_Toughness":     toughness = local_am.getAmount(); break;
                    case "Generic.Knockback_Resistance": knockback_resistence = local_am.getAmount();
                }
            }
        }

        ItemStack result = item.clone();
        if( armor == 2.0 && toughness == 0.0 && modifier.getType() == Material.DIAMOND_HELMET ) {
            if(!enable_both_upgrades || !enable_diamond_upgrade) { smith.setResult( new ItemStack(Material.AIR) ); return; }
            enable = true;
            if(name == "Turtle Shell") { prefix = "§r§b"; name = "Diamond Shell"; } else { prefix = "§b§o"; }
            armor = 3.0; toughness = 2.0;
        }
        if( armor == 2.0 && toughness == 0.0 && modifier.getType() == Material.NETHERITE_HELMET ) {
            if(!enable_both_upgrades) { smith.setResult( new ItemStack(Material.AIR) ); return; }
            enable = true;
            if(name == "Turtle Shell") { prefix = "§r§e"; name = "Netherite Shell"; } else { prefix = "§e§o"; }
            armor = 3.0; toughness = 2.0; knockback_resistence = 1.0;
        }
        if( armor == 3.0 && toughness == 2.0 && modifier.getType() == Material.NETHERITE_INGOT ) {
            if(!enable_both_upgrades) { smith.setResult( new ItemStack(Material.AIR) ); return; }
            enable = true;
            if(name == "Diamond Shell") { prefix = "§r§e"; name = "Netherite Shell"; } else { prefix = "§e§o"; }
            armor = 3.0; toughness = 2.0; knockback_resistence = 1.0;
        }

        if(enable) {
            meta.setDisplayName(prefix + name);

            meta.removeAttributeModifier(Attribute.GENERIC_ARMOR);                  meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Generic.Armor", armor, ADD_NUMBER, HEAD ) );
            meta.removeAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS);        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(),"Generic.Armor_Toughness", toughness, ADD_NUMBER, HEAD ) );
            meta.removeAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE);   meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(),"Generic.Knockback_Resistance", knockback_resistence, ADD_NUMBER, HEAD ) );
            result.setItemMeta( (ItemMeta)(meta) );

            smith.setResult(result);
        } else {
            smith.setResult( new ItemStack(Material.AIR) );
        }

        List<HumanEntity> viewers = smith.getViewers();
        viewers.forEach( person -> ( (Player)(person) ).updateInventory() );

    }

}