package com.sinam7.dynamicshop.shop;

import com.sinam7.dynamicshop.ConfigManager;
import com.sinam7.dynamicshop.message.ShopMessage;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.sinam7.dynamicshop.DynamicShop.getEcon;

public class ShopManager {

    @Setter
    private static Long sequence = 0L;
    public static final Map<Long, Shop> shopList = new HashMap<>();

    public static Long createShop(String name) {
        Shop shop = new Shop(sequence++);
        shop.setName(name);
        shopList.put(shop.id, shop);
        ConfigManager.addShop(shop);
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

        ConfigManager.updateShopItemQuery(shopId);
        return new String[]{((TextComponent) displayName).content(), shop.getName()};
    }

    public static void executeBuyProcess(Player player, ItemEntry entry, Integer price, int amount) {
        if (!entry.isBuyAble()) { // not buyable
            player.sendMessage(ShopMessage.buyDisabled());
            return;
        }

        double balance = getEcon().getBalance(player);
        if (balance < price * amount) { // not enough money
            player.sendMessage(ShopMessage.notEnoughMoneyBuyProcess(price * amount, balance));
            return;
        }

        EconomyResponse res = getEcon().withdrawPlayer(player, price * amount);

        HashMap<Integer, ItemStack> boughtButNotGiven = player.getInventory().addItem(entry.getStock().asQuantity(amount));
        if (!boughtButNotGiven.isEmpty()) { // not enough inventory slot
            EconomyResponse refund = getEcon().depositPlayer(player, res.amount); // cancel payment for not given item
            player.sendMessage(ShopMessage.notEnoughSlotBuyProcess(res.amount, refund.balance));
            return;
        }
        player.sendMessage(ShopMessage.successBuyProcess(amount, res.amount, res.balance));
    }

    public static void executeSellProcess(Player player, ItemEntry entry, Integer price, int amount) {
        if (!entry.isSellable()) { // not sellable
            player.sendMessage(ShopMessage.sellDisabled());
            return;
        }

        if (amount != 1) {
            // get amount of item which is equals to entry.getStock() in player's inventory
            int amountInInventory = 0;
            for (ItemStack itemStack : player.getInventory().getContents()) {
                if (itemStack != null && itemStack.isSimilar(entry.getStock())) {
                    amountInInventory += itemStack.getAmount();
                    if (amountInInventory > itemStack.getMaxStackSize()) {
                        amountInInventory = itemStack.getMaxStackSize();
                        break;
                    }
                }
            }
            amount = amountInInventory;

            if (amountInInventory < 1) { // not enough item to sell
                player.sendMessage(ShopMessage.notEnoughItemSellProcess());
                return;
            }
        } else {
            if (!player.getInventory().containsAtLeast(entry.getStock(), amount)) { // not enough item to sell
                player.sendMessage(ShopMessage.notEnoughItemSellProcess());
                return;
            }
        }

        player.getInventory().removeItem(entry.getStock().asQuantity(amount));
        EconomyResponse res = getEcon().depositPlayer(player, price * amount);
        player.sendMessage(ShopMessage.successSellProcess(amount, res.amount, res.balance));
    }

    public static void addShopToList(Shop shop) {
        shopList.put(shop.getId(), shop);
    }
}
