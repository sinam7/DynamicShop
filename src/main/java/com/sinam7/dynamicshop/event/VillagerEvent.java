package com.sinam7.dynamicshop.event;

import com.sinam7.dynamicshop.ConfigManager;
import com.sinam7.dynamicshop.gui.GuiManager;
import com.sinam7.dynamicshop.villager.VillagerManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class VillagerEvent implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity rightClicked = event.getRightClicked();
        UUID clickedUniqueId = rightClicked.getUniqueId();
        Villager villagerById = VillagerManager.getVillagerById(rightClicked.getUniqueId());

        if (villagerById != null) {
            event.setCancelled(true);
            GuiManager.createGui(event.getPlayer(), VillagerManager.getShopIdByVillagerId(clickedUniqueId));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ConfigManager.loadConfig();
    }
}
