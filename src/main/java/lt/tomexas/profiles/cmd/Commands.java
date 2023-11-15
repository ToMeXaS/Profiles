package lt.tomexas.profiles.cmd;

import lt.tomexas.profiles.guis.ProfileGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;

public class Commands implements CommandExecutor {

    private static final HashMap<HumanEntity, InventoryHolder> inventories = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("Usage: /profile <player_name>"); return false;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            player.sendMessage("That player is not online");
            return false;
        }
        Player argPlayer = Bukkit.getPlayer(args[0]);

        if (player.hasPermission("profiles.staff"))
            new ProfileGUI(player, argPlayer).displayMenu(player);
        else
            new ProfileGUI(player, argPlayer).displayMenu(player);

        /*Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), task -> {
            if (argPlayer.isOnline()) return;

            if (!(player.getOpenInventory().getTopInventory().equals(inventories.get(player)))) {
                player.sendMessage("TASK CANCELLED");
                task.cancel();
            }

            player.closeInventory();
            task.cancel();

            player.sendMessage(ChatColor.RED + "The player has logged off, therefore their profile was closed!");
        }, 0L, 20L);*/

        return false;
    }

    public static HashMap<HumanEntity, InventoryHolder> getInventories() {
        return inventories;
    }
}
