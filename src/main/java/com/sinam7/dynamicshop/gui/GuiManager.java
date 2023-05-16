package com.sinam7.dynamicshop.gui;

import com.sinam7.dynamicshop.shop.Shop;
import com.sinam7.dynamicshop.shop.ShopManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.stream.IntStream;

public class GuiManager {

    private static ItemStack separator;

    public static void createGui(Player player, Long shopId) {
        Shop shop = ShopManager.getShop(shopId);

        Inventory inv = Bukkit.createInventory(new GuiHolder(shopId), 54, Component.text(shop.getName() + ":" + shopId));

        Map<Integer, ItemStack> displayItems = shop.getDisplayItems();
        for (Integer slotId : displayItems.keySet()) {
            inv.setItem(slotId, displayItems.get(slotId));
        }

        IntStream.range(36, 45).forEach(i -> inv.setItem(i, getSeparator()));
        player.openInventory(inv);
    }

    private static ItemStack getSeparator() {
        if (separator == null) {
            ItemStack sep = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            ItemMeta itemMeta = sep.getItemMeta();
            itemMeta.displayName(Component.text("====================", Style.style(TextColor.color(255, 255, 255), TextDecoration.BOLD)));
            sep.setItemMeta(itemMeta);
            separator = sep;
        }
        return separator;
    }


}
