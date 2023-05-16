package com.sinam7.dynamicshop.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public record GuiHolder(long shopId) implements InventoryHolder {

    @SuppressWarnings("DataFlowIssue")
    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
