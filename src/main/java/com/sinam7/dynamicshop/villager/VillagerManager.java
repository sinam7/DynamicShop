package com.sinam7.dynamicshop.villager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import java.util.Objects;
import java.util.Random;

import static net.kyori.adventure.text.Component.text;

public class VillagerManager {
    public static void createVillager(String name, Location loc) {
        loc.setPitch(0);
        Villager v = (Villager) Objects.requireNonNull(Bukkit.getServer().getWorld(loc.getWorld().getUID())).spawnEntity(loc, EntityType.VILLAGER);

        v.customName(text(name));
        v.setAI(false);
        v.setCollidable(false);
        v.setInvulnerable(true);
        v.setCustomNameVisible(true);
        v.setHealth(1);

        Random random = new Random();
        v.setProfession(Villager.Profession.values()[random.nextInt(0, 15)]);
    }
}
