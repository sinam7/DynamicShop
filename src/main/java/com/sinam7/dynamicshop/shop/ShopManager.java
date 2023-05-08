package com.sinam7.dynamicshop.shop;

import org.bukkit.Location;

public class ShopManager {

    static Long sequence = 0L; // TODO: 2023-05-09 save sequence info

    public static void createShop(String name, Location location) {
        Shop shop = new Shop(sequence++, location);
        shop.setName(name);

    }

}
