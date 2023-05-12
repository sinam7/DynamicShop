package com.sinam7.dynamicshop.event;

import com.sinam7.dynamicshop.gui.GuiHolder;
import com.sinam7.dynamicshop.shop.ShopManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

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
        ItemStack item = clickedInventory.getItem(event.getSlot());

//        Bukkit.getLogger().log(Level.INFO, "topInventory = {%s}, player = {%s}, holder = {%s}".formatted(topInventory, player.toString(), holder));

        if (holder instanceof GuiHolder) { // buy phase
            Long shopId = ((GuiHolder) holder).shopId();
            ItemStack stock = ShopManager.getShop(shopId).displayToStock(item);
            if (stock != null) { // ignore empty slot or separator clicked
                HashMap<Integer, ItemStack> boughtButNotGiven = player.getInventory().addItem(stock);
                if (!boughtButNotGiven.isEmpty()) {
                    player.sendMessage("어 인벤 꽉찼다");
                }
            }
        }

        else if (holder == player) { // sell phase
            if (item != null) {
                item.setAmount(Math.max(item.getAmount() - 1, 0));
            }
        }

        event.setCancelled(true);
    }
}
