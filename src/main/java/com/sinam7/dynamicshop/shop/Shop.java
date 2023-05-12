package com.sinam7.dynamicshop.shop;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class Shop {

    final Long id;
    String name;
    Location location;

    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    Map<ItemStack, ItemEntry> displayToEntryMap;

    public Shop(long id, Location location) {
        this.id = id;
        this.location = location;
        displayToEntryMap = new LinkedHashMap<>(36);
    }
    public void addItemEntry(ItemStack itemStack, Integer buyPrice, Integer sellPrice) {
        ItemEntry itemEntry = new ItemEntry(itemStack, buyPrice, sellPrice);
        displayToEntryMap.put(itemEntry.getDisplayItem(), itemEntry);
    }

    public ItemStack displayToStock(ItemStack displayItem) {
        ItemEntry itemEntry = displayToEntryMap.get(displayItem);
        return itemEntry != null ? itemEntry.getStock() : null;
    }

    @SuppressWarnings("unused")
    public void removeItemEntry(ItemEntry itemEntry) {
        displayToEntryMap.remove(itemEntry.getDisplayItem());
    }

    public ItemStack[] getItemStacks() {
        ArrayList<ItemStack> itemStacks = new ArrayList<>(displayToEntryMap.size());
        displayToEntryMap.values().forEach(entry -> itemStacks.add(entry.getDisplayItem()));
        return itemStacks.toArray(new ItemStack[0]);
    }

}
