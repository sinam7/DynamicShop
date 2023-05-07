package com.sinam7.dynamicshop.command;

import com.sinam7.dynamicshop.villager.VillagerManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args[0]) {
            case "create" -> {
                VillagerManager.createVillager((Player) sender, args);
                return true;
            }
        }
        return false;
    }


}
