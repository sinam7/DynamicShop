package com.sinam7.dynamicshop.villager;

import com.sinam7.dynamicshop.ConfigManager;
import com.sinam7.dynamicshop.message.ShopMessage;
import com.sinam7.dynamicshop.shop.Shop;
import com.sinam7.dynamicshop.shop.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.kyori.adventure.text.Component.text;

public class VillagerManager {

    private static final Map<UUID, Long> villagerToShopIdMap = new LinkedHashMap<>();
    private static final Map<UUID, Villager> villagerMap = new LinkedHashMap<>();

    public static @NotNull UUID createVillager(String name, Location loc) {
        loc.setPitch(0);
        Villager v = (Villager) Objects.requireNonNull(Bukkit.getServer().getWorld(loc.getWorld().getUID())).spawnEntity(loc, EntityType.VILLAGER);

        v.customName(text(name));
        v.setAI(false);
        v.setCollidable(false);
        v.setInvulnerable(true);
        v.setCustomNameVisible(true);
        v.setHealth(1);
        v.setPersistent(true);

        Random random = new Random();
        v.setProfession(Villager.Profession.values()[random.nextInt(0, 15)]);
        villagerMap.put(v.getUniqueId(), v);
        return v.getUniqueId();
    }

    public static void bindVillagerToShop(UUID villagerUUID, Long shopId) {
        villagerToShopIdMap.put(villagerUUID, shopId);
        ShopManager.getShop(shopId).addVillager(villagerUUID);
        ConfigManager.updateNpcUuid(shopId);
    }

    public static Villager getVillagerById(UUID id) {
        return villagerMap.get(id);
    }

    public static Long getShopIdByVillagerId(UUID id) {
        return villagerToShopIdMap.get(id);
    }

    public static void getVillagerFromConfig(Map<UUID, Long> uuidToShopMap) {
        for (World w : Bukkit.getServer().getWorlds()) {
            for (Entity e : w.getEntities()) {
                UUID uuid = e.getUniqueId();
                if (uuidToShopMap.containsKey(uuid)) {
                    villagerMap.put(uuid, (Villager) e);
                    bindVillagerToShop(uuid, uuidToShopMap.get(uuid));
                }
            }
        }
    }

    public static void removeNpc(UUID villagerUUID, Player executer) {
        if (villagerMap.get(villagerUUID) != null && villagerToShopIdMap.get(villagerUUID) != null) {
            villagerMap.remove(villagerUUID);
            Long shopId = villagerToShopIdMap.remove(villagerUUID);
            Shop shop = ShopManager.getShop(shopId);
            shop.removeVillager(villagerUUID);
            ConfigManager.updateNpcUuid(shopId);
            executer.sendMessage(ShopMessage.successRemoveNpc(shopId, shop.getName()));
        }
    }
}
