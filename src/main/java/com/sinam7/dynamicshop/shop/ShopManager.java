package com.sinam7.dynamicshop.shop;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.sinam7.dynamicshop.DynamicShop.getEcon;

public class ShopManager {

    private static Long sequence = 0L; // TODO: 2023-05-09 save sequence info
    public static final Map<Long, Shop> shopList = new HashMap<>();

    public static Long createShop(String name, Location location) {
        Shop shop = new Shop(sequence++, location);
        shop.setName(name);
        shopList.put(shop.id, shop);
        return shop.id;
    }

    public static Shop getShop(Long shopId) {
        return shopList.get(shopId);
    }

    public static boolean isShopNotExist(Long shopId) {
        return !shopList.containsKey(shopId);
    }

    public static String[] addItem(long shopId, int buyPrice, int sellPrice, ItemStack itemStack) {
        Shop shop = getShop(shopId);
        shop.addItemEntry(itemStack, buyPrice, sellPrice);
        //noinspection StringOperationCanBeSimplified: Material.name() recommends to call toString()
        Component displayName = itemStack.getItemMeta().hasDisplayName()
                ? itemStack.getItemMeta().displayName() : Component.text(itemStack.getType().name().toString());
        assert displayName != null;
        return new String[]{((TextComponent) displayName).content(), shop.getName()};
    }

    // TODO: 2023-05-14 메시지 변경 
    public static void executeBuyProcess(Player player, ItemStack stock, Integer price) {
        double balance = getEcon().getBalance(player);
        if (balance < price) {
            player.sendMessage("아 돈이 없는데");
            return;
        }
        EconomyResponse res = getEcon().withdrawPlayer(player, price);
        HashMap<Integer, ItemStack> boughtButNotGiven = player.getInventory().addItem(stock);
        if (!boughtButNotGiven.isEmpty()) {
            player.sendMessage("어 인벤 꽉찼다");
            getEcon().depositPlayer(player, res.amount); // cancel payment for not given item
        }
        player.sendMessage("구매 완료!");
    }

    // TODO: 2023-05-14 메시지 변경 
    public static void executeSellProcess(Player player, ItemStack item, Integer price) {
        item.setAmount(Math.max(item.getAmount() - 1, 0));
        getEcon().depositPlayer(player, price);
        player.sendMessage("판매 완료!");
    }
}
