package com.sinam7.dynamicshop.shop;

import java.util.List;
import java.util.Random;

// TODO: 2023-05-18 버킷 스케쥴링으로 자동 가격 변경
public class PriceChanger {

    private static double runGaussian() {
        Random random = new Random();
        return random.nextGaussian(0, 0.25); // Price change ratio of default price (fixed)
    }

    public static void changePrice(ItemEntry entry) {
        entry.updatePrice(runGaussian());
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

}
