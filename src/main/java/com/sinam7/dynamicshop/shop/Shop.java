package com.sinam7.dynamicshop.shop;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
@Setter
public class Shop {

    final Long id;
    String name;
    Map<Integer, ItemEntry> itemEntryMap;
    List<UUID> villagerUUIDList;

    public Shop(long id) {
        this(id, null, new LinkedHashMap<>(), new ArrayList<>());
    }

    public Shop(Long id, String name, Map<Integer, ItemEntry> itemEntryMap, List<UUID> villagerUUIDList) {
        this.id = id;
        this.name = name;
        this.itemEntryMap = itemEntryMap;
        this.villagerUUIDList = villagerUUIDList;
    }

    public void addItemEntry(ItemStack itemStack, Integer buyPrice, Integer sellPrice) {
        ItemEntry itemEntry = new ItemEntry(itemStack, buyPrice, sellPrice);
        int size = itemEntryMap.size();
        itemEntryMap.put(size, itemEntry);
    }

    public ItemEntry getEntry(int slotId) {
        return itemEntryMap.get(slotId);
    }

    public Map<Integer, ItemStack> getDisplayItems() {
        Map<Integer, ItemStack> display = new LinkedHashMap<>(itemEntryMap.size());
        for (Integer slotId : itemEntryMap.keySet()) {
            display.put(slotId, itemEntryMap.get(slotId).getDisplayItem());
        }
        return display;
    }

    public void addVillager(UUID villagerUUID) {
        if (!villagerUUIDList.contains(villagerUUID)) villagerUUIDList.add(villagerUUID);
    }

    public void removeVillager(UUID villagerUUID) {
        villagerUUIDList.set(villagerUUIDList.indexOf(villagerUUID), null);
    }

}
