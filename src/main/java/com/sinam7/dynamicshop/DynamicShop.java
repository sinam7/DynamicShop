package com.sinam7.dynamicshop;

import com.sinam7.dynamicshop.command.CommandManager;
import com.sinam7.dynamicshop.command.CommandTabCompleter;
import com.sinam7.dynamicshop.event.ShopEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DynamicShop extends JavaPlugin implements Listener {

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new ShopEvent(), this);
        this.getCommand("ds").setExecutor(new CommandManager());
        this.getCommand("ds").setTabCompleter(new CommandTabCompleter());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(Component.text("Hello, " + event.getPlayer().getName() + "!"));
    }

}