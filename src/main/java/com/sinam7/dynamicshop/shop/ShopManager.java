package com.sinam7.dynamicshop.shop;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ShopManager {

    private static Long sequence = 0L; // TODO: 2023-05-09 save sequence info
    public static final Map<Long, Shop> shopList = new HashMap<>();

    public static Long createShop(String name, Location location) {
        Shop shop = new Shop(sequence++, location);
        shop.setName(name);
        shopList.put(shop.id, shop);
        return shop.id;
    }

    public static Shop getShop(Long shopId) {
        return shopList.get(shopId);
    }

    public static boolean isShopNotExist(Long shopId) {
        return !shopList.containsKey(shopId);
    }

    public static String[] addItem(long shopId, int buyPrice, int sellPrice, ItemStack itemStack) {
        Shop shop = getShop(shopId);
        shop.addItemEntry(itemStack, buyPrice, sellPrice);
        //noinspection StringOperationCanBeSimplified: Material.name() recommends to call toString()
        Component displayName = itemStack.getItemMeta().hasDisplayName()
                ? itemStack.getItemMeta().displayName() : Component.text(itemStack.getType().name().toString());
        assert displayName != null;
        return new String[]{((TextComponent) displayName).content(), shop.getName()};
    }
}
