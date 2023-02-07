package me.leoo.bedwars.mapselector.configuration;

import me.leoo.bedwars.mapselector.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;

public class MapsConfig {

	public static ConfigHandler maps;

	public static void setupMapsConfig() {
		String getType = Main.bungee ? "BedWarsProxy" : "BedWars1058";
		(new File("plugins/" + getType + "/Addons/MapSelector")).mkdirs();
		maps = new ConfigHandler(Main.plugin, "maps_config", "plugins/" + getType + "/Addons/MapSelector");
		YamlConfiguration yml = maps.getYml();

		yml.options().header(Main.plugin.getDescription().getName() + " v" + Main.plugin.getDescription().getVersion() + " made by " + Main.plugin.getDescription().getAuthors() + ".\n" +
			"Dependencies: " + Main.plugin.getDescription().getDepend() + ".\n" +
			"SoftDependencies: " + Main.plugin.getDescription().getSoftDepend() + ".\n" +
			"Join my discord for support: https://discord.gg/dtwanz4GQg\n");

		yml.addDefault("maps", Arrays.asList());

		yml.options().copyDefaults(true);
		maps.save();
	}
}
