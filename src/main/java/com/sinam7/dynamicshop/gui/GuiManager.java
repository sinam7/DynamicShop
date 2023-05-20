package com.sinam7.dynamicshop.gui;

import com.sinam7.dynamicshop.shop.ItemEntry;
import com.sinam7.dynamicshop.shop.Shop;
import com.sinam7.dynamicshop.shop.ShopManager;
import lombok.Getter;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class GuiManager {

    private static ItemStack separator;
    private static ItemStack nullshopicon;
    private static ItemStack changePriceIcon;
    private static ItemStack reloadIcon;

    private static final Style shopNameStyle = Style.style(TextColor.fromHexString("#00ff00"), TextDecoration.ITALIC.withState(false));
    private static final Style shopLoreStyle = Style.style(TextColor.fromHexString("#ffff00"), TextDecoration.ITALIC.withState(false));
    private static final Style noEntryStyle = Style.style(TextColor.fromHexString("#ff0000"), TextDecoration.ITALIC.withState(false));
    private static final Style separatorStyle = Style.style(TextColor.fromHexString("#ffffff"), TextDecoration.BOLD, TextDecoration.ITALIC.withState(false));

    @Getter
    private static final int changePriceIconLoc = 52;
    @Getter
    private static final int reloadConfigIconLoc = 53;


    public static void createShopGui(Player player, Long shopId) {
        Shop shop = ShopManager.getShop(shopId);

        Inventory inv = Bukkit.createInventory(new GuiHolder(shopId), 54, Component.text(shop.getName() + ":" + shopId));

        Map<Integer, ItemStack> displayItems = shop.getDisplayItems();
        for (Integer slotId : displayItems.keySet()) {
            inv.setItem(slotId, displayItems.get(slotId));
        }

        IntStream.range(36, 45).forEach(i -> inv.setItem(i, getSeparator()));
        player.openInventory(inv);
    }

    public static void createAdminGui(Player player) {
        Inventory inv = Bukkit.createInventory(new GuiHolder(-1), 54, Component.text("Admin GUI"));
        for (Long shopId : ShopManager.shopList.keySet()) {
            ItemStack shopIcon = getShopIcon(shopId);
            inv.setItem(Math.toIntExact(shopId), shopIcon);
        }

        inv.setItem(changePriceIconLoc, getChangePriceIcon());
        inv.setItem(reloadConfigIconLoc, getReloadIcon());
        player.openInventory(inv);
    }

    private static ItemStack getSeparator() {
        if (separator == null) {
            ItemStack ico = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
            ItemMeta itemMeta = ico.getItemMeta();
            itemMeta.displayName(Component.text("====================", separatorStyle));
            ico.setItemMeta(itemMeta);
            separator = ico;
        }
        return separator;
    }

    private static ItemStack getShopIcon(Long shopId) {
        Shop shop = ShopManager.getShop(shopId);
        ItemEntry entry = shop.getEntry(0);
        if (entry == null) {
            if (nullshopicon == null) {
                ItemStack ico = new ItemStack(Material.BARRIER);
                ItemMeta itemMeta = ico.getItemMeta();

                itemMeta.displayName(Component.text("%s:%s".formatted(shop.getName(), shopId), shopNameStyle));
                itemMeta.lore(new ArrayList<>(List.of(Component.text("No Entry", noEntryStyle))));
                ico.setItemMeta(itemMeta);
                nullshopicon = ico;
            }
            return nullshopicon;
        }

        ItemStack icon;
        icon = entry.getStock().clone();
        ItemMeta itemMeta = icon.getItemMeta();
        itemMeta.displayName(Component.text("%s:%s".formatted(shop.getName(), shopId), shopNameStyle));
        int size = shop.getItemEntryMap().size();
        itemMeta.lore(new ArrayList<>(List.of(Component.text((size + (size == 1 ? " entry" : " entries")), shopLoreStyle))));
        icon.setItemMeta(itemMeta);
        return icon;
    }

    private static ItemStack getChangePriceIcon() {
        if (changePriceIcon == null) {
            ItemStack ico = new ItemStack(Material.GOLD_INGOT);
            ItemMeta itemMeta = ico.getItemMeta();
            itemMeta.displayName(Component.text("Change All Item's Price", shopLoreStyle.decorate(TextDecoration.BOLD)));
            ico.setItemMeta(itemMeta);
            changePriceIcon = ico;
        }
        return changePriceIcon;
    }

    private static ItemStack getReloadIcon() {
        if (reloadIcon == null) {
            ItemStack ico = new ItemStack(Material.WRITTEN_BOOK);
            ItemMeta itemMeta = ico.getItemMeta();
            itemMeta.displayName(Component.text("Reload Config", noEntryStyle.decorate(TextDecoration.BOLD)));
            ico.setItemMeta(itemMeta);
            reloadIcon = ico;
        }
        return reloadIcon;
    }


}
