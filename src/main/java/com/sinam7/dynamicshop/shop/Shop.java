package com.sinam7.dynamicshop.shop;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Shop {

    final Long id;
    String name;
    Location location;

    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    List<ItemEntry> itemEntryList;

    public Shop(long id, Location location) {
        this.id = id;
        this.location = location;
        itemEntryList = new ArrayList<>(36);
    }
    public void addItemEntry(ItemStack itemStack, Integer buyPrice, Integer sellPrice) {
        ItemEntry itemEntry = new ItemEntry(itemStack, buyPrice, sellPrice);
        itemEntryList.add(itemEntry);
    }

    public void removeItemEntry(ItemEntry itemEntry) {
        itemEntryList.remove(itemEntry);
    }

    public ItemStack[] getItemStacks() {
        ItemStack[] itemStacks = new ItemStack[itemEntryList.size()];
        for (int i = 0; i < itemEntryList.size(); i++) {
            itemStacks[i] = itemEntryList.get(i).getDisplayItem();
        }
        return itemStacks;
    }

}
