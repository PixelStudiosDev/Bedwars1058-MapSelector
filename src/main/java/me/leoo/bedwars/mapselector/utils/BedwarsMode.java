package me.leoo.bedwars.mapselector.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public enum BedwarsMode {
    BEDWARS("BedWars1058"), PROXY("BedWarsProxy"), NONE("");

    private final String name;

    public String getPath() {
        return "plugins/" + name + "/Addons/MapSelector";
    }

    @NotNull
    public static BedwarsMode init(Plugin plugin) {
        BedwarsMode mode = null;

        for (BedwarsMode value : values()) {
            if (Bukkit.getPluginManager().isPluginEnabled(value.getName())) {
                mode = value;
                break;
            }
        }

        if (mode == null) {
            Bukkit.getLogger().severe("Bedwars1058/BedwarsProxy not found. Disabling...");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return NONE;
        }

        Bukkit.getLogger().info("Hooked into " + mode.getName());

        return mode;
    }
}
