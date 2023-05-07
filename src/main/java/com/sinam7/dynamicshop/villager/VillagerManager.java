package com.sinam7.dynamicshop.villager;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;

import static net.kyori.adventure.text.Component.text;

public class VillagerManager {
    public static void createVillager(Player sender, @NotNull String @NotNull [] args) {
        Location loc = sender.getLocation();
        loc.setPitch(0);
        Villager v = (Villager) Bukkit.getServer().getWorld(sender.getWorld().getUID()).spawnEntity(loc, EntityType.VILLAGER);


        String rawName = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ' ');
        v.customName(text(rawName.isBlank() ? "Default Name" : rawName));
        v.setAI(false);
        v.setCollidable(false);
        v.setInvulnerable(true);
        v.setCustomNameVisible(true);
        v.setHealth(1);

        Random random = new Random();
        v.setProfession(Villager.Profession.values()[random.nextInt(0, 15)]);
    }
}
