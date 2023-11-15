package lt.tomexas.profiles.guis;

import lt.tomexas.profiles.Main;
import lt.tomexas.profiles.utils.ConfigHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.ipvp.canvas.type.ChestMenu;

import java.util.*;
import java.util.stream.Collectors;

public class PunishmentsGUI {

    private final Main main = Main.getInstance();
    private final InventoryUtils invUtils = Main.getInvUtils();
    private final ConfigHandler configHandler = Main.getConfigHandler();

    private ChestMenu menu;
    private final Player player;
    private final Player argPlayer;
    private String tab = "chat";

    public PunishmentsGUI(Player player, Player argPlayer) {
        this.player = player;
        this.argPlayer = argPlayer;

        updateInventory();
    }

    private void addGuiItems() {
        // Setting empty slots
        int modelData = 13;
        for (int i = 9; i < 18; i++) {
            this.menu.getSlot(i).setItem(this.invUtils.getItemStack(this.player, Material.PAPER, modelData, this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.NO_RULE_CREATED).get(0), null));
            modelData += 2;
        }

        switch (this.tab) {
            case "chat": {
                this.tab = "chat";
                for (int i = 3; i <= 5; i++) {
                    this.menu.getSlot(i).setItem(this.invUtils.getItemStack(this.player, Material.PAPER, 1, this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.BEHAVIOR_TAB).get(0), this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.BEHAVIOR_TAB).stream().skip(1).collect(Collectors.toList())));
                }
                for (int i = 6; i <= 8; i++) {
                    this.menu.getSlot(i).setItem(this.invUtils.getItemStack(this.player, Material.PAPER, 1, this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.MODS_TAB).get(0), this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.MODS_TAB).stream().skip(1).collect(Collectors.toList())));
                }
                setRuleSlots(this.configHandler.getChatPunishments(), this.player);

                break;
            }
            case "behavior": {
                this.tab = "behavior";
                for (int i = 0; i <= 2; i++) {
                    this.menu.getSlot(i).setItem(this.invUtils.getItemStack(this.player, Material.PAPER, 1, this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.CHAT_TAB).get(0), this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.CHAT_TAB).stream().skip(1).collect(Collectors.toList())));
                }
                for (int i = 6; i <= 8; i++) {
                    this.menu.getSlot(i).setItem(this.invUtils.getItemStack(this.player, Material.PAPER, 1, this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.MODS_TAB).get(0), this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.MODS_TAB).stream().skip(1).collect(Collectors.toList())));
                }
                setRuleSlots(this.configHandler.getBehaviorPunishments(), this.player);
                break;
            }
            case "mods": {
                this.tab = "mods";
                for (int i = 0; i <= 2; i++) {
                    this.menu.getSlot(i).setItem(invUtils.getItemStack(this.player, Material.PAPER, 1, this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.CHAT_TAB).get(0), this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.CHAT_TAB).stream().skip(1).collect(Collectors.toList())));
                }
                for (int i = 3; i <= 5; i++) {
                    this.menu.getSlot(i).setItem(invUtils.getItemStack(this.player, Material.PAPER, 1, this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.BEHAVIOR_TAB).get(0), this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.BEHAVIOR_TAB).stream().skip(1).collect(Collectors.toList())));
                }
                setRuleSlots(this.configHandler.getModPunishments(), this.player);
                break;
            }
        }

        addClickHandlers();
    }

    private void setRuleSlots(HashMap<Integer, HashMap<List<String>, String>> punishments, Player player) {
        int modelData = 12;
        for (int i = 9; i < punishments.size()+9; i++) {
            HashMap<List<String>, String> tempMap = punishments.get(i-8);
            for (Map.Entry<List<String>, String> map : tempMap.entrySet()) {
                if (map.getValue().isEmpty()) continue;
                this.menu.getSlot(i).setItem(this.invUtils.getItemStack(player, Material.PAPER, modelData, map.getKey().get(0), map.getKey().stream().skip(1).collect(Collectors.toList())));
            }
            modelData += 2;
        }
    }

    private void addClickHandlers() {
        // CHAT TAB
        for (int i = 0; i <= 2; i++) {
            this.menu.getSlot(i).setClickHandler((player, info) -> {
                this.tab = "chat";
                updateInventory();
            });
        }
        // BEHAVIOR TAB
        for (int i = 3; i <= 5; i++) {
            this.menu.getSlot(i).setClickHandler((player, info) -> {
                this.tab = "behavior";
                updateInventory();
            });
        }
        // MODS TAB
        for (int i = 6; i <= 8; i++) {
            this.menu.getSlot(i).setClickHandler((player, info) -> {
                this.tab = "mods";
                updateInventory();
            });
        }

        switch (this.tab) {
            case "chat":
                processPunishmentButtons(this.configHandler.getChatPunishments());
                break;
            case "behavior":
                processPunishmentButtons(this.configHandler.getBehaviorPunishments());
                break;
            case "mods":
                processPunishmentButtons(this.configHandler.getModPunishments());
                break;
        }
    }

    private void processPunishmentButtons(Map<Integer, HashMap<List<String>, String>> punishments) {
        for (Map.Entry<Integer, HashMap<List<String>, String>> entry : punishments.entrySet()) {
            HashMap<List<String>, String> map = entry.getValue();
            for (Map.Entry<List<String>, String> set : map.entrySet()) {
                if (set.getValue().isEmpty()) continue;
                this.menu.getSlot(entry.getKey() + 8).setClickHandler((player, info) -> {
                    player.closeInventory();
                    Bukkit.dispatchCommand(player, set.getValue());
                });
            }
        }
    }

    private void updateInventory() {
        buildMenu();
        displayMenu(this.player);
    }

    private void buildMenu() {
        String title = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(this.player, "&f%img_offset_-8%%img_punish_" + this.tab + "%"));
        this.menu = ChestMenu.builder(2)
                .title(title)
                .build();
        addGuiItems();
    }

    public void displayMenu(Player player) {
        this.menu.open(player);
    }
}
