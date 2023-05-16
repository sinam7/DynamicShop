package com.sinam7.dynamicshop.shop;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
@Setter
public class Shop {

    final Long id;
    String name;
    Location location;

    @Setter(AccessLevel.NONE)
    Map<Integer, ItemEntry> itemEntryMap;

    public Shop(long id, Location location) {
        this.id = id;
        this.location = location;
        itemEntryMap = new LinkedHashMap<>();
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

}
