package com.sinam7.dynamicshop.shop;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public record ItemEntry(ItemStack itemStack, Integer buyPrice, Integer sellPrice) {

    public boolean isBuyAble() {
        return buyPrice != null && buyPrice > 0;
    }

    public boolean isSellable() {
        return sellPrice != null && sellPrice > 0;
    }

    public Component getDisplayName() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasDisplayName()) return itemMeta.displayName();
        else return Component.text("null");
    }

}
