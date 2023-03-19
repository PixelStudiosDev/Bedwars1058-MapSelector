package me.leoo.bedwars.mapselector.configuration;

import me.leoo.bedwars.mapselector.MapSelector;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class CacheConfig extends ConfigManager {

    public CacheConfig(Plugin plugin, String name, String directory) {
        super(plugin, name, directory);

        YamlConfiguration yml = getYml();

        yml.options().header(MapSelector.getPlugin().getDescription().getName() + " v" + MapSelector.getPlugin().getDescription().getVersion() + " made by " + MapSelector.getPlugin().getDescription().getAuthors() + ".\n" +
                "Dependencies: " + MapSelector.getPlugin().getDescription().getDepend() + ".\n" +
                "SoftDependencies: " + MapSelector.getPlugin().getDescription().getSoftDepend() + ".\n" +
                "Join my discord for support: https://discord.gg/dtwanz4GQg\n");

        yml.options().copyDefaults(true);

        save();
    }
}
