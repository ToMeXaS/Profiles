package lt.tomexas.profiles.guis;

import lt.tomexas.profiles.Main;
import lt.tomexas.profiles.utils.ConfigHandler;
import lt.tomexas.profiles.utils.ProgressBar;
import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class InventoryUtils {

    private final ConfigHandler configHandler = Main.getConfigHandler();

    public ItemStack getItemStack(@Nullable Player player, Material material, Integer customModelData, @Nullable String displayName, @Nullable List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        SkullMeta skullMeta;
        ItemMeta itemMeta;

        if (material.equals(Material.PLAYER_HEAD)) {
            skullMeta = (SkullMeta) itemStack.getItemMeta();
            skullMeta.setOwnerProfile(player.getPlayerProfile());

            if (customModelData != null)
                skullMeta.setCustomModelData(customModelData);

            skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, displayName)));

            if (lore != null) {
                for (int i = 0; i < lore.size(); i++) {
                    lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
                    lore.set(i, PlaceholderAPI.setPlaceholders(player, lore.get(i)));
                }
                skullMeta.setLore(lore);
            }
            itemStack.setItemMeta(skullMeta);
        } else {
            itemMeta = itemStack.getItemMeta();

            if (customModelData != null)
                itemMeta.setCustomModelData(customModelData);

            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, displayName)));

            if (lore != null) {
                for (int i = 0; i < lore.size(); i++) {
                    lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
                    lore.set(i, PlaceholderAPI.setPlaceholders(player, lore.get(i)));
                }
                itemMeta.setLore(lore);
            }

            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public String getPicBorder(Player player) {
        StringBuilder border = new StringBuilder();

        User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
        String group = LuckPermsProvider.get().getGroupManager().getGroup(user.getPrimaryGroup()).getName();

        if (group.equalsIgnoreCase("default")) border.append("%img_offset_-121%");
        else border.append("%img_offset_-176%%img_profile_tag_").append(group).append("%");

        return border.toString();
    }

    public String getProgressBars(Player argPlayer) {
        StringBuilder progressBar = new StringBuilder(PlaceholderAPI.setPlaceholders(argPlayer, "%img_offset_16%"));

        int i = 0;
        for (Map.Entry<String, LinkedHashMap<Enum<?>, String>> entry : this.configHandler.getGuiProgressBars().entrySet()) {
            if (i > 0) progressBar.append(PlaceholderAPI.setPlaceholders(argPlayer, "%img_offset_-57%"));

            String progress = PlaceholderAPI.setPlaceholders(argPlayer, entry.getValue().get(ConfigHandler.guiProgressBarEnum.CURRENT_XP));
            String maxProgress = PlaceholderAPI.setPlaceholders(argPlayer, entry.getValue().get(ConfigHandler.guiProgressBarEnum.REQUIRED_XP));

            char barEmptyChar = PlaceholderAPI.setPlaceholders(argPlayer, "%img_bar_" + entry.getKey() + "_middle_empty%").charAt(0);
            char endEmptyChar = PlaceholderAPI.setPlaceholders(argPlayer, "%img_bar_" + entry.getKey() + "_end_empty%").charAt(0);
            char barHalfChar = PlaceholderAPI.setPlaceholders(argPlayer, "%img_bar_" + entry.getKey() + "_middle_half%").charAt(0);
            char endHalfChar = PlaceholderAPI.setPlaceholders(argPlayer, "%img_bar_" + entry.getKey() + "_end_half%").charAt(0);
            char barFullChar = PlaceholderAPI.setPlaceholders(argPlayer, "%img_bar_" + entry.getKey() + "_middle_full%").charAt(0);
            char endFullChar = PlaceholderAPI.setPlaceholders(argPlayer, "%img_bar_" + entry.getKey() + "_end_full%").charAt(0);

            String progressBarPic = ProgressBar.getProgressBar(
                    Double.parseDouble(progress),
                    Double.parseDouble(maxProgress),
                    barEmptyChar,
                    endEmptyChar,
                    barHalfChar,
                    endHalfChar,
                    barFullChar,
                    endFullChar,
                    10
            );
            progressBar.append(progressBarPic);
            i++;

        }

        return progressBar.toString();
    }

    public ItemStack setSlotFiller(int customModelData) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();

        if (meta == null) return item;
        switch (customModelData) {
            case 6: meta.setDisplayName(ChatColor.WHITE + "Helmet Slot"); break;
            case 7: meta.setDisplayName(ChatColor.WHITE + "Chestplate Slot"); break;
            case 8: meta.setDisplayName(ChatColor.WHITE + "Leggings Slot"); break;
            case 9: meta.setDisplayName(ChatColor.WHITE + "Boots Slot"); break;
            case 10: meta.setDisplayName(ChatColor.WHITE + "Hand Slot"); break;
            case 11: meta.setDisplayName(ChatColor.WHITE + "Off-Hand Slot"); break;
        }

        meta.setCustomModelData(customModelData);
        item.setItemMeta(meta);

        return item;
    }
}
