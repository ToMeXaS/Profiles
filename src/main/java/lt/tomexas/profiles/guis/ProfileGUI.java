package lt.tomexas.profiles.guis;

import lt.tomexas.profiles.Main;
import lt.tomexas.profiles.utils.ConfigHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.type.ChestMenu;

import java.util.stream.Collectors;

public class ProfileGUI {

    private final Main main = Main.getInstance();
    private final InventoryUtils invUtils = Main.getInvUtils();
    private final ConfigHandler configHandler = Main.getConfigHandler();

    private ChestMenu menu;
    private final Player player;
    private final Player argPlayer;
    private String cmd = "";


    public ProfileGUI(Player player, Player argPlayer) {
        this.player = player;
        this.argPlayer = argPlayer;

        buildMenu();
    }

    private void addGuiItems() {
        this.menu.getSlot(29).setItem(this.invUtils.getItemStack(this.argPlayer, Material.PLAYER_HEAD, 3, "&c",
                this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.PLAYER_INFO_BUTTON)));

        if (this.argPlayer.equals(this.player)) {
            this.menu.getSlot(27).setItem(this.invUtils.getItemStack(this.argPlayer, Material.PAPER, 1, this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.IS_HOME_BUTTON).get(0), null));
            this.menu.getSlot(28).setItem(this.invUtils.getItemStack(this.argPlayer, Material.PAPER, 1, this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.IS_HOME_BUTTON).get(0), null));
        } else {
            this.menu.getSlot(27).setItem(this.invUtils.getItemStack(this.argPlayer, Material.PAPER, 1, this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.IS_VISIT_BUTTON).get(0), null));
            this.menu.getSlot(28).setItem(this.invUtils.getItemStack(this.argPlayer, Material.PAPER, 1, this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.IS_VISIT_BUTTON).get(0), null));
        }

        this.menu.getSlot(30).setItem(this.invUtils.getItemStack(this.argPlayer, Material.PAPER, 1,this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.DISCORD_NOT_LINKED_BUTTON).get(0), null));
        this.menu.getSlot(31).setItem(this.invUtils.getItemStack(this.argPlayer, Material.PAPER, 1,this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.FRIEND_LIST_BUTTON).get(0), null));
        this.menu.getSlot(32).setItem(this.invUtils.getItemStack(this.argPlayer, Material.PAPER, 1,this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.PLAYER_VAULT_BUTTON).get(0), null));
        this.menu.getSlot(33).setItem(this.invUtils.getItemStack(this.argPlayer, Material.PAPER, 1,this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.DUEL_BUTTON).get(0), null));

        if (this.player.hasPermission("profiles.staff")) {
            for (int i = 36; i <= 38; i++)
                this.menu.getSlot(i).setItem(this.invUtils.getItemStack(this.argPlayer, Material.PAPER, 1, this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.PUNISH_BUTTON).get(0), null));

            if (this.player != this.argPlayer)
                for (int i = 39; i <= 41; i++)
                    this.menu.getSlot(i).setItem(this.invUtils.getItemStack(this.argPlayer, Material.PAPER, 1, this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.INVSEE_BUTTON).get(0), null));

            for (int i = 42; i <= 44; i++)
                this.menu.getSlot(i).setItem(this.invUtils.getItemStack(this.argPlayer, Material.PAPER, 1, this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.TELEPORT_BUTTON).get(0), this.configHandler.getGuiTexts().get(ConfigHandler.guiTextEnum.TELEPORT_BUTTON).stream().skip(1).collect(Collectors.toList())));
        }

        setEquipementSlots();
        setPlayerNameSlots();

        addClickHandlers();
    }

    private void addClickHandlers() {
        if (this.argPlayer == null) return;

        for (int i = 27; i <= 28; i++) {
            this.menu.getSlot(i).setClickHandler((player, info) -> {
                if (this.player.equals(this.argPlayer)) this.cmd = this.configHandler.getGuiCommands().get(ConfigHandler.guiCommandsEnum.IS_HOME);
                else this.cmd = this.configHandler.getGuiCommands().get(ConfigHandler.guiCommandsEnum.IS_VISIT);
                Bukkit.dispatchCommand(player, PlaceholderAPI.setPlaceholders(this.argPlayer, this.cmd));
            });
        }

        if (this.player.hasPermission("profiles.staff")) {
            for (int i = 42; i <= 44; i++) {
                this.menu.getSlot(i).setClickHandler((player, info) -> {
                    switch (info.getClickType()) {
                        case LEFT:
                            this.cmd = this.configHandler.getGuiCommands().get(ConfigHandler.guiCommandsEnum.TP);
                            break;
                        case RIGHT:
                            this.cmd = this.configHandler.getGuiCommands().get(ConfigHandler.guiCommandsEnum.TPHERE);
                            break;
                        case SHIFT_LEFT:
                            this.cmd = this.configHandler.getGuiCommands().get(ConfigHandler.guiCommandsEnum.TPO);
                            break;
                        case SHIFT_RIGHT:
                            this.cmd = this.configHandler.getGuiCommands().get(ConfigHandler.guiCommandsEnum.TPOHERE);
                            break;
                    }
                    Bukkit.dispatchCommand(player, PlaceholderAPI.setPlaceholders(this.argPlayer, this.cmd));
                });
            }

            for (int i = 39; i <= 41; i++) {
                this.menu.getSlot(i).setClickHandler((player, info) -> {
                    player.closeInventory();
                    this.cmd = this.configHandler.getGuiCommands().get(ConfigHandler.guiCommandsEnum.INVSEE);
                    Bukkit.dispatchCommand(player, PlaceholderAPI.setPlaceholders(this.argPlayer, this.cmd));
                });
            }

            for (int i = 36; i <= 38; i++) {
                this.menu.getSlot(i).setClickHandler((player, info) -> {
                    this.player.closeInventory();
                    new PunishmentsGUI(this.player, this.argPlayer).displayMenu(this.player);
                });
            }
        }
    }

    private void setEquipementSlots() {
        this.menu.getSlot(7).setItem((this.player.getInventory().getItem(EquipmentSlot.HEAD).getType() == Material.AIR) ? this.invUtils.setSlotFiller(6) : this.player.getInventory().getItem(EquipmentSlot.HEAD));
        this.menu.getSlot(16).setItem((this.player.getInventory().getItem(EquipmentSlot.CHEST).getType() == Material.AIR) ? this.invUtils.setSlotFiller(7) : this.player.getInventory().getItem(EquipmentSlot.CHEST));
        this.menu.getSlot(17).setItem((this.player.getInventory().getItem(EquipmentSlot.HAND).getType() == Material.AIR) ? this.invUtils.setSlotFiller(10) : this.player.getInventory().getItem(EquipmentSlot.HAND));
        this.menu.getSlot(25).setItem((this.player.getInventory().getItem(EquipmentSlot.LEGS).getType() == Material.AIR) ? this.invUtils.setSlotFiller(8) : this.player.getInventory().getItem(EquipmentSlot.LEGS));
        this.menu.getSlot(26).setItem((this.player.getInventory().getItem(EquipmentSlot.OFF_HAND).getType() == Material.AIR) ? this.invUtils.setSlotFiller(11) : this.player.getInventory().getItem(EquipmentSlot.OFF_HAND));
        this.menu.getSlot(34).setItem((this.player.getInventory().getItem(EquipmentSlot.FEET).getType() == Material.AIR) ? this.invUtils.setSlotFiller(9) : this.player.getInventory().getItem(EquipmentSlot.FEET));
    }

    private void setPlayerNameSlots() {
        Mask mask = BinaryMask.builder(menu)
                .item(this.invUtils.getItemStack(this.player, Material.PAPER, 1, ChatColor.WHITE + this.player.getName(), null))
                .pattern("111000000") // First row
                .pattern("111000000") // Second row
                .pattern("111000000").build(); // Third row
        mask.apply(menu);
    }

    private void buildMenu() {
        String title;
        if (this.player != this.argPlayer) {
            title = PlaceholderAPI.setPlaceholders(this.player, "%img_offset_-8%%img_profile_menu_" + (this.player.hasPermission("profiles.staff") ? "staff" : "") + "%" + this.invUtils.getPicBorder(this.argPlayer)) + this.invUtils.getProgressBars(this.argPlayer);
        } else {
            title = PlaceholderAPI.setPlaceholders(this.player, "%img_offset_-8%%img_profile_menu_" + (this.player.hasPermission("profiles.staff") ? "staff_self" : "") + "%" + this.invUtils.getPicBorder(this.argPlayer)) + this.invUtils.getProgressBars(this.argPlayer);
        }

        this.menu = ChestMenu.builder((this.player.hasPermission("profiles.staff") ? 5 : 4))
                .title(ChatColor.WHITE + "" + title)
                .build();

        addGuiItems();
    }

    public void displayMenu(Player player) {
        this.menu.open(player);
    }
}
