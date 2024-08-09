package me.sedattr.deluxecrafting.others;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class DataRecipe {
    private final ItemStack output;
    private final String name;
    private final boolean usePermission;
    private final Map<Integer, ItemStack> ingredients = new HashMap<>();

    public DataRecipe(ItemStack output, String name, boolean usePermission) {
        this.output = output.clone();
        this.name = (name == null) ? "" : name;
        this.usePermission = usePermission;
    }

    public void setIngredient(int slot, ItemStack item) {
        if (item == null)
            item = new ItemStack(Material.AIR);

        this.ingredients.put(slot, item);
    }

    public boolean fits(HumanEntity player, ItemStack[] itemStacks) {
        if (this.usePermission && !this.name.isEmpty() && !player.hasPermission("crafting." + this.name))
            return false;

        for (int i = 0; i < itemStacks.length; i++) {
            ItemStack input = itemStacks[i];
            ItemStack ingredient = this.ingredients.get(i);
            if (input == null || input.getType() == Material.AIR) {
                if (ingredient != null && ingredient.getType() != Material.AIR)
                    return false;
            } else if (!isSimilar(ingredient, input) || input.getAmount() < ingredient.getAmount())
                return false;
        }

        return true;
    }

    public boolean isSimilar(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null)
            return false;
        if (item1.getType() != item2.getType() || !item1.getType().equals(item2.getType()))
            return false;
        if (item1 == item2 || item1.equals(item2))
            return true;

        ItemMeta meta1 = item1.hasItemMeta() ? item1.getItemMeta() : null;
        ItemMeta meta2 = item2.hasItemMeta() ? item2.getItemMeta() : null;
        if (meta1 != null && meta2 != null) {
            if (meta1.hasDisplayName() && meta2.hasDisplayName() && (
                    meta1.getDisplayName().equalsIgnoreCase(meta2.getDisplayName()) || ChatColor.stripColor(meta1.getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(meta2.getDisplayName()))))
                return true;
            if (meta1.hasLore() && meta2.hasLore() && meta1.getLore().equals(meta2.getLore()))
                return true;
        }

        return item1.hasItemMeta() && item1.getItemMeta().hasDisplayName() && item2.hasItemMeta() && item2.getItemMeta().hasDisplayName() && item1.getItemMeta().getDisplayName().equalsIgnoreCase(item2.getItemMeta().getDisplayName());
    }
}
