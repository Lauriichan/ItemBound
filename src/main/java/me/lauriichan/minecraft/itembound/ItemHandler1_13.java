package me.lauriichan.minecraft.itembound;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

@SuppressWarnings("deprecation")
public final class ItemHandler1_13 extends ItemHandler {

    private final NamespacedKey actionKey;
    private final NamespacedKey countKey;

    public ItemHandler1_13(ItemBound plugin) {
        super(plugin);
        this.actionKey = new NamespacedKey(plugin, "action");
        this.countKey = new NamespacedKey(plugin, "count");
    }
    
    @Override
    public ItemStack getItemInHand(EntityEquipment equipment) {
        return equipment.getItemInMainHand();
    }

    @Override
    public boolean handleInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL || event.getHand() != EquipmentSlot.HAND) {
            return false;
        }
        ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return false;
        }
        ItemMeta meta = itemStack.getItemMeta();
        CustomItemTagContainer container = meta.getCustomTagContainer();
        if (!container.hasCustomTag(actionKey, ItemTagType.STRING)) {
            return false;
        }
        String action = container.getCustomTag(actionKey, ItemTagType.STRING);
        if (container.hasCustomTag(countKey, ItemTagType.INTEGER)) {
            int count = container.getCustomTag(countKey, ItemTagType.INTEGER);
            action = action.replace(COUNT_PLACEHOLDER, Integer.toString(count));
            container.setCustomTag(countKey, ItemTagType.INTEGER, count + 1);
            itemStack.setItemMeta(meta);
        }
        return runAction(event.getPlayer(), action);
    }

    @Override
    public void applyAction(ItemMeta itemMeta, String action) {
        CustomItemTagContainer container = itemMeta.getCustomTagContainer();
        container.setCustomTag(actionKey, ItemTagType.STRING, action);
        if (action.contains(COUNT_PLACEHOLDER)) {
            container.setCustomTag(countKey, ItemTagType.INTEGER, 1);
        }
    }

}
