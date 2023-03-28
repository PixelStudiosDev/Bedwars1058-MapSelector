package me.leoo.bedwars.mapselector.configuration;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.leoo.bedwars.mapselector.MapSelector;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Getter
public class ConfigManager {
    private YamlConfiguration yml;
    private File config;
    private String name;

    public ConfigManager(Plugin plugin, String name, String dir) {
        File folder = new File(dir);

        if (!folder.exists()) {
            plugin.getLogger().log(Level.INFO, "Creating {0}", folder.getPath());
            if (!folder.mkdirs()) {
                plugin.getLogger().log(Level.SEVERE, "Could not create {0}", folder.getPath());
                return;
            }
        }

        if (!folder.exists() && !folder.mkdirs()) {
            plugin.getLogger().log(Level.SEVERE, "Could not create {0}", folder.getPath());
        } else {
            this.config = new File(dir, name + ".yml");

            if (!this.config.exists()) {
                plugin.getLogger().log(Level.INFO, "Creating {0}", this.config.getPath());
                try {
                    if (!this.config.createNewFile()) {
                        plugin.getLogger().log(Level.SEVERE, "Could not create {0}", this.config.getPath());
                        return;
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

            this.yml = YamlConfiguration.loadConfiguration(this.config);
            this.yml.options().copyDefaults(true);
            this.name = name;
        }
    }

    /**
     * Reload configuration.
     */
    public void reload() {
        yml = YamlConfiguration.loadConfiguration(config);
    }

    /**
     * Save yml file
     */
    public void save() {
        try {
            this.yml.save(this.config);
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    /**
     * Get boolean at given path
     */
    public boolean getBoolean(String path) {
        return yml.getBoolean(path);
    }

    /**
     * Get integer at given path
     */
    public int getInt(String path) {
        return yml.getInt(path);
    }

    public String getString(String path) {
        if (yml.getString(path) == null) {
            MapSelector.getPlugin().getLogger().info("String " + path + " not found in " + yml.getName());
            return "MissingString";
        }
        return PlaceholderAPI.setPlaceholders(null, ChatColor.translateAlternateColorCodes('&', yml.getString(path)));
    }

    /**
     * Get list of strings at given path
     *
     * @return a list of string with colors translated
     */
    public List<String> getList(String path) {
        return yml.getStringList(path).stream()
                .map(s -> PlaceholderAPI.setPlaceholders(null, ChatColor.translateAlternateColorCodes('&', s)))
                .collect(Collectors.toList());
    }

    /**
     * Set object and save file
     */
    public void set(String path, Object object) {
        getYml().set(path, object);
        save();
    }
}
