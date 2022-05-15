package me.lauriichan.minecraft.itembound;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class ItemHandler1_14 extends ItemHandler {

    private final NamespacedKey actionKey;
    private final NamespacedKey countKey;

    public ItemHandler1_14(ItemBound plugin) {
        super(plugin);
        this.actionKey = new NamespacedKey(plugin, "action");
        this.countKey = new NamespacedKey(plugin, "count");
    }

    @Override
    public ItemStack getItemInHand(EntityEquipment equipment) {
        return equipment.getItemInMainHand();
    }

    @Override
    public boolean isAir(Material material) {
        return material.name().endsWith("AIR");
    }

    @Override
    public boolean handleInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL || event.getHand() != EquipmentSlot.HAND) {
            return false;
        }
        ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType().name().endsWith("AIR")) {
            return false;
        }
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(actionKey, PersistentDataType.STRING)) {
            return false;
        }
        String action = container.get(actionKey, PersistentDataType.STRING);
        if (container.has(countKey, PersistentDataType.INTEGER)) {
            int count = container.get(countKey, PersistentDataType.INTEGER);
            action = action.replace(COUNT_PLACEHOLDER, Integer.toString(count));
            container.set(countKey, PersistentDataType.INTEGER, count + 1);
            itemStack.setItemMeta(meta);
        }
        return runAction(event.getPlayer(), action);
    }

    @Override
    public void applyAction(ItemMeta itemMeta, String action) {
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(actionKey, PersistentDataType.STRING, action);
        if (action.contains(COUNT_PLACEHOLDER)) {
            container.set(countKey, PersistentDataType.INTEGER, 1);
        }
    }

}
