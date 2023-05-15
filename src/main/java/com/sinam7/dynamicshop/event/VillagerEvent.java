package com.sinam7.dynamicshop.event;

import com.sinam7.dynamicshop.gui.GuiManager;
import com.sinam7.dynamicshop.villager.VillagerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.logging.Level;

public class VillagerEvent implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Bukkit.getLogger().log(Level.INFO, event.toString());
        Entity rightClicked = event.getRightClicked();
        int entityId = rightClicked.getEntityId();
        if (VillagerManager.getVillagerById(entityId) != null) {
            event.setCancelled(true);
            GuiManager.createGui(event.getPlayer(), VillagerManager.getShopIdByVillagerId(entityId));
        }
    }
}
