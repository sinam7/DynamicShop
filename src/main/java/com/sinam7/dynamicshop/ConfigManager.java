package com.sinam7.dynamicshop;

import com.sinam7.dynamicshop.shop.ItemEntry;
import com.sinam7.dynamicshop.shop.Shop;
import com.sinam7.dynamicshop.shop.ShopManager;
import com.sinam7.dynamicshop.villager.VillagerManager;
import org.bukkit.Location;
import org.bukkit.World;
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
        world: (world's name:String)
        location: [x, y, z, yaw] // pitch = 0
        npcuuid: (npc's uuid:String)
        entries:
          (slotId):
            stock: ItemStack()
            buyprice: Integer()
            sellPrice: Integer()
     */

    public static void loadConfig() {
        plugin.getLogger().log(Level.INFO, "Config load started...");
        plugin.reloadConfig();
        config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("shop"); // shop
        if (section == null) {
            plugin.getLogger().log(Level.INFO, "No shop section found!");
            return;
        }
        long sequence = 0L;
        Map<UUID, Long> uuidMap = new LinkedHashMap<>();
        for (String key : section.getKeys(false)) { // shop.0
            ConfigurationSection shopSection = config.getConfigurationSection("shop." + key);
            if (shopSection == null) continue;

            long shopId = Long.parseLong(key); // shopId
            sequence = Math.max(sequence, shopId);

            String name = shopSection.getString("name"); // shop.0.name
            if (name == null) continue;

            String worldName = shopSection.getString("world"); // shop.0.world
            List<Double> rawloc = shopSection.getDoubleList("location"); // shop.0.location
            String rawUUID = shopSection.getString("npcuuid"); // shop.0.npcuuid

            if (worldName == null || rawloc.isEmpty() || rawUUID == null) continue;

            World world = plugin.getServer().getWorld(worldName);
            Location location = new Location(world, rawloc.get(0), rawloc.get(1), rawloc.get(2), 0, rawloc.get(3).floatValue());
            UUID npcUUID = UUID.fromString(rawUUID);
            uuidMap.put(npcUUID, shopId);

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
                        default -> state = buyprice == 0; /*  if changePrice is not specified and buyprice is not 0(=buy enabled), return false */
                    }

                    if (stock == null || buyprice < 0 || sellprice < 0) continue;
                    ItemEntry itemEntry = new ItemEntry(stock, buyprice, sellprice, state
                    );
                    itemEntryMap.put(slotId, itemEntry);
                }
            }

            Shop shop = new Shop(shopId, name, location, itemEntryMap);
            ShopManager.addShopToList(shop);

        }
        ShopManager.setSequence(++sequence);

        VillagerManager.bindVillagerFromConfig(uuidMap);
        plugin.getLogger().log(Level.INFO, "Config successfully loaded!");
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

        Location loc = shop.getLocation();
        double[] coordinates = new double[]{loc.x(), loc.y(), loc.z(), loc.getYaw()};
        config.set("shop.%s.world".formatted(id), loc.getWorld().getName());
        config.set("shop.%s.location".formatted(id), coordinates);

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

    public static void updateShopLocationQuery(UUID villagerUUID, Long shopId) {
        Shop shop = ShopManager.getShop(shopId);
        Location loc = shop.getLocation();
        config.set("shop.%s.world".formatted(shopId), loc.getWorld().getName());

        double[] coordinates = new double[]{loc.x(), loc.y(), loc.z(), loc.getYaw()};
        config.set("shop.%s.location".formatted(shopId), coordinates);
        config.set("shop.%s.npcuuid".formatted(shopId), villagerUUID.toString());

        plugin.saveConfig();
    }
}
