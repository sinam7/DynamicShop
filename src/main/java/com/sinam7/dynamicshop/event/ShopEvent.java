package com.sinam7.dynamicshop.event;

import com.sinam7.dynamicshop.ConfigManager;
import com.sinam7.dynamicshop.gui.GuiHolder;
import com.sinam7.dynamicshop.gui.GuiManager;
import com.sinam7.dynamicshop.shop.ItemEntry;
import com.sinam7.dynamicshop.shop.PriceChanger;
import com.sinam7.dynamicshop.shop.Shop;
import com.sinam7.dynamicshop.shop.ShopManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.Objects;

public class ShopEvent implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return; // when click outside the inventory

        InventoryView view = event.getView();
        Inventory topInventory = view.getTopInventory();

        if (!(topInventory.getHolder() instanceof GuiHolder)) return; // when shop gui is not opened

        event.setCancelled(true); // Shop GUI confirmed

        Player player = (Player) view.getPlayer();
        if (((GuiHolder) topInventory.getHolder()).shopId() == -1) { // admin gui clicked
            adminClicked((long) event.getSlot(), player);
            return;
        }
        if (!clickedInventory.equals(topInventory)) return; // when shop gui is not clicked (player inventory clicked)


        Shop shop = ShopManager.getShop(((GuiHolder) Objects.requireNonNull(topInventory.getHolder())).shopId()); // holder is null-safe
        ItemEntry entry = shop.getEntry(event.getSlot());
        if (entry == null) return; // Non-Item clicked

        ClickType click = event.getClick();
        switch (click) {
            case LEFT -> ShopManager.executeBuyProcess(player, entry, entry.getCurrentBuyPrice(), 1);
            case SHIFT_LEFT -> ShopManager.executeBuyProcess(player, entry, entry.getCurrentBuyPrice(), 64);

            case RIGHT -> ShopManager.executeSellProcess(player, entry, entry.getCurrentSellPrice(), 1);
            case SHIFT_RIGHT -> ShopManager.executeSellProcess(player, entry, entry.getCurrentSellPrice(), 64);
        }
    }

    private void adminClicked(Long slot, Player player) {
        if (slot == GuiManager.getChangePriceIconLoc()) {
            PriceChanger.notifyUpdateShopPrice(player);
            return;
        }
        if (slot == GuiManager.getReloadConfigIconLoc()) {
            ConfigManager.notifyLoadConfig(player);
            return;
        }

        GuiManager.createShopGui(player, slot);

    }

}
