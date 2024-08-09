package me.sedattr.deluxecrafting;

import lombok.Getter;
import me.sedattr.deluxecrafting.listeners.CraftListener;
import me.sedattr.deluxecrafting.listeners.PrepareCraftListener;
import me.sedattr.deluxecrafting.others.DataRecipe;
import me.sedattr.deluxecrafting.others.FileStorage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class DeluxeCrafting extends JavaPlugin {
    @Getter private static DeluxeCrafting instance;
    @Getter private final List<DataRecipe> recipes = new ArrayList<>();
    @Getter private YamlConfiguration config;

    public void addRecipe(DataRecipe recipe) {
        this.recipes.add(recipe);
    }

    public void removeRecipe(DataRecipe recipe) {
        this.recipes.remove(recipe);
    }

    public DataRecipe getRecipe(String name) {
        if (name == null || name.isEmpty())
            return null;

        return this.recipes.stream().filter(a -> a.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        this.config = getConfig();

        new FileStorage();

        Bukkit.getPluginManager().registerEvents(new CraftListener(), this);
        Bukkit.getPluginManager().registerEvents(new PrepareCraftListener(), this);
    }
}
