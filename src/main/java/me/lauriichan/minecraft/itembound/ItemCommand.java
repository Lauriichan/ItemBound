package me.lauriichan.minecraft.itembound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.lauriichan.minecraft.itembound.data.message.Messages;

public final class ItemCommand implements CommandExecutor, TabCompleter {

    public static final int MAX_SIZE = 48;
    public static final int THRESHOLD = 12;

    private final ItemHandler itemHandler;

    public ItemCommand(final ItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("itembound.use")) {
            Messages.COMMAND_BIND_NOT_PERMITTED.send(sender);
            return false;
        }
        if (!(sender instanceof Player)) {
            Messages.COMMAND_BIND_ONLY_PLAYER.send(sender);
            return false;
        }
        if (args.length == 0) {
            Messages.COMMAND_BIND_NO_ACTION.send(sender);
            return false;
        }
        ItemStack itemStack = itemHandler.getItemInHand(((Player) sender).getEquipment());
        if (itemStack == null || itemHandler.isAir(itemStack.getType())) {
            Messages.COMMAND_BIND_NO_ITEM.send(sender);
            return false;
        }
        String action = Arrays.asList(args).stream().collect(Collectors.joining(" "));
        ItemMeta meta = itemStack.getItemMeta();
        setLore(meta, action);
        meta.setDisplayName(getColor(action) + (action.length() > 48 ? action.substring(0, 45) + "..." : action));
        itemHandler.applyAction(meta, action);
        itemStack.setItemMeta(meta);
        Messages.COMMAND_BIND_SUCCESS.send(sender);
        return false;
    }

    private ChatColor getColor(String action) {
        if (action.startsWith("/")) {
            if (action.contains(ItemHandler.COUNT_PLACEHOLDER)) {
                return ChatColor.LIGHT_PURPLE;
            }
            return ChatColor.DARK_PURPLE;
        }
        if (action.contains(ItemHandler.COUNT_PLACEHOLDER)) {
            return ChatColor.AQUA;
        }
        return ChatColor.BLUE;
    }

    private void setLore(ItemMeta meta, String action) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "Bound action: ");
        try {
            if (action.length() <= 48) {
                lore.add(ChatColor.GRAY + action);
                return;
            }
            if (!action.contains(" ")) {
                int size = action.length();
                for (int index = 0; index < size; index += MAX_SIZE) {
                    lore.add(ChatColor.GRAY + action.substring(index, Math.min(index + MAX_SIZE, size)));
                }
                return;
            }
            String[] parts = action.split(" ");
            StringBuilder builder = new StringBuilder();
            for (String part : parts) {
                builder.append(part).append(" ");
                if (builder.length() > MAX_SIZE) {
                    try {
                        if (builder.length() > MAX_SIZE + THRESHOLD) {
                            String built = builder.toString();
                            int size = built.length();
                            for (int index = 0; index < size; index += MAX_SIZE) {
                                lore.add(ChatColor.GRAY + built.substring(index, Math.min(index + MAX_SIZE, size)));
                            }
                            continue;
                        }
                        lore.add(ChatColor.GRAY + builder.toString());
                    } finally {
                        builder = new StringBuilder();
                    }
                }
            }
            if (builder.length() < MAX_SIZE && builder.length() != 0) {
                lore.add(ChatColor.GRAY + builder.toString());
            }
        } finally {
            meta.setLore(lore);
        }
    }

}
