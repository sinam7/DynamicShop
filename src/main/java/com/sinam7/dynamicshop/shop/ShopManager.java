package com.sinam7.dynamicshop.shop;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class ShopManager {

    private static Long sequence = 0L; // TODO: 2023-05-09 save sequence info
    public static Map<Long, Shop> shopList = new HashMap<>();

    public static Long createShop(String name, Location location) {
        Shop shop = new Shop(sequence++, location);
        shop.setName(name);
        shopList.put(shop.id, shop);
        return shop.id;
    }

    public static Shop getShop(Long shopId) {
        return shopList.get(shopId);
    }

    public static boolean isShopExist(Long shopId) {
        return shopList.containsKey(shopId);
    }

}
