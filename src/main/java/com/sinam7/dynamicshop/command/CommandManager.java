package com.sinam7.dynamicshop.command;

import com.sinam7.dynamicshop.message.ShopMessage;
import com.sinam7.dynamicshop.shop.ShopManager;
import com.sinam7.dynamicshop.villager.VillagerManager;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args[0]) {
            case "create" -> { // ds create [Shop name]
                return createShop(sender, args);
            }
            case "additem" -> { // ds additem (Shop id) (buyPrice) (sellPrice)
                if (args.length != 3) { // insufficient args given
                    sender.sendMessage(ShopMessage.insufficientAddItemArguments(args.length));
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean createShop(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        String rawName = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ' ');
        String shopName = rawName;

        // if name is blank, set to default name and notify to sender
        if (rawName.isBlank()) {
            shopName = "Default Name";
            sender.sendMessage(ShopMessage.blankShopName());
        }

        Location location = ((Player) sender).getLocation();
        ShopManager.createShop(shopName, location);
        VillagerManager.createVillager(shopName, location);
        return true;
    }


}
