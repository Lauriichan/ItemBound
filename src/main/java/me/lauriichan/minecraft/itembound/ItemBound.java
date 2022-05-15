package me.lauriichan.minecraft.itembound;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;

import me.lauriichan.minecraft.itembound.data.MainConfiguration;
import me.lauriichan.minecraft.itembound.data.MessageConfiguration;
import me.lauriichan.minecraft.itembound.data.io.Reloadable;
import me.lauriichan.minecraft.itembound.data.message.Message;
import me.lauriichan.minecraft.itembound.data.message.Messages;

@SuppressWarnings("deprecation")
public final class ItemBound extends JavaPlugin {

    private ItemHandler itemHandler;

    private MainConfiguration mainConfig;
    private MessageConfiguration messageConfig;

    @Override
    public void onLoad() {
        Message.setLogger(getLogger());
        Messages.PREFIX.getFallback();
        itemHandler = buildHandler();
        mainConfig = new MainConfiguration(new File(getDataFolder(), "config.yml"));
        messageConfig = new MessageConfiguration(new File(getDataFolder(), "message.yml"));
    }

    @Override
    public void onEnable() {
        Reloadable.update();
        Reloadable.start();
        Bukkit.getPluginManager().registerEvents(new ItemListener(itemHandler), this);
        PluginCommand command = getCommand("bind");
        ItemCommand itemCommand = new ItemCommand(itemHandler);
        command.setExecutor(itemCommand);
        command.setTabCompleter(itemCommand);
    }
    
    @Override
    public void onDisable() {
        Reloadable.shutdown();
    }

    /*
     * Getter
     */

    public ItemHandler getItemHandler() {
        return itemHandler;
    }

    public MainConfiguration getMainConfig() {
        return mainConfig;
    }

    public MessageConfiguration getMessageConfig() {
        return messageConfig;
    }

    /*
     * Build handler
     */

    private final ItemHandler buildHandler() {
        try {
            PersistentDataContainer.class.getClass();
            return new ItemHandler1_14(this);
        } catch (NoClassDefFoundError ig) {
        }
        try {
            CustomItemTagContainer.class.getClass();
            return new ItemHandler1_13(this);
        } catch (NoClassDefFoundError ig) {
        }
        try {
            EquipmentSlot.valueOf("OFF_HAND");
            return new ItemHandler1_12(this);
        } catch (IllegalArgumentException ig) {
        }
        return new ItemHandler1_8(this);
    }

}
