package lt.tomexas.profiles;

import lt.tomexas.profiles.cmd.Commands;
import lt.tomexas.profiles.guis.InventoryUtils;
import lt.tomexas.profiles.listeners.PlayerInteractAtEntityListener;
import lt.tomexas.profiles.utils.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;
import org.ipvp.canvas.MenuFunctionListener;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static ConfigHandler configHandler;
    private static InventoryUtils invUtils;

    @Override
    public void onEnable() {
        instance = this;
        configHandler = new ConfigHandler();
        invUtils = new InventoryUtils();

        configHandler.createConfig();
        configHandler.retrieveDataFromConfig();

        getServer().getPluginCommand("profile").setExecutor(new Commands());

        Bukkit.getPluginManager().registerEvents(new MenuFunctionListener(), this);

        getServer().getPluginManager().registerEvents(new PlayerInteractAtEntityListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return instance;
    }

    public static InventoryUtils getInvUtils() {
        return invUtils;
    }

    public static ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
