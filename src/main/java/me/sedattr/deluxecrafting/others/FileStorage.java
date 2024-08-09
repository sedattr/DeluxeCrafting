package me.sedattr.deluxecrafting.others;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import me.sedattr.deluxecrafting.DeluxeCrafting;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class FileStorage {
    private final File file;
    private final FileConfiguration fileConfig;

    public FileStorage() {
        this.file = new File(DeluxeCrafting.getInstance().getDataFolder(), "data.yml");

        if (!this.file.exists())
            DeluxeCrafting.getInstance().saveResource("data.yml", false);

        this.fileConfig = new YamlConfiguration();
        try {
            this.fileConfig.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

        for (String key : this.fileConfig.getKeys(false)) {
            ItemStack result = this.fileConfig.getItemStack(key + ".result");
            if (result == null)
                continue;

            DataRecipe recipe = new DataRecipe(result, key, this.fileConfig.getBoolean(key + ".permission", false));

            ConfigurationSection section = this.fileConfig.getConfigurationSection(key + ".ingredients");
            if (section == null)
                continue;

            Set<String> keys = section.getKeys(false);
            if (keys.isEmpty())
                continue;

            for (String id : keys) {
                recipe.setIngredient(Integer.parseInt(id), section.getItemStack(id));
            }

            DeluxeCrafting.getInstance().addRecipe(recipe);
        }
    }

    public void save() {
        this.fileConfig.getKeys(false).forEach(key -> this.fileConfig.set(key, null));

        for (DataRecipe recipe : DeluxeCrafting.getInstance().getRecipes()) {
            String name = recipe.getName();

            this.fileConfig.set(name + ".permission", recipe.isUsePermission());
            this.fileConfig.set(name + ".result", recipe.getOutput());
            this.fileConfig.set(name + ".ingredients", recipe.getIngredients());
        }

        try {
            this.fileConfig.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}