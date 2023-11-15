package lt.tomexas.profiles.listeners;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerInteractAtEntityListener implements Listener {

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof HumanEntity)) return;
        if (!(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR))) return;

        event.getPlayer().performCommand("profile " + event.getRightClicked().getName());
    }
}
