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
}
