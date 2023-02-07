package me.leoo.bedwars.mapselector.configuration;

import me.leoo.bedwars.mapselector.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class PlayerCache {

	public static ConfigHandler cache;

	public static void setupCache() {
		String getType = Main.bungee ? "BedWarsProxy" : "BedWars1058";
		(new File("plugins/" + getType + "/Addons/MapSelector")).mkdirs();
		cache = new ConfigHandler(Main.plugin, "cache", "plugins/" + getType + "/Addons/MapSelector");
		YamlConfiguration yml = cache.getYml();

		yml.options().header(Main.plugin.getDescription().getName() + " v" + Main.plugin.getDescription().getVersion() + " made by " + Main.plugin.getDescription().getAuthors() + ".\n" +
			"Dependencies: " + Main.plugin.getDescription().getDepend() + ".\n" +
			"SoftDependencies: " + Main.plugin.getDescription().getSoftDepend() + ".\n" +
			"Join my discord for support: https://discord.gg/dtwanz4GQg\n");

		yml.options().copyDefaults(true);
		cache.save();
	}
}
