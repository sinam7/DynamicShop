package com.sinam7.dynamicshop.shop;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Random;

// TODO: 2023-05-18 버킷 스케쥴링으로 자동 가격 변경
public class PriceChanger {

    private static final TextComponent shopUpdatedText = Component.text("Shop price is updated!", Style.style(TextColor.fromHexString("#ffff00")));

    private static double runGaussian() {
        Random random = new Random();
        return random.nextGaussian(0, 0.25); // Price change ratio of default price (fixed)
    }

    public static void changePrice(ItemEntry entry) {
        if (entry.canChangePrice()) entry.updatePrice(runGaussian());
    }

    public static void updateAllShopPrice() {
        List<Shop> shopList = ShopManager.shopList.values().stream().toList();
        for (Shop shop : shopList) {
            List<ItemEntry> entryList = shop.itemEntryMap.values().stream().toList();
            for (ItemEntry entry : entryList) {
                changePrice(entry);
            }
        }
    }

    public static void notifyUpdateShopPrice(CommandSender sender /*broadcast if sender is null*/) {
        updateAllShopPrice();

        if (sender == null) {
            Bukkit.getServer().broadcast(shopUpdatedText);
        } else {
            sender.sendMessage(shopUpdatedText);
        }
    }

}
