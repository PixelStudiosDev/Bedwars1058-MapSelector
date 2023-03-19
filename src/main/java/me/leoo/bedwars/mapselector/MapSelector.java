package me.leoo.bedwars.mapselector;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.proxy.BedWarsProxy;
import lombok.Getter;
import me.leoo.bedwars.mapselector.commands.GroupMenuCommand;
import me.leoo.bedwars.mapselector.commands.ReloadCommand;
import me.leoo.bedwars.mapselector.commands.SelectorMenuCommand;
import me.leoo.bedwars.mapselector.configuration.CacheConfig;
import me.leoo.bedwars.mapselector.configuration.ConfigManager;
import me.leoo.bedwars.mapselector.configuration.MainConfig;
import me.leoo.bedwars.mapselector.database.Database;
import me.leoo.bedwars.mapselector.database.Yaml;
import me.leoo.bedwars.mapselector.listeners.JoinListener;
import me.leoo.bedwars.mapselector.listeners.ProxySelectorListeners;
import me.leoo.bedwars.mapselector.listeners.SelectorListeners;
import me.leoo.bedwars.mapselector.utils.BedwarsMode;
import me.leoo.bedwars.mapselector.utils.PlaceholdersUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.bukkit.Bukkit.getPluginManager;

@Getter
public class MapSelector extends JavaPlugin {

    @Getter
    private static MapSelector plugin;

    private ConfigManager mainConfig;
    private ConfigManager cacheConfig;
    private Yaml yamlConfig;
    private Database ddatabase;
    private BedwarsMode bedwarsMode;

    @Override
    public void onEnable() {
        plugin = this;

        if (getPluginManager().getPlugin("BedWars1058") != null) {
            bedwarsMode = BedwarsMode.BEDWARS;
            registerEvents(new SelectorListeners());

            getPlugin().getLogger().info("Hooked into BedWars1058");
        }
        if (getPluginManager().getPlugin("BedWarsProxy") != null) {
            bedwarsMode = BedwarsMode.BEDWARSPROXY;
            registerEvents(new ProxySelectorListeners());

            getPlugin().getLogger().info("Hooked into BedWarsProxy");
        }

        if (bedwarsMode == null) Bukkit.getPluginManager().disablePlugin(this);

        mainConfig = new MainConfig(MapSelector.getPlugin(), "config", "plugins/" + bedwarsMode.getName() + "/Addons/MapSelector");
        cacheConfig = new CacheConfig(MapSelector.getPlugin(), "cache", "plugins/" + bedwarsMode.getName() + "/Addons/MapSelector");
        yamlConfig = new Yaml();

        connectDatabase();

        if (Bukkit.getPluginManager().isPluginEnabled(this)) {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                new PlaceholdersUtil().register();
            }

            registerEvents(new JoinListener());

            ((CraftServer) plugin.getServer()).getCommandMap().register("bedwarsmenu", new GroupMenuCommand("bedwarsmenu"));
            ((CraftServer) plugin.getServer()).getCommandMap().register("bedwarsmap", new SelectorMenuCommand("bedwarsmap"));
            ((CraftServer) plugin.getServer()).getCommandMap().register("bedwarsselector", new ReloadCommand("bedwarsselector"));

            getPlugin().getLogger().info("&a" + getDescription().getName() + " plugin by itz_leoo has been successfully enabled.");
        }
    }

    @Override
    public void onDisable() {
        if (ddatabase != null) ddatabase.close();
        getPlugin().getLogger().info("&c" + getDescription().getName() + " plugin by itz_leoo has been successfully disabled.");
    }

    public void debug(String string) {
        if (mainConfig == null) return;
        if (mainConfig.getBoolean("map_selector.debug"))
            getPlugin().getLogger().info(ChatColor.translateAlternateColorCodes('&', "[" + getPlugin().getDescription().getName() + "] " + string));
    }

    public void connectDatabase() {
        if (ddatabase == null) {
            try {
                String storage = mainConfig.getString("map_selector.storage");

                if (storage == null || storage.equalsIgnoreCase("sqlite")) {
                    mainConfig.getYml().set("map_selector.storage", "SQLite");

                    File folder = new File(bedwarsMode.equals(BedwarsMode.BEDWARS) ? BedWars.plugin.getDataFolder() + "/Cache" : BedWarsProxy.getPlugin().getDataFolder() + "/Cache");
                    File file = new File(folder, "database.db");

                    if (!folder.exists()) {
                        getPlugin().getLogger().info("Creating " + folder.getPath());
                        if (!folder.mkdirs()) {
                            getPlugin().getLogger().severe("Could not create " + folder.getPath());
                        }
                    }

                    if (!file.exists()) {
                        plugin.getLogger().info("Creating " + file.getPath());
                        try {
                            if (!file.createNewFile()) {
                                plugin.getLogger().severe("Could not create " + file.getPath());
                                return;
                            }
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }

                    ddatabase = new Database(file.getAbsolutePath());
                } else {
                    if (bedwarsMode.equals(BedwarsMode.BEDWARS)) {
                        ddatabase = new Database(BedWars.config.getYml().getString("database.host"), BedWars.config.getYml().getInt("database.port"), BedWars.config.getYml().getString("database.database"), BedWars.config.getYml().getString("database.user"), BedWars.config.getYml().getString("database.pass"), BedWars.config.getYml().getBoolean("database.ssl"));
                    } else if (bedwarsMode.equals(BedwarsMode.BEDWARSPROXY)) {
                        ddatabase = new Database(BedWarsProxy.config.getYml().getString("database.host"), BedWarsProxy.config.getYml().getInt("database.port"), BedWarsProxy.config.getYml().getString("database.database"), BedWarsProxy.config.getYml().getString("database.user"), BedWarsProxy.config.getYml().getString("database.pass"), BedWarsProxy.config.getYml().getBoolean("database.ssl"));
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();

                getPlugin().getLogger().info("Unavailable database connection");

                Bukkit.getPluginManager().disablePlugin(getPlugin());
            }
        }
    }

    public Database getMapSelectorDatabase() {
        if (ddatabase == null) {
            getPlugin().getLogger().info("Database connection not found. Reconnecting...");
            connectDatabase();
        }
        return ddatabase;
    }

    private void registerEvents(Listener... listeners) {
        Arrays.asList(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, plugin));
    }
}
