package me.leoo.bedwars.mapselector;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.proxy.BedWarsProxy;
import me.leoo.bedwars.mapselector.commands.BwMapCmd;
import me.leoo.bedwars.mapselector.commands.BwMenuCmd;
import me.leoo.bedwars.mapselector.commands.ReloadSelectorMenuCmd;
import me.leoo.bedwars.mapselector.configuration.Config;
import me.leoo.bedwars.mapselector.configuration.PlayerCache;
import me.leoo.bedwars.mapselector.database.Database;
import me.leoo.bedwars.mapselector.listeners.JoinListener;
import me.leoo.bedwars.mapselector.listeners.ProxySelectorListeners;
import me.leoo.bedwars.mapselector.listeners.SelectorListeners;
import me.leoo.bedwars.mapselector.utils.BedwarsMode;
import me.leoo.bedwars.mapselector.utils.PlaceholdersUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.bukkit.Bukkit.getPluginManager;

public class Main extends JavaPlugin {

	private static Main plugin;
	private static Database database = null;
	private static BedwarsMode type = null;

	@Override
	public void onEnable() {
		plugin = this;

		if (getPluginManager().getPlugin("Bedwars1058") != null) {
			type = BedwarsMode.BEDWARS;

			registerEvents(new SelectorListeners());
			debug("Hooked into BedWars1058");
		} else if (getPluginManager().getPlugin("BedWarsProxy") != null) {
			type = BedwarsMode.BEDWARSPROXY;

			registerEvents(new ProxySelectorListeners());
			debug("Hooked into BedWarsProxy");
		}

		if (type == null) Bukkit.getPluginManager().disablePlugin(this);

		Config.setupConfig();
		PlayerCache.setupConfig();

		registerEvents(new JoinListener());

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PlaceholdersUtil().register();
		}

		connectDatabase();

		if (Bukkit.getPluginManager().isPluginEnabled(this)) {
			registerCommands();

			debug("&a" + getDescription().getName() + " plugin by itz_leoo has been successfully enabled.");
		}
	}

	@Override
	public void onDisable() {
		if (database != null) database.close();
		debug("&c" + getDescription().getName() + " plugin by itz_leoo has been successfully disabled.");
	}

	public static void debug(String s) {
		if(Config.config == null) return;
		if (Config.config.getBoolean("map_selector.debug"))
			System.out.println(ChatColor.translateAlternateColorCodes('&', "[" + getPlugin().getDescription().getName() + "] " + s));
	}

	public static void connectDatabase() {
		try {
			String storage = Config.config.getString("map_selector.storage");
			if (storage == null || storage.equalsIgnoreCase("sqlite")) {
				Config.config.getYml().set("map_selector.storage", "SQLite");

				File file = new File(type.equals(BedwarsMode.BEDWARS) ? BedWars.plugin.getDataFolder() + "/Cache/database.db" : BedWarsProxy.getPlugin().getDataFolder() + "/Cache/database.db");
				if (!file.exists()) {
					try {
						if (!file.createNewFile()) debug("Error while creating the database.db file");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				database = new Database(file.getAbsolutePath());
			} else if (storage.equalsIgnoreCase("mysql")) {
				if (type.equals(BedwarsMode.BEDWARS)) {
					database = new Database(BedWars.config.getYml().getString("database.host"), BedWars.config.getYml().getInt("database.port"), BedWars.config.getYml().getString("database.database"), BedWars.config.getYml().getString("database.user"), BedWars.config.getYml().getString("database.pass"), BedWars.config.getYml().getBoolean("database.ssl"));
				} else if (getMode().equals(BedwarsMode.BEDWARSPROXY)) {
					database = new Database(BedWarsProxy.config.getYml().getString("database.host"), BedWarsProxy.config.getYml().getInt("database.port"), BedWarsProxy.config.getYml().getString("database.database"), BedWarsProxy.config.getYml().getString("database.user"), BedWarsProxy.config.getYml().getString("database.pass"), BedWarsProxy.config.getYml().getBoolean("database.ssl"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			debug("Unavailable database connection");
			Bukkit.getPluginManager().disablePlugin(getPlugin());
		}
	}

	private void registerCommands() {
		plugin.getCommand("bedwarsmenu").setExecutor(new BwMenuCmd());
		plugin.getCommand("bedwarsmenu").setTabCompleter(new BwMenuCmd());
		plugin.getCommand("bedwarsmap").setExecutor(new BwMapCmd());
		plugin.getCommand("bedwarsmap").setTabCompleter(new BwMapCmd());
		plugin.getCommand("bedwarsselector").setExecutor(new ReloadSelectorMenuCmd());
	}


	private void registerEvents(Listener... listeners) {
		Arrays.asList(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, plugin));
	}

	public static Main getPlugin() {
		return plugin;
	}

	public static Database getMapSelectorDatabase() {
		return database;
	}

	public static BedwarsMode getMode() {
		return type;
	}
}
