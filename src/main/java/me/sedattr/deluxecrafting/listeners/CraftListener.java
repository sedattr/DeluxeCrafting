package me.sedattr.deluxecrafting.listeners;

import me.sedattr.deluxecrafting.DeluxeCrafting;
import me.sedattr.deluxecrafting.others.DataRecipe;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class CraftListener implements Listener {
    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        for (DataRecipe recipe : DeluxeCrafting.getInstance().getRecipes()) {
            if (recipe.fits(event.getWhoClicked(), event.getInventory().getMatrix())) {
                event.setCancelled(true);

                int shiftSize = 65;

                CraftingInventory inventory = event.getInventory();
                if (event.getClick() == ClickType.SHIFT_LEFT)
                    for (int i = 1; i < 10; i++) {
                        ItemStack ingredient = recipe.getIngredients().get(i - 1);
                        ItemStack currentItem = inventory.getItem(i);
                        if (ingredient != null && ingredient.getType() != Material.AIR && currentItem != null && currentItem.getType() != Material.AIR)
                            shiftSize = Math.min(shiftSize, currentItem.getAmount() / ingredient.getAmount());
                    }

                int finalShiftSize = shiftSize;
                recipe.getIngredients().forEach((integer, itemStack) -> {
                    ItemStack inSlot = inventory.getItem(integer + 1);
                    if (inSlot == null || inSlot.getType() == Material.AIR)
                        return;
                    inSlot.setAmount(inSlot.getAmount() - ((finalShiftSize == 65) ? itemStack.getAmount() : (itemStack.getAmount() * finalShiftSize)));
                    inventory.setItem(integer + 1, inSlot);
                });

                inventory.setResult(recipe.getOutput().clone());
                inventory.getResult().setAmount((finalShiftSize == 65) ? recipe
                        .getOutput().getAmount() : (recipe
                        .getOutput().getAmount() * finalShiftSize));
                break;
            }
        }
    }
}
