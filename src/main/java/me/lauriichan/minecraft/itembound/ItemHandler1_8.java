package me.lauriichan.minecraft.itembound;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemHandler1_8 extends ItemHandler {

    public static final String COUNT_DELIMITER = "i/";

    public ItemHandler1_8(ItemBound plugin) {
        super(plugin);
    }

    @SuppressWarnings("deprecation")
    public ItemStack getItemInHand(EntityEquipment equipment) {
        return equipment.getItemInHand();
    }

    @Override
    public boolean handleInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            return false;
        }
        ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return false;
        }
        ItemMeta meta = itemStack.getItemMeta();
        ActionData data = loadActionData(meta);
        if (data == null) {
            return false;
        }
        String action = data.getAction();
        if (data.hasCount()) {
            action = action.replace(COUNT_PLACEHOLDER, data.getCount().toString());
            data.updateCount(meta);
            itemStack.setItemMeta(meta);
        }
        return runAction(event.getPlayer(), action);
    }

    @Override
    public void applyAction(ItemMeta itemMeta, String action) {
        if (action.contains(COUNT_PLACEHOLDER)) {
            List<String> lore = itemMeta.getLore();
            lore.add(ChatColor.DARK_GRAY + COUNT_DELIMITER + '1');
            itemMeta.setLore(lore);
        }
    }

    private ActionData loadActionData(ItemMeta itemMeta) {
        List<String> lore = itemMeta.getLore();
        if (lore.isEmpty()) {
            return null;
        }
        int size = lore.size();
        String last = lore.get(size - 1);
        Integer count = null;
        if (last.startsWith(ChatColor.DARK_GRAY + COUNT_DELIMITER)) {
            String[] parts = last.split(COUNT_DELIMITER);
            if (parts.length == 2) {
                String number = parts[1];
                try {
                    count = Integer.parseInt(number);
                } catch (NumberFormatException nfe) {
                }
            }
        }
        StringBuilder actionBuilder = new StringBuilder();
        for (int index = 2; index < size; index++) {
            String str = ChatColor.stripColor(lore.get(index));
            if (str.length() == 0) {
                break;
            }
            actionBuilder.append(str).append(' ');
        }
        if (actionBuilder.length() == 0) {
            return null;
        }
        return new ActionData(actionBuilder.substring(0, actionBuilder.length() - 1), count);
    }

    private static class ActionData {

        private final String action;
        private final Integer count;

        public ActionData(final String action, final Integer count) {
            this.action = action;
            this.count = count;
        }

        public String getAction() {
            return action;
        }

        public boolean hasCount() {
            return count != null;
        }

        public Integer getCount() {
            return count;
        }

        public void updateCount(ItemMeta meta) {
            if (count == null) {
                return;
            }
            List<String> lore = meta.getLore();
            lore.set(lore.size() - 1, ChatColor.DARK_GRAY + COUNT_DELIMITER + Integer.toString(count + 1));
            meta.setLore(lore);
        }

    }

}
