package me.leoo.bedwars.mapselector;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.proxy.BedWarsProxy;
import me.leoo.bedwars.mapselector.commands.BwMenuCmd;
import me.leoo.bedwars.mapselector.commands.BwMapCmd;
import me.leoo.bedwars.mapselector.commands.ReloadSelectorMenuCmd;
import me.leoo.bedwars.mapselector.configuration.Config;
import me.leoo.bedwars.mapselector.configuration.PlayerCache;
import me.leoo.bedwars.mapselector.database.Database;
import me.leoo.bedwars.mapselector.listeners.SelectorBungeeListeners;
import me.leoo.bedwars.mapselector.listeners.SelectorListeners;
import me.leoo.bedwars.mapselector.listeners.JoinListener;
import me.leoo.bedwars.mapselector.utils.PlaceholdersUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

import static org.bukkit.Bukkit.getPluginManager;

public class Main extends JavaPlugin {

	public static Main plugin;
	public static Database database;
	public static BedWars bedWars;
	public static boolean bungee = false;

	@Override
	public void onEnable() {
		plugin = this;

		if(getPluginManager().getPlugin("BedWarsProxy") != null ){
			bungee = true;
		}
		if(bungee){
			if (getPluginManager().getPlugin("BedWarsProxy") == null) {
				getLogger().severe("§cBedWarsProxy was not found. Disabling...");
				getPluginManager().disablePlugin(this);
				return;
			}
		}else{
			if (getPluginManager().getPlugin("BedWars1058") == null) {
				bedWars = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
				getLogger().severe("§cBedWars1058 was not found. Disabling...");
				getPluginManager().disablePlugin(this);
				return;
			}
		}
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PlaceholdersUtil().register();
		}

		Config.setupConfig();
		PlayerCache.setupCache();
		//MapsConfig.setupMapsConfig();
		initializeMapsConfig();

		connectDB();

		if(Bukkit.getPluginManager().isPluginEnabled(this)){
			registerCommands();
			registerEvents();

			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aBedWars1058 Map Selector addon has been successfully enabled."));
		}
	}

	@Override
	public void onDisable() {
		database.close();
		getPluginManager().disablePlugin(this);
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cBedWars1058 Map Selector addon has been successfully disabled."));
	}

	public static void connectDB(){
		try {
			if(Config.config.getString("data-save").equalsIgnoreCase("mysql")){
				if(bungee){
					database = new Database(BedWarsProxy.config.getYml().getString("database.host"), BedWarsProxy.config.getYml().getInt("database.port"), BedWarsProxy.config.getYml().getString("database.database"), BedWarsProxy.config.getYml().getString("database.user"), BedWarsProxy.config.getYml().getString("database.pass"), BedWarsProxy.config.getYml().getBoolean("database.ssl"));
				}else{
					database = new Database(com.andrei1058.bedwars.BedWars.config.getYml().getString("database.host"), com.andrei1058.bedwars.BedWars.config.getYml().getInt("database.port"), com.andrei1058.bedwars.BedWars.config.getYml().getString("database.database"), com.andrei1058.bedwars.BedWars.config.getYml().getString("database.user"), com.andrei1058.bedwars.BedWars.config.getYml().getString("database.pass"), com.andrei1058.bedwars.BedWars.config.getYml().getBoolean("database.ssl"));
				}
			}else if(Config.config.getString("data-save").equalsIgnoreCase("sqlite")){
				File file;
				if(bungee){
					file = new File(BedWarsProxy.getPlugin().getDataFolder() + "/Cache/map_selector.db");
				}else{
					file = new File(com.andrei1058.bedwars.BedWars.plugin.getDataFolder() + "/Cache/map_selector.db");
				}
				if(!file.exists()){
					try {
						file.createNewFile();
					}catch (IOException e){
						e.printStackTrace();
					}
				}
				database = new Database(file.getAbsolutePath());
			}else{
				Bukkit.getConsoleSender().sendMessage("[" + plugin.getDescription().getName() + "] Undefined storage type (check the string data-save in the plugin config)");
			}
		}catch (Exception e){
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("[" + plugin.getDescription().getName() + "] Unavailable database connection");
			Bukkit.getPluginManager().disablePlugin(plugin);
		}
	}

	private void registerCommands(){
		//first gui command
		plugin.getCommand("bwmenu").setExecutor(new BwMenuCmd());
		plugin.getCommand("bwmap").setExecutor(new BwMapCmd());
		//second gui command
		plugin.getCommand("bwmenu").setTabCompleter(new BwMenuCmd());
		plugin.getCommand("bwmap").setTabCompleter(new BwMapCmd());
		//reload command
		plugin.getCommand("bwselector").setExecutor(new ReloadSelectorMenuCmd());
	}

	private void registerEvents(){

		if(bungee){
			getPluginManager().registerEvents(new SelectorBungeeListeners(), this);
			getPluginManager().registerEvents(new JoinListener(), this);
		}else{
			getPluginManager().registerEvents(new SelectorListeners(), this);
			getPluginManager().registerEvents(new JoinListener(), this);
		}

	}

	public static void initializeMapsConfig(){
		/*if(bungeeServer){
			for(CachedArena a : ArenaManager.getArenas()){
				if(MapsConfig.maps.getList("maps." + a.getArenaGroup() + "." + a.getArenaName()) == null || MapsConfig.maps.getList("maps." + a.getArenaGroup() + "." + a.getArenaName()).isEmpty()){
					MapsConfig.maps.set("maps." + a.getArenaGroup() + "." + a.getArenaName(), Arrays.asList(a.getArenaName()));
				}
			}
		}else{
			for(IArena a : Arena.getArenas()){
				if(MapsConfig.maps.getList("maps." + a.getGroup() + "." + a.getArenaName()) == null || MapsConfig.maps.getList("maps." + a.getGroup() + "." + a.getArenaName()).isEmpty()){
					MapsConfig.maps.set("maps." + a.getGroup() + "." + a.getArenaName(), Arrays.asList(a.getArenaName()));
				}
			}
		}*/
	}

	public static BedWars getBedWars() {
		return bedWars;
	}

}
