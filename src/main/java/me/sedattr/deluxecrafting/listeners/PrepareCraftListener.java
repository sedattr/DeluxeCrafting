package me.sedattr.deluxecrafting.listeners;

import me.sedattr.deluxecrafting.DeluxeCrafting;
import me.sedattr.deluxecrafting.others.DataRecipe;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

public class PrepareCraftListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        for (DataRecipe recipe : DeluxeCrafting.getInstance().getRecipes()) {
            if (recipe.fits(event.getViewers().get(0), event.getInventory().getMatrix())) {
                event.getInventory().setResult(recipe.getOutput().clone());
                break;
            }
        }
    }
}