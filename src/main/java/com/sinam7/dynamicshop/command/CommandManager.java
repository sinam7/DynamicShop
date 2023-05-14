package com.sinam7.dynamicshop.command;

import com.sinam7.dynamicshop.gui.GuiManager;
import com.sinam7.dynamicshop.message.ShopMessage;
import com.sinam7.dynamicshop.shop.ShopManager;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@SuppressWarnings("SameReturnValue")
public class CommandManager implements CommandExecutor {

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

        // ds ( ) __
        switch (args[0]) {
            case "create" -> { // ds create [Shop name]
                return createShop(sender, args);
            }
            case "additem" -> { // ds additem (Shop id) (buyPrice) (sellPrice)
                return addItemToShop(sender, args);
            }
            case "open" -> { // ds open (Shop id)
                return openShopGui(sender, args);
            }
        }
        return false;
    }

    private static boolean addItemToShop(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length != 4) { // insufficient args given
            sender.sendMessage(ShopMessage.insufficientAddItemArguments(args.length));
            return true;
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
            return true;
        }

        if (ShopManager.isShopNotExist(shopId)) { // ShopId not exist
            sender.sendMessage(ShopMessage.invalidShopId(shopId));
            return true;
        }

        if (buyPrice < 0 || sellPrice < 0) { // Invalid price set
            sender.sendMessage(ShopMessage.invalidAddItemPriceInput(buyPrice, sellPrice));
            return true;
        }

        ItemStack itemStack = ((Player) sender).getInventory().getItemInMainHand().clone();
        if (itemStack.getType() == Material.AIR) { // Empty hand
            sender.sendMessage(ShopMessage.emptyHandAddItem());
            return true;
        }
        itemStack.setAmount(1); // only one item
        String[] result = ShopManager.addItem(shopId, buyPrice, sellPrice, itemStack);
        if (result.length != 2) return false; // something went wrong!

        // Success
        sender.sendMessage(ShopMessage.successAddItem(result[0], result[1], buyPrice, sellPrice));
        return true;
    }

    private static boolean openShopGui(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length != 2) { // insufficient args given
            sender.sendMessage(ShopMessage.insufficientOpenShopArguments(args.length));
            return true;
        }

        long shopId;
        try {
            shopId = Long.parseLong(args[1]);
        } catch (NumberFormatException e) { // ShopId convert failed
            sender.sendMessage(ShopMessage.invalidShopIdFormat(args[1]));
            return true;
        }

        if (ShopManager.isShopNotExist(shopId)) { // ShopId not exist
            sender.sendMessage(ShopMessage.invalidShopId(shopId));
            return true;
        }

        GuiManager.createGui((Player) sender, shopId);
        return true;
    }

    private static boolean createShop(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        String rawName = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ' ');
        String shopName = rawName;

        // if name is blank, set to default name and notify to sender
        if (rawName.isBlank()) {
            shopName = "Default Name";
            sender.sendMessage(ShopMessage.blankShopName());
        }

        Player player = (Player) sender;
        Location location = player.getLocation();
        Long shopId = ShopManager.createShop(shopName, location);
        /* TODO: 2023-05-14 Connect(bind) shop to villager
        VillagerManager.createVillager(shopName, location);
        */
        GuiManager.createGui(player, shopId);
        sender.sendMessage(ShopMessage.successCreateShop(shopId, shopName));

        return true;
    }


}
