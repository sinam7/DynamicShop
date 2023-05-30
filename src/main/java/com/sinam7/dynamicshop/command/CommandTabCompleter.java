package com.sinam7.dynamicshop.command;

import com.sinam7.dynamicshop.shop.ShopManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CommandTabCompleter implements TabCompleter {

    final String[] commands = new String[]{"create", "open", "additem", "npc", "debug"};
    final String[] debugCommands = new String[]{"price", "reload", "resetupdateperiod"};
    final List<String> emptyList = new ArrayList<>();

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1) {
            return List.of(commands);
        }

        if (args.length == 2) {
            switch (args[0]) {
                case "open", "additem", "npc" -> {
                    IntStream range = IntStream.range(0, ShopManager.shopList.size());
                    List<String> list = new ArrayList<>();
                    range.forEach(i -> list.add(String.valueOf(i)));
                    return list;
                }
                case "debug" -> {
                    return List.of(debugCommands);
                }
            }
        }
        return emptyList;
    }
}
