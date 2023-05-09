package com.sinam7.dynamicshop.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;

public class ShopMessage {

    public static TextComponent insufficientAddItemArguments(int arguments) {
        return Component.text(
                """
                        Argument must be 3, %s argument(s) found.
                        Usage: /ds additem (Shop id) (buyPrice) (sellPrice)
                        """.formatted(arguments - 1)
                , Style.style(TextColor.color(255, 0, 0))
        );
    }

    public static TextComponent blankShopName() {
        return Component.text(
                """
                        There is no shop name, so the shop name has been set to "Default Name" by default.
                        Usage: /ds create [Shop name]
                        """
                , Style.style(TextColor.color(255, 255, 0))
        );
    }

    public static TextComponent insufficientOpenShopArguments(int arguments) {
        return Component.text(
                """
                        Argument must be 1, %s argument(s) found.
                        Usage: /ds open (Shop id)
                        """.formatted(arguments - 1)
                , Style.style(TextColor.color(255, 0, 0))
        );
    }

    public static TextComponent invalidShopId(long wrongShopId) {
        return Component.text(
                """
                        The shop id "%s" is invalid. Check your Shop Id.
                        Usage: /ds open (Shop id)
                        """.formatted(wrongShopId)
                , Style.style(TextColor.color(255, 0, 0))
        );
    }

    public static TextComponent invalidShopIdFormat(Object args) {
        return Component.text(
                """
                        The shop id "%s" is invalid. Check your Shop Id.
                        Usage: /ds open (Shop id)
                        """.formatted(args)
                , Style.style(TextColor.color(255, 0, 0))
        );
    }
}
