package com.sinam7.dynamicshop.shop;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Shop {

    final Long id;
    String name;
    Location location;
    List<ItemEntry> itemEntryList;

    public Shop(long id, Location location) {
        this.id = id;
        this.location = location;
        itemEntryList = new ArrayList<>(45);
    }
    public void setName(String newName) {
        this.name = newName;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addItemEntry(ItemStack itemStack, Integer buyPrice, Integer sellPrice) {
        ItemEntry itemEntry = new ItemEntry(itemStack, buyPrice, sellPrice);
        itemEntryList.add(itemEntry);
    }

    public void removeItemEntry(ItemEntry itemEntry) {
        itemEntryList.remove(itemEntry);
    }

}
