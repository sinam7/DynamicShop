package com.sinam7.dynamicshop.shop;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class ItemEntry {

    private ItemStack displayItem;
    private final ItemStack stock;
    private final Integer defaultBuyPrice;
    private final Integer defaultSellPrice;
    private final Boolean changePrice;

    @Getter(AccessLevel.NONE)
    private double ratio;
    private int currentBuyPrice;
    private int currentSellPrice;

    @Getter(AccessLevel.NONE)
    private int recentBuyPrice;
    @Getter(AccessLevel.NONE)
    private int recentSellPrice;

    private final Style textStyle = Style.style(TextColor.fromHexString("#35c965"));
    private final Style valueStyle = Style.style(TextColor.fromHexString("#ffff00"));
    private final Style disableStyle = Style.style(TextColor.fromHexString("#78807a"));
    private final Style priceUp = Style.style(TextColor.fromHexString("#ff0000"));
    private final Style priceDown = Style.style(TextColor.fromHexString("#0000ff"));
    private final Style priceEqual = Style.style(TextColor.fromHexString("#878787"));

    public ItemEntry(ItemStack itemStack, Integer defaultBuyPrice, Integer defaultSellPrice, Boolean changePrice) {
        this.displayItem = itemStack.clone();
        this.stock = itemStack;
        this.defaultBuyPrice = this.currentBuyPrice = this.recentBuyPrice = defaultBuyPrice;
        this.defaultSellPrice = this.currentSellPrice = this.recentSellPrice = defaultSellPrice;
        this.ratio = 0.0;
        this.changePrice = changePrice;
        applyPriceInfoToLore();
    }

    public ItemEntry(ItemStack itemStack, Integer defaultBuyPrice, Integer defaultSellPrice) {
        this(itemStack, defaultBuyPrice, defaultSellPrice, defaultBuyPrice == 0 /*if buy disabled return true*/) ;
    }

    public void updatePrice(double ratio) {
        this.ratio = ratio;

        if (isBuyAble()) {
            this.recentBuyPrice = currentBuyPrice;
            this.currentBuyPrice = Math.max((defaultBuyPrice + (int) (defaultBuyPrice * ratio)), 1);
        }

        if (isSellable()) {
            this.recentSellPrice = currentSellPrice;
            this.currentSellPrice = Math.max((defaultSellPrice + (int) (defaultSellPrice * ratio)), 1);
        }
        applyPriceInfoToLore();
    }

    private void applyPriceInfoToLore() {
        ItemStack itemStack = stock.clone();
        ItemMeta meta = itemStack.getItemMeta(); // get itemMeta from Stock

        ArrayList<Component> lore = new ArrayList<>();
        int maxStackSize = itemStack.getMaxStackSize();

        // create/update text
        TextComponent buyPrice1 = Component.text("Buy Price: ").style(textStyle);
        TextComponent buyStack1 = Component.text("Buy Stack (x%s): ".formatted(maxStackSize)).style(textStyle);
        TextComponent sellPrice1 = Component.text("Sell Price: ").style(textStyle);
        TextComponent sellStack1 = Component.text("Sell Stack (x%s): ".formatted(maxStackSize)).style(textStyle);

        TextComponent buyPrice2 = Component.text(currentBuyPrice).style(valueStyle).append(getChangedInfo(recentBuyPrice, currentBuyPrice));
        TextComponent buyStack2 = Component.text(currentBuyPrice * maxStackSize).style(valueStyle).append(getChangedInfo(recentBuyPrice * maxStackSize, currentBuyPrice * maxStackSize));
        TextComponent sellPrice2 = Component.text(currentSellPrice).style(valueStyle).append(getChangedInfo(recentSellPrice, currentSellPrice));
        TextComponent sellStack2 = Component.text(currentSellPrice * maxStackSize).style(valueStyle).append(getChangedInfo(recentSellPrice * maxStackSize, currentSellPrice * maxStackSize));

        TextComponent disabled = Component.text("Disabled").style(disableStyle);


        // text concatenation
        Component buyLore, buyStackLore = Component.text("");
        if (isBuyAble()) {
            buyLore = buyPrice1.append(buyPrice2);
            buyStackLore = buyStack1.append(buyStack2);
        } else buyLore = buyPrice1.append(disabled);
        lore.add(buyLore);
        lore.add(buyStackLore);

        Component sellLore, sellStackLore = Component.text("");
        if (isSellable()) {
            sellLore = sellPrice1.append(sellPrice2);
            sellStackLore = sellStack1.append(sellStack2);
        } else sellLore = sellPrice1.append(disabled);
        lore.add(sellLore);
        lore.add(sellStackLore);

        // Get existing lore and add new lore
        List<Component> result = meta.hasLore() ? meta.lore() : new ArrayList<>();
        assert result != null;
        result.addAll(lore);

        // apply
        meta.lore(result);
        itemStack.setItemMeta(meta);
        this.displayItem = itemStack;
    }

    private TextComponent getChangedInfo(int recent, int current) {
        TextComponent changeinfo;
        int abs = Math.abs(recent - current);

        if (recent < current) changeinfo = Component.text(" (▲%s)".formatted(abs)).style(priceUp); /* price up */
        else if (recent > current) changeinfo = Component.text(" (▼%s)".formatted(abs)).style(priceDown); /* price down */
        else changeinfo = Component.text(" (〓0)").style(priceEqual); /* equal */

        return changeinfo;
    }

    public boolean isBuyAble() {
        return defaultBuyPrice != null && defaultBuyPrice > 0;
    }

    public boolean isSellable() {
        return defaultSellPrice != null && defaultSellPrice > 0;
    }

    public boolean canChangePrice() {
        return changePrice;
    }

}
