package me.lauriichan.minecraft.itembound;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ItemHandler {

    public static final String COUNT_PLACEHOLDER = "%%i";

    protected final ItemBound plugin;

    public ItemHandler(final ItemBound plugin) {
        this.plugin = plugin;
    }

    public abstract ItemStack getItemInHand(EntityEquipment equipment);

    public boolean isAir(Material material) {
        return material == Material.AIR;
    }

    public abstract boolean handleInteract(PlayerInteractEvent event);

    public abstract void applyAction(ItemMeta itemMeta, String action);

    public final boolean runAction(Player player, String action) {
        if (action.startsWith("/")) {
            Bukkit.dispatchCommand(player, action.substring(1));
            return true;
        }
        player.chat(action);
        return true;
    }

}
