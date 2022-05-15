package me.lauriichan.minecraft.itembound;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public final class ItemHandler1_12 extends ItemHandler1_8 {

    public ItemHandler1_12(ItemBound plugin) {
        super(plugin);
    }
    
    @Override
    public ItemStack getItemInHand(EntityEquipment equipment) {
        return equipment.getItemInMainHand();
    }

    @Override
    public boolean handleInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return false;
        }
        return super.handleInteract(event);
    }

}
