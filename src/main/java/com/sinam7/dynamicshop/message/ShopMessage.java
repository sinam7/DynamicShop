package com.sinam7.dynamicshop.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;

public class ShopMessage {

    /*
        ds additem
     */
    public static TextComponent insufficientAddItemArguments(int arguments) {
        return Component.text(
                """
                        Argument must be 3, %s argument(s) found.
                        Usage: /ds additem (Shop id) (buyPrice) (sellPrice)""".formatted(arguments - 1)
                , Style.style(TextColor.fromHexString("#ff0000"))
        );
    }

    public static TextComponent invalidAddItemArgumentFormat(String args, String input) {
        return Component.text(
                """
                        The argument(s) "%s" is invalid. Check your input: %s.
                        Usage: /ds additem (Shop id) (buyPrice) (sellPrice)""".formatted(args, input)
                , Style.style(TextColor.fromHexString("#ff0000"))
        );
    }

    public static TextComponent invalidAddItemPriceInput(int buyPrice, int sellPrice) {
        return Component.text(
                """
                        Buy/Sell Price is must greater than 0. (Your buy/sell input: %s/%s)
                        If the price is 0, Buy/Sell option will be disabled.
                        Usage: /ds additem (Shop id) (buyPrice) (sellPrice)""".formatted(buyPrice, sellPrice)
                , Style.style(TextColor.fromHexString("#ff0000"))
        );
    }

    public static TextComponent emptyHandAddItem() {
        return Component.text(
                """
                        You should hold item to add to shop in your main hand."""
                , Style.style(TextColor.fromHexString("#ff0000"))
        );
    }

    public static TextComponent successAddItem(String itemDisplayName, String shopName, int buyPrice, int sellPrice) {
        return Component.text(
                """
                        Successfully added your offer(%s) to shop(%s)
                        Buy price = %s
                        Sell price = %s""".formatted(itemDisplayName, shopName,
                        buyPrice == 0 ? "Disabled" : buyPrice,
                        sellPrice == 0 ? "Disabled" : sellPrice)
                , Style.style(TextColor.fromHexString("#00ff00"))
        );
    }

    /*
        ds create
     */
    public static TextComponent successCreateShop(Long shopId, String shopName) {
        return Component.text(
                """
                        Successfully created Shop!
                        (Shop id: %s, Shop name: %s)""".formatted(shopId, shopName)
                , Style.style(TextColor.fromHexString("#00ff00"))
        );
    }

    public static TextComponent blankShopName() {
        return Component.text(
                """
                        There is no shop name, so the shop name has been set to "Default Name" by default.
                        Usage: /ds create [Shop name]"""
                , Style.style(TextColor.fromHexString("#ff8800"))
        );
    }

    /*
        ds open
     */
    public static TextComponent insufficientOpenShopArguments(int arguments) {
        return Component.text(
                """
                        Argument must be 1, %s argument(s) found.
                        Usage: /ds open (Shop id)""".formatted(arguments - 1)
                , Style.style(TextColor.fromHexString("#ff0000"))
        );
    }

    public static TextComponent invalidShopId(long wrongShopId) {
        return Component.text(
                """
                        The shop id "%s" is invalid. Check your Shop Id.
                        Usage: /ds open (Shop id)""".formatted(wrongShopId)
                , Style.style(TextColor.fromHexString("#ff0000"))
        );
    }

    public static TextComponent invalidShopIdFormat(Object args) {
        return Component.text(
                """
                        The shop id "%s" is invalid. Check your Shop Id.
                        Usage: /ds open (Shop id)""".formatted(args)
                , Style.style(TextColor.fromHexString("#ff0000"))
        );
    }

    /*
        Purchase
     */
    public static TextComponent successBuyProcess(double balance) {
        return Component.text(
                """
                        Item successfully bought!
                        your current balance: %s""".formatted(balance)
                , Style.style(TextColor.fromHexString("#00ff00"))
        );
    }

    public static TextComponent notEnoughSlotBuyProcess(double refund, double balance) {
        return Component.text(
                """
                        There is no slot available!
                        Your money has been refunded (%s refunded, your current balance: %s).""".formatted(refund, balance)
                , Style.style(TextColor.fromHexString("#ffff00"))
        );
    }

    public static TextComponent notEnoughMoneyBuyProcess(int price, double balance) {
        return Component.text(
                """
                        Not enough money!
                        (Price: %s, your current balance: %s).""".formatted(price, balance)
                , Style.style(TextColor.fromHexString("#ff8800"))
        );
    }

    public static TextComponent buyDisabled() {
        return Component.text(
                """
                        Buying this item is disabled!
                        """
                , Style.style(TextColor.fromHexString("#ff8800"))
        );
    }

    public static TextComponent successSellProcess(double balance) {
        return Component.text(
                """
                        Item successfully sold!
                        your current balance: %s""".formatted(balance)
                , Style.style(TextColor.fromHexString("#00ff00"))
        );
    }
    public static TextComponent sellDisabled() {
        return Component.text(
                """
                        Selling this item is disabled!
                        """
                , Style.style(TextColor.fromHexString("#ff8800"))
        );
    }

    /*
        extra
     */
    public static TextComponent consoleCannotCommand() {
        return Component.text(
                """
                        Unable to command from console. Only players can use it."""
                , Style.style(TextColor.fromHexString("#ff0000"))
        );
    }

    public static TextComponent opOnlyCommand() {
        return Component.text(
                """
                        You don't have a permission to command."""
                , Style.style(TextColor.fromHexString("#ff0000"))
        );
    }
}
