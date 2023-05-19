package com.sinam7.dynamicshop.event;

import com.sinam7.dynamicshop.ConfigManager;
import com.sinam7.dynamicshop.gui.GuiManager;
import com.sinam7.dynamicshop.villager.VillagerManager;
import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class VillagerEvent implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        ConfigManager.loadNpc();
        Entity rightClicked = event.getRightClicked();
        UUID clickedUniqueId = rightClicked.getUniqueId();
        Villager villagerById = VillagerManager.getVillagerById(rightClicked.getUniqueId());

        if (villagerById != null) {
            event.setCancelled(true);
            GuiManager.createGui(event.getPlayer(), VillagerManager.getShopIdByVillagerId(clickedUniqueId));
        }
    }

    @EventHandler
    public void onNpcVillagerDied(EntityDeathEvent event) {
        LivingEntity diedEntity = event.getEntity();
        Player killer = diedEntity.getKiller();
        if (diedEntity.getType() == EntityType.VILLAGER && killer != null && killer.isOp() && killer.getGameMode() == GameMode.CREATIVE)
            VillagerManager.removeNpc(diedEntity.getUniqueId(), killer);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ConfigManager.loadConfig();
    }
}
