package com.sinam7.dynamicshop.event;

import com.sinam7.dynamicshop.gui.GuiHolder;
import com.sinam7.dynamicshop.shop.ItemEntry;
import com.sinam7.dynamicshop.shop.Shop;
import com.sinam7.dynamicshop.shop.ShopManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class ShopEvent implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return; // when click outside the inventory

        InventoryView view = event.getView();
        Inventory topInventory = view.getTopInventory();
        if (!(topInventory.getHolder() instanceof GuiHolder)) return; // when shop gui is not opened

        Player player = (Player) view.getPlayer();
        InventoryHolder holder = clickedInventory.getHolder();
        if (holder == null) return; // when gui is not related to this plugin; this plugin's holder is GuiHolder

//        Bukkit.getLogger().log(Level.INFO, "topInventory = {%s}, player = {%s}, holder = {%s}".formatted(topInventory, player.toString(), holder));
        Shop shop = ShopManager.getShop(((GuiHolder) topInventory.getHolder()).shopId());
        ItemStack item = clickedInventory.getItem(event.getSlot());
        if (clickedInventory.equals(topInventory)) { // buy phase
            ItemEntry entry = shop.getEntryFromDisplay(item);
            if (entry != null) { // ignore empty slot or separator clicked
                ShopManager.executeBuyProcess(player, entry.getStock(), entry.getBuyPrice());
            }
        } else { // sell phase
            if (item != null) { // ignore empty slot clicked
                ItemEntry entry = shop.getEntryFromStock(item);
                ShopManager.executeSellProcess(player, item, entry.getSellPrice());
            }
        }

        event.setCancelled(true);
    }
}
