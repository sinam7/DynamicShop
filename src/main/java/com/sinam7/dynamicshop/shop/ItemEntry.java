package com.sinam7.dynamicshop.shop;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemEntry {

    private final ItemStack displayItem;
    private final Integer buyPrice;
    private final Integer sellPrice;
    private final ItemStack stock;

    public ItemEntry(ItemStack itemStack, Integer buyPrice, Integer sellPrice) {
        this.displayItem = itemStack.clone();
        this.stock = itemStack;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;

        applyPriceInfoToLore(this.displayItem, buyPrice, sellPrice);
    }

    private void applyPriceInfoToLore(ItemStack itemStack, Integer buyPrice, Integer sellPrice) {
        ItemMeta meta = itemStack.getItemMeta(); // get itemMeta

        // create text
        ArrayList<Component> lore = new ArrayList<>();
        Style textStyle = Style.style(TextColor.fromHexString("#31cade"));
        TextComponent buyPrice1 = Component.text("Buy Price: ").style(textStyle);
        TextComponent sellPrice1 = Component.text("Sell Price: ").style(textStyle);
        TextComponent buyStack1 = Component.text("Buy Stack (x64): ").style(textStyle);
        TextComponent sellStack1 = Component.text("Sell Stack (x64): ").style(textStyle);
        Style valueStyle = Style.style(TextColor.fromHexString("#00ff51"));
        TextComponent buyPrice2 = Component.text(buyPrice).style(valueStyle);
        TextComponent sellPrice2 = Component.text(sellPrice).style(valueStyle);
        TextComponent buyStack2 = Component.text(buyPrice * 64).style(valueStyle);
        TextComponent sellStack2 = Component.text(sellPrice * 64).style(valueStyle);
        Style disableStyle = Style.style(TextColor.fromHexString("#78807a"));
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
    }

    public boolean isBuyAble() {
        return buyPrice != null && buyPrice > 0;
    }

    public boolean isSellable() {
        return sellPrice != null && sellPrice > 0;
    }

}
