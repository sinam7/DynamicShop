package com.sinam7.dynamicshop;

import com.sinam7.dynamicshop.shop.ItemEntry;
import com.sinam7.dynamicshop.shop.Shop;
import com.sinam7.dynamicshop.shop.ShopManager;
import com.sinam7.dynamicshop.villager.VillagerManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;

public class ConfigManager {
    private static JavaPlugin plugin = null;
    private static FileConfiguration config;

    /*
    shop:
      (shopId):
        name: "shopName"
        npcuuid: [npc's uuid:String]
        entries:
          (slotId):
            stock: ItemStack()
            buyprice: Integer()
            sellPrice: Integer()
            changeprice: true/false/(or not: result of buyprice==0)
     */

    public static void loadConfig() {
        config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("shop"); // shop
        long sequence = -1L;
        if (section == null) {
            plugin.getLogger().log(Level.INFO, "No shop section found!");
            return;
        }
        Map<UUID, Long> uuidToShopIdMap = new LinkedHashMap<>();
        for (String key : section.getKeys(false)) { // shop.0
            ConfigurationSection shopSection = config.getConfigurationSection("shop." + key);
            if (shopSection == null) continue;

            long shopId = Long.parseLong(key); // shopId
            sequence = Math.max(sequence, shopId);

            String name = shopSection.getString("name"); // shop.0.name
            if (name == null) continue;

            List<UUID> npcList = new ArrayList<>();
            List<String> npcStringList = shopSection.getStringList("npc"); // shop.0.npc
            for (String s : npcStringList) {
                UUID uuid;
                try {
                    uuid = UUID.fromString(s);
                } catch (IllegalArgumentException ignored) {
                    uuid = null;
                } // not uuid is ignored
                if (uuid != null) {
                    npcList.add(uuid);
                    uuidToShopIdMap.put(UUID.fromString(s), shopId);
                }
            }

            ConfigurationSection entriesSection = shopSection.getConfigurationSection("entries"); // shop.0.entries
            Map<Integer, ItemEntry> itemEntryMap = new LinkedHashMap<>();
            if (entriesSection != null) {
                Set<String> slots = entriesSection.getKeys(false);
                for (String s : slots) { // shop.0.entries.0
                    int slotId = Integer.parseInt(s); // slotId
                    ConfigurationSection entry = entriesSection.getConfigurationSection(s);
                    if (entry == null) continue;

                    ItemStack stock = entry.getItemStack("stock"); // shop.0.entries.0.stock
                    int buyprice = entry.getInt("buyprice", 0); // shop.0.entries.0.buyprice
                    int sellprice = entry.getInt("sellprice", 0); // shop.0.entries.0.sellprice

                    String rawChangePrice = entry.getString("changeprice"); // shop.0.entries.0.changeprice
                    boolean state;
                    switch (rawChangePrice == null ? "null" : rawChangePrice.toLowerCase().strip()) {
                        case "true" -> state = true;
                        case "false" -> state = false;
                        default -> state = buyprice == 0;
                        /*  if changePrice is not specified and buyprice is not 0(=buy enabled), return false */
                    }

                    if (stock == null || buyprice < 0 || sellprice < 0) continue;
                    ItemEntry itemEntry = new ItemEntry(stock, buyprice, sellprice, state);
                    itemEntryMap.put(slotId, itemEntry);
                }
            }

            Shop shop = new Shop(shopId, name, itemEntryMap, npcList);
            ShopManager.addShopToList(shop);
            updateNpcUUIDToConfig(shopId);
        }
        ShopManager.setSequence(++sequence);

        VillagerManager.getVillagerFromConfig(uuidToShopIdMap);
    }

    public static void init(JavaPlugin plugin) {
        ConfigManager.plugin = plugin;
        config = plugin.getConfig();
        loadConfig();
    }

    public static void addShop(Shop shop) {
        Long id = shop.getId();
        String name = shop.getName();
        config.set("shop.%s.name".formatted(id), name);
        plugin.saveConfig();
    }

    public static void updateShopItemQuery(Long shopId) {
        Shop shop = ShopManager.getShop(shopId);
        Map<Integer, ItemEntry> itemEntryMap = shop.getItemEntryMap();
        for (Integer slotId : itemEntryMap.keySet()) {
            config.set("shop.%s.entries.%s.stock".formatted(shopId, slotId), itemEntryMap.get(slotId).getStock());
            config.set("shop.%s.entries.%s.buyprice".formatted(shopId, slotId), itemEntryMap.get(slotId).getDefaultBuyPrice());
            config.set("shop.%s.entries.%s.sellprice".formatted(shopId, slotId), itemEntryMap.get(slotId).getDefaultSellPrice());
            Boolean raw = itemEntryMap.get(slotId).getChangePrice();
            config.set("shop.%s.entries.%s.changeprice".formatted(shopId, slotId), raw == null ? itemEntryMap.get(slotId).getDefaultBuyPrice() == 0 : raw);
        }

        plugin.saveConfig();
    }

    public static void updateNpcUUIDToConfig(Long shopId) {
        Shop shop = ShopManager.getShop(shopId);
        List<UUID> villagerUUIDList = shop.getVillagerUUIDList();
        List<String> result = new ArrayList<>();
        for (UUID uuid : villagerUUIDList) {
            String string = uuid != null ? uuid.toString() : "REMOVED";
            result.add(string);
        }
        config.set("shop.%s.npc".formatted(shopId), result);
        plugin.saveConfig();
    }

    public static void loadNpc() {
        ConfigurationSection section = config.getConfigurationSection("shop");
        Map<UUID, Long> uuidToShopIdMap = new LinkedHashMap<>();
        if (section == null) return;

        for (String shopId : section.getKeys(false)) {
            ConfigurationSection shopSection = section.getConfigurationSection(shopId);
            if (shopSection == null) continue;

            List<String> npcStringList = shopSection.getStringList("npc"); // shop.0.npc
            for (String s : npcStringList) {
                UUID uuid;
                try {
                    uuid = UUID.fromString(s);
                } catch (IllegalArgumentException ignored) {
                    uuid = null; // not uuid is ignored
                }
                if (uuid == null) continue;
                uuidToShopIdMap.put(UUID.fromString(s), Long.valueOf(shopId));
            }
        }
        VillagerManager.getVillagerFromConfig(uuidToShopIdMap);

    }

}
