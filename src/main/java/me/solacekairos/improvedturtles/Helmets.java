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

    public Helmets() {}

    @EventHandler
    void onSmithingTableEvent(PrepareSmithingEvent smith) {
        String name = "§r§bTurtle Shell";
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
            enable = true;
            if(name == "§r§bTurtle Shell") { name = "§r§bDiamond Shell"; } else { name = "§e§o"+name; }
            armor = 3.0; toughness = 2.0;
        }
        if( (armor == 3.0 && toughness == 2.0 && modifier.getType() == Material.NETHERITE_INGOT) || (armor == 2.0 && toughness == 0.0 && modifier.getType() == Material.NETHERITE_HELMET) ) {
            enable = true;
            if(name == "§r§bTurtle Shell") { name = "§r§eNetherite Shell"; } else { name = "§e§o"+name; }
            armor = 3.0; toughness = 2.0; knockback_resistence = 1.0;
        }

        if(enable) {
            meta.setDisplayName(name);

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
