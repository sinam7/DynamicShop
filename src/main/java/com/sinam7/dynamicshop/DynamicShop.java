package com.sinam7.dynamicshop;

import com.sinam7.dynamicshop.command.CommandManager;
import com.sinam7.dynamicshop.command.CommandTabCompleter;
import com.sinam7.dynamicshop.event.ShopEvent;
import com.sinam7.dynamicshop.event.VillagerEvent;
import com.sinam7.dynamicshop.shop.PriceChanger;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("DataFlowIssue")
public class DynamicShop extends JavaPlugin implements Listener {

    public static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    private BukkitTask updatePriceTask;

    public void resetUpdatePeriod() {
        updatePriceTask.cancel();
        updatePriceTask = Bukkit.getScheduler().runTaskTimer(this, () -> PriceChanger.notifyUpdateShopPrice(null), 0, ConfigManager.getUpdateperiod());
    }


    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getPlugin(this.getClass()).getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getPluginManager().registerEvents(new ShopEvent(), this);
        Bukkit.getPluginManager().registerEvents(new VillagerEvent(), this);
        this.getCommand("ds").setExecutor(new CommandManager(this));
        this.getCommand("ds").setTabCompleter(new CommandTabCompleter());

        saveDefaultConfig();
        ConfigManager.init(this);
        updatePriceTask = Bukkit.getScheduler().runTaskTimer(this, () -> PriceChanger.notifyUpdateShopPrice(null), 0, ConfigManager.getUpdateperiod());
    }


    @SuppressWarnings("ConstantValue")
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            log.log(Level.WARNING, "setupEconomy - Vault plugin not found");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            log.log(Level.WARNING, "setupEconomy - rsp not found");
            return false;
        }
        econ = rsp.getProvider();
        if (econ == null) {
            log.log(Level.WARNING, "setupEconomy - provider not found");
            return false;
        }
        return true;
    }

    @SuppressWarnings("ConstantValue")
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        Chat chat = rsp.getProvider();
        if (chat == null) {
            log.log(Level.WARNING, "setupChat - provider not found");
            return false;
        }
        return true;
    }

    @SuppressWarnings("ConstantValue")
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        Permission perms = rsp.getProvider();
        if (perms == null) {
            log.log(Level.WARNING, "setupPermission - provider not found");
            return false;
        }
        return true;
    }

    public static Economy getEcon() {
        return econ;
    }

}