package me.lauriichan.minecraft.itembound;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public final class ItemListener implements Listener {

    private final ItemHandler itemHandler;

    public ItemListener(ItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().hasPermission("itembound.use")) {
            return;
        }
        event.setCancelled(itemHandler.handleInteract(event));
    }

}
