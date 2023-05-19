package com.sinam7.dynamicshop.command;

import com.sinam7.dynamicshop.ConfigManager;
import com.sinam7.dynamicshop.gui.GuiManager;
import com.sinam7.dynamicshop.message.ShopMessage;
import com.sinam7.dynamicshop.shop.PriceChanger;
import com.sinam7.dynamicshop.shop.Shop;
import com.sinam7.dynamicshop.shop.ShopManager;
import com.sinam7.dynamicshop.villager.VillagerManager;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

public class CommandManager implements CommandExecutor {

    private final JavaPlugin plugin;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) { // No console
            sender.sendMessage(ShopMessage.consoleCannotCommand());
            return true;
        }

        if (!sender.isOp()) { // Only Op
            sender.sendMessage(ShopMessage.opOnlyCommand());
            return true;
        }

        if (args.length == 0) { // blank call; /ds
            return false;
        }

        boolean flag = true;
        // ds ( ) __
        switch (args[0]) {

            case "create" -> createShop(sender, args); // ds create [Shop name]
            case "additem" -> addItemToShop(sender, args); // ds additem (Shop id) (buyPrice) (sellPrice)
            case "open" -> openShopGui(sender, args); // ds open (Shop id)
            case "npc" -> createNpc((Player) sender, args); // ds npc (Shop id)

            case "debug" -> { // ds debu.g (run)
                switch (args.length == 2 ? args[1] : "") {
                    case "price" -> PriceChanger.updateAllShopPrice();
                    case "reload" -> reloadConfig();
                    default -> flag = false;
                }
            }

            default -> flag = false;
        }
        return flag;
    }

    private void reloadConfig() {
        plugin.getLogger().log(Level.INFO, "Config load started...");
        plugin.reloadConfig();
        ConfigManager.loadConfig();
        plugin.getLogger().log(Level.INFO, "Config successfully loaded!");
    }

    private static void createNpc(Player sender, String[] args) {
        if (args.length != 2) { // insufficient args given
            sender.sendMessage(ShopMessage.insufficientNpcArguments(args.length));
            return;
        }

        long shopId;
        try {
            shopId = Long.parseLong(args[1]);
        } catch (NumberFormatException e) { // argument convert failed; first failed args will be notified
            sender.sendMessage(ShopMessage.invalidNpcArgumentFormat(args[1]));
            return;
        }

        Shop shop = ShopManager.getShop(shopId);
        UUID villagerUUID = VillagerManager.createVillager(shop.getName(), sender.getLocation());
        VillagerManager.bindVillagerToShop(villagerUUID, shopId);
        sender.sendMessage(ShopMessage.successCreateNpc(shopId, shop.getName()));
    }

    private static void addItemToShop(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length != 4) { // insufficient args given
            sender.sendMessage(ShopMessage.insufficientAddItemArguments(args.length));
            return;
        }

        int flag = 0;
        String[] argsName = new String[]{"shopId", "buyPrice", "sellPrice"};
        long shopId;
        int buyPrice, sellPrice;
        try {
            flag = 1;
            shopId = Long.parseLong(args[1]);
            flag = 2;
            buyPrice = Integer.parseInt(args[2]);
            flag = 3;
            sellPrice = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) { // argument convert failed; first failed args will be notified
            sender.sendMessage(ShopMessage.invalidAddItemArgumentFormat(argsName[flag], args[flag]));
            return;
        }

        if (ShopManager.isShopNotExist(shopId)) { // ShopId not exist
            sender.sendMessage(ShopMessage.invalidShopId(shopId));
            return;
        }

        if (buyPrice < 0 || sellPrice < 0) { // Invalid price set
            sender.sendMessage(ShopMessage.invalidAddItemPriceInput(buyPrice, sellPrice));
            return;
        }

        ItemStack itemStack = ((Player) sender).getInventory().getItemInMainHand().clone();
        if (itemStack.getType() == Material.AIR) { // Empty hand
            sender.sendMessage(ShopMessage.emptyHandAddItem());
            return;
        }
        itemStack.setAmount(1); // only one item
        String[] result = ShopManager.addItem(shopId, buyPrice, sellPrice, itemStack);
        if (result.length != 2) return; // something went wrong!

        // Success
        sender.sendMessage(ShopMessage.successAddItem(result[0], result[1], buyPrice, sellPrice));
    }

    private static void openShopGui(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length != 2) { // insufficient args given
            sender.sendMessage(ShopMessage.insufficientOpenShopArguments(args.length));
            return;
        }

        long shopId;
        try {
            shopId = Long.parseLong(args[1]);
        } catch (NumberFormatException e) { // ShopId convert failed
            sender.sendMessage(ShopMessage.invalidShopIdFormat(args[1]));
            return;
        }

        if (ShopManager.isShopNotExist(shopId)) { // ShopId not exist
            sender.sendMessage(ShopMessage.invalidShopId(shopId));
            return;
        }

        GuiManager.createGui((Player) sender, shopId);
    }

    private static void createShop(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        String rawName = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ' ');
        String shopName = rawName;

        // if name is blank, set to default name and notify to sender
        if (rawName.isBlank()) {
            shopName = "Default Name";
            sender.sendMessage(ShopMessage.blankShopName());
        }

        Player player = (Player) sender;
        Location location = player.getLocation();

        Long shopId = ShopManager.createShop(shopName);
        UUID villagerUUID = VillagerManager.createVillager(shopName, location);
        VillagerManager.bindVillagerToShop(villagerUUID, shopId);

        GuiManager.createGui(player, shopId);
        sender.sendMessage(ShopMessage.successCreateShop(shopId, shopName));

    }

}
