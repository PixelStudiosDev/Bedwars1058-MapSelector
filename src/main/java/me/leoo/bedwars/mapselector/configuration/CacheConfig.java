package me.leoo.bedwars.mapselector.configuration;

import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.utils.bukkit.config.ConfigManager;
import org.bukkit.configuration.file.YamlConfiguration;

public class CacheConfig extends ConfigManager {

    public CacheConfig(String name, String directory) {
        super(name, directory);

        YamlConfiguration yml = getYml();

        yml.options().header(MapSelector.get().getDescription().getName() + " v" + MapSelector.get().getDescription().getVersion() + " made by " + MapSelector.get().getDescription().getAuthors() + ".\n" +
                "Dependencies: " + MapSelector.get().getDescription().getDepend() + ".\n" +
                "SoftDependencies: " + MapSelector.get().getDescription().getSoftDepend() + ".\n" +
                "Join my discord for support: https://pixelstudios.dev/discord\n"
        );

        yml.options().copyDefaults(true);

        save();
    }
}
