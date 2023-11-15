package lt.tomexas.profiles.utils;

import lt.tomexas.profiles.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfigHandler {

    private final Main main = Main.getInstance();
    private static YamlConfiguration config;

    private final HashMap<Integer, HashMap<List<String>, String>> chatPunishments = new HashMap<>();
    private final HashMap<Integer, HashMap<List<String>, String>> behaviorPunishments = new HashMap<>();
    private final HashMap<Integer, HashMap<List<String>, String>> modPunishments = new HashMap<>();
    private final HashMap<Enum<?>, List<String>> guiTexts = new HashMap<>();
    private final HashMap<Enum<?>, String> guiCommands = new HashMap<>();
    private final LinkedHashMap<String, LinkedHashMap<Enum<?>, String>> guiProgressBars = new LinkedHashMap<>();

    public enum guiTextEnum {
        IS_HOME_BUTTON,
        IS_VISIT_BUTTON,
        FRIEND_LIST_BUTTON,
        PLAYER_VAULT_BUTTON,
        DUEL_BUTTON,
        DISCORD_BUTTON,
        DISCORD_NOT_LINKED_BUTTON,
        PUNISH_BUTTON,
        INVSEE_BUTTON,
        PLAYER_INFO_BUTTON,
        TELEPORT_BUTTON,
        CHAT_TAB,
        BEHAVIOR_TAB,
        MODS_TAB,
        NO_RULE_CREATED
    }

    public enum guiProgressBarEnum {
        CURRENT_XP,
        REQUIRED_XP
    }

    public enum guiCommandsEnum{
        IS_VISIT,
        IS_HOME,
        TP,
        TPHERE,
        TPO,
        TPOHERE,
        INVSEE
    }

    public void createConfig() {
        File configFile = new File(this.main.getDataFolder(), "config.yml");
        if (!configFile.exists())
            this.main.saveResource("config.yml", false);

        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void retrieveDataFromConfig() {
        for (guiTextEnum enumKey : guiTextEnum.values()) {
            for (String key : config.getConfigurationSection("gui").getKeys(false)) {
                if (!enumKey.name().equalsIgnoreCase(key)) continue;
                if (config.isString("gui." + key)) {
                    guiTexts.put(enumKey, Collections.singletonList(config.getString("gui." + key)));
                } else {
                    guiTexts.put(enumKey, config.getStringList("gui." + key));
                }
            }
        }

        for (guiCommandsEnum enumKey : guiCommandsEnum.values()) {
            for (String key : config.getConfigurationSection("commands").getKeys(false)) {
                if (!enumKey.name().equalsIgnoreCase(key)) continue;
                guiCommands.put(enumKey, config.getString("commands." + key));
            }
        }

        for (String key : config.getConfigurationSection("progress_bars").getKeys(false)) {
            ConfigurationSection block = config.getConfigurationSection("progress_bars." + key);
            LinkedHashMap<Enum<?>, String> tempMap = new LinkedHashMap<>();
            tempMap.put(guiProgressBarEnum.CURRENT_XP, block.getString("current_xp"));
            tempMap.put(guiProgressBarEnum.REQUIRED_XP, block.getString("required_xp"));
            guiProgressBars.put(key, tempMap);
        }

        for (String key : config.getConfigurationSection("punishment_tabs.chat").getKeys(false)) {
            ConfigurationSection block = config.getConfigurationSection("punishment_tabs.chat." + key);
            if (key.equalsIgnoreCase("button_text")) guiTexts.put(guiTextEnum.CHAT_TAB, config.getStringList("punishment_tabs.chat.button_text"));
            else {
                HashMap<List<String>, String> tempMap = new HashMap<>();
                tempMap.put(block.getStringList("text"), block.getString("command"));
                chatPunishments.put(Integer.parseInt(key), tempMap);
            }
        }
        for (String key : config.getConfigurationSection("punishment_tabs.behavior").getKeys(false)) {
            ConfigurationSection block = config.getConfigurationSection("punishment_tabs.behavior." + key);
            if (key.equalsIgnoreCase("button_text")) guiTexts.put(guiTextEnum.BEHAVIOR_TAB, config.getStringList("punishment_tabs.behavior.button_text"));
            else {
                HashMap<List<String>, String> tempMap = new HashMap<>();
                tempMap.put(block.getStringList("text"), block.getString("command"));
                behaviorPunishments.put(Integer.parseInt(key), tempMap);
            }
        }
        for (String key : config.getConfigurationSection("punishment_tabs.mods").getKeys(false)) {
            ConfigurationSection block = config.getConfigurationSection("punishment_tabs.mods." + key);
            if (key.equalsIgnoreCase("button_text")) guiTexts.put(guiTextEnum.MODS_TAB, config.getStringList("punishment_tabs.mods.button_text"));
            else {
                HashMap<List<String>, String> tempMap = new HashMap<>();
                tempMap.put(block.getStringList("text"), block.getString("command"));
                modPunishments.put(Integer.parseInt(key), tempMap);
            }
        }
    }

    public HashMap<Integer, HashMap<List<String>, String>> getChatPunishments() {
        return chatPunishments;
    }

    public HashMap<Integer, HashMap<List<String>, String>> getBehaviorPunishments() {
        return behaviorPunishments;
    }

    public HashMap<Integer, HashMap<List<String>, String>> getModPunishments() {
        return modPunishments;
    }

    public HashMap<Enum<?>, List<String>> getGuiTexts() {
        return guiTexts;
    }

    public HashMap<Enum<?>, String> getGuiCommands() {
        return guiCommands;
    }

    public LinkedHashMap<String, LinkedHashMap<Enum<?>, String>> getGuiProgressBars() {
        return guiProgressBars;
    }
}
