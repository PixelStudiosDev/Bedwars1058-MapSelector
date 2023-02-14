package me.leoo.bedwars.mapselector.configuration;

import me.leoo.bedwars.mapselector.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class PlayerCache {

	public static ConfigHandler config;

	public static void setupConfig() {
		String mode = Main.getMode().getName();
		File file = new File("plugins/" + mode + "/Addons/MapSelector");

		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("[BedWars1058-MapSelector] Successfully created the plugin's folder");
			} else {
				System.out.println("[BedWars1058-MapSelector] Error while creating the plugin's folder");
			}
		}

		config = new ConfigHandler(Main.getPlugin(), "cache", "plugins/" + mode + "/Addons/MapSelector");
		YamlConfiguration yml = config.getYml();

		yml.options().header(Main.getPlugin().getDescription().getName() + " v" + Main.getPlugin().getDescription().getVersion() + " made by " + Main.getPlugin().getDescription().getAuthors() + ".\n" +
			"Dependencies: " + Main.getPlugin().getDescription().getDepend() + ".\n" +
			"SoftDependencies: " + Main.getPlugin().getDescription().getSoftDepend() + ".\n" +
			"Join my discord for support: https://discord.gg/dtwanz4GQg\n");

		yml.options().copyDefaults(true);
		config.save();
	}
}
