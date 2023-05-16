package com.sinam7.dynamicshop;

import com.sinam7.dynamicshop.shop.ItemEntry;
import com.sinam7.dynamicshop.shop.Shop;
import com.sinam7.dynamicshop.shop.ShopManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class ConfigManager {
    private static JavaPlugin plugin = null;
    private static FileConfiguration config;

    /*
    shop:
      (shopId):
        name: "shopName"
        npcData: Villager() todo 상점 - 주민 연결 정보를 어떻게 저장할지?
        location: [x, y, z, yaw] // pitch = 0
        entries:
          (slotId):
            stock: ItemStack()
            buyprice: Integer()
            sellPrice: Integer()
     */

    public static void init(JavaPlugin plugin, FileConfiguration config) {
        ConfigManager.plugin = plugin;
        ConfigManager.config = config;
    }

    public static void addShop(Shop shop) {
        Long id = shop.getId();
        String name = shop.getName();
        config.set("shop.%s.name".formatted(id), name);

        Location rawLoc = shop.getLocation();
        double[] location = new double[]{rawLoc.x(), rawLoc.y(), rawLoc.z(), rawLoc.getYaw()};
        config.set("shop.%s.location".formatted(id), location);

        plugin.saveConfig();
    }

    public static void updateShopItemQuery(Long shopId) {
        Shop shop = ShopManager.getShop(shopId);
        Map<Integer, ItemEntry> itemEntryMap = shop.getItemEntryMap();
        for (Integer slotId : itemEntryMap.keySet()) {
            config.set("shop.%s.entries.%s.stock".formatted(shopId, slotId), itemEntryMap.get(slotId).getStock());
            config.set("shop.%s.entries.%s.buyprice".formatted(shopId, slotId), itemEntryMap.get(slotId).getBuyPrice());
            config.set("shop.%s.entries.%s.sellprice".formatted(shopId, slotId), itemEntryMap.get(slotId).getSellPrice());
        }

        plugin.saveConfig();
    }
}
