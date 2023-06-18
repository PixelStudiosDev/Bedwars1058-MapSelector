/*
 *
 */

package me.leoo.bedwars.mapselector.configuration;

import me.leoo.bedwars.mapselector.MapSelector;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainConfig extends ConfigManager {

    public MainConfig(Plugin plugin, String name, String directory) {
        super(plugin, name, directory);

        YamlConfiguration yml = getYml();

        //header
        yml.options().header(MapSelector.getPlugin().getDescription().getName() + " v" + MapSelector.getPlugin().getDescription().getVersion() + " made by " + MapSelector.getPlugin().getDescription().getAuthors() + ".\n" +
                "Dependencies: " + MapSelector.getPlugin().getDescription().getDepend() + ".\n" +
                "Soft Dependencies: " + MapSelector.getPlugin().getDescription().getSoftDepend() + ".\n" +
                "See the wiki: https://leo18bernese.gitbook.io/bedwars1058-mapselector/\n" +
                "Join my discord server for support: https://discord.gg/dtwanz4GQg\n" +
                "Storage methods: MySQL / SQLite.\n");

        //settings
        yml.addDefault("map-selector.debug", Boolean.FALSE);
        yml.addDefault("map-selector.storage", "SQLite");
        yml.addDefault("map-selector.last-date", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        //commands messages
        yml.addDefault("map-selector.messages.open.group-doesnt-exists", "&cThis group doesn't exists");
        yml.addDefault("map-selector.messages.open.missing", "&cUse: /bwmenu <group>");
        yml.addDefault("map-selector.messages.open.missing2", "&cUse: /bwmap <group>");
        yml.addDefault("map-selector.messages.reload.success", "&aConfiguration reloaded!");
        yml.addDefault("map-selector.messages.reset-uses.missing", "&cUse: /bwselector resetuses <player>");
        yml.addDefault("map-selector.messages.reset-uses.not-found", "&cPlayer not found!");
        yml.addDefault("map-selector.messages.reset-uses.success", "&aReset uses of {player}!");
        yml.addDefault("map-selector.messages.set-uses.missing", "&cUse: /bwselector setuses <player> <uses>");
        yml.addDefault("map-selector.messages.set-uses.not-found", "&cPlayer not found!");
        yml.addDefault("map-selector.messages.set-uses.success", "&aSet uses of {player} to {uses}!");

        //error messages
        yml.addDefault("map-selector.messages.limit-reached", "&cYou've reached your limit of daily map selections today!");
        yml.addDefault("map-selector.messages.no-favorites-maps", "&cYou don't have any favorite maps");
        yml.addDefault("map-selector.messages.no-maps", "&cThere aren't any available arenas of this group");
        yml.addDefault("map-selector.messages.not-party-leader", "&cYou can't join arenas because your aren't the leader of your party");

        //other values
        yml.addDefault("map-selector.selections.permission", "bwselector.selector");
        yml.addDefault("map-selector.selections.unlimited-message", "Unlimited");

        //menu settings
        yml.addDefault("map-selector.menus.bedwars-menu.title", "&8Play Bed Wars ");
        yml.addDefault("map-selector.menus.bedwars-menu.slots", 36);
        yml.addDefault("map-selector.menus.maps-menu.title", "&8Bed Wars ");
        yml.addDefault("map-selector.menus.maps-menu.slots", 54);
        yml.addDefault("map-selector.menus.maps-menu.maps-slots", "10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34");

        //first gui items
        addFirstGuiItem("join-random", 12, false, Material.BED,
                "&aBed Wars ({groupName})",
                Arrays.asList("&7Play a game of Bed Wars {groupName}.", "", "&eClick to play!"));
        addFirstGuiItem("map-selector", 14, false, Material.SIGN,
                "&aMap Selector ({groupName})",
                Arrays.asList("&7Pick which map you want to play", "&7from a list of available games.", "", "&eClick to browse!"));
        addFirstGuiItem("close", 31, false, Material.BARRIER,
                "&cClose",
                Collections.emptyList());
        addFirstGuiItem("rejoin", 35, false, Material.ENDER_PEARL,
                "&cClick here to rejoin!",
                Arrays.asList("&7Click here to rejoin your game", "&7if you have been disconnected", "&7from it."));

        //second gui items
        addSecondGuiItem("map", 0, false, Material.PAPER,
                "&a{mapName}",
                Arrays.asList("&8{groupName}", "", "&7Available Games: &a{availableGames}", "&7Times Joined: &a{timesJoined}", "&7Map Selections: &a{selectionsType}", "", "&a▸ Click to Play", "&eRight click to toggle favorite!"));
        addSecondGuiItem("map-favorite", 0, false, Material.EMPTY_MAP,
                "&b✫ &a{mapName}",
                Arrays.asList("&8{groupName}", "", "&7Available Games: &a{availableGames}", "&7Times Joined: &a{timesJoined}", "&7Map Selections: &a{selectionsType}", "", "&a▸ Click to Play", "&eRight click to toggle favorite!"));
        addSecondGuiItem("map-no-permissions-no-uses", 0, false, Material.SULPHUR,
                "&a{mapName}",
                Arrays.asList("&8{groupName}", "", "&7Available Games: &a{availableGames}", "&7Times Joined: &a{timesJoined}", "&7Map Selections: &a{selectionsType}", "", "&c✘ You don't have the required rank(s)", "&cor you have reached", "&cthe daily map selections limit!"));

        addSecondGuiItem("random-map", 39, false, Material.FIREWORK,
                "&aRandom Map",
                Arrays.asList("&8{groupName}", "", "&7Map selections: &a{selectionsType}", "", "&a▸ Click to Play"));
        addSecondGuiItem("random-favourite", 41, false, Material.DIAMOND,
                "&aRandom Favourite",
                Arrays.asList("&8{groupName}", "", "&7Map selections: &a{selectionsType}", "", "&a▸ Click to Play"));
        addSecondGuiItem("back", 49, false, Material.ARROW,
                "&aBack",
                Collections.emptyList());
        addSecondGuiItem("next-page", 26, false, Material.ARROW,
                "&aNext Page",
                Collections.singletonList("&ePage {nextPage}"));
        addSecondGuiItem("previous-page", 18, false, Material.ARROW,
                "&aPrevious Page",
                Collections.singletonList("&ePage {previousPage}"));

        //extra items
        addFirstGuiItem("practice", 27, true, Material.WOOL,
                "&aPractice",
                Arrays.asList("&7Improve your gameplay by", "&7practicing different aspects of", "&7Bed Wars!", "", "&eClick to view modes!"));
        addSecondGuiItem("book", 53, true, Material.BOOK,
                "&aMap Selection",
                Arrays.asList("&7Each day you can choose which", "&7map you want to play 1 time.", "&7Unlock unlimited map selection!", "&7by upgrading to &bMVP&c+ &7rank", "&7on our store!", "", "&ehttps://store.hypixel.net!"));

        //selections settings
        addSelectionsType(yml, "default", "bwselector.default", 3, false);
        addSelectionsType(yml, "mvp+", "bwselector.mvp+", 0, true);
        addSelectionsType(yml, "admin", "bwselector.admin", 0, true);

        yml.options().copyDefaults(true);
        save();
    }


    public void addFirstGuiItem(String name, int slot, boolean extra, Material material, String itemName, List<String> itemLore) {
        getYml().addDefault("map-selector.menus.bedwars-menu.items." + name + ".material", material.name());
        getYml().addDefault("map-selector.menus.bedwars-menu.items." + name + ".enabled", true);
        getYml().addDefault("map-selector.menus.bedwars-menu.items." + name + ".extra", extra);
        getYml().addDefault("map-selector.menus.bedwars-menu.items." + name + ".slot", slot);
        getYml().addDefault("map-selector.menus.bedwars-menu.items." + name + ".command", "default-action");
        getYml().addDefault("map-selector.menus.bedwars-menu.items." + name + ".head-value", "Head value here (see: https://bit.ly/3eF7h8l)");
        getYml().addDefault("map-selector.menus.bedwars-menu.items." + name + ".data", 0);
        getYml().addDefault("map-selector.menus.bedwars-menu.items." + name + ".name", itemName);
        getYml().addDefault("map-selector.menus.bedwars-menu.items." + name + ".lore", itemLore);
        getYml().addDefault("map-selector.menus.bedwars-menu.items." + name + ".enchanted", false);
    }

    public void addSecondGuiItem(String name, int slot, boolean extra, Material material, String itemName, List<String> itemLore) {
        getYml().addDefault("map-selector.menus.maps-menu.items." + name + ".material", material.name());
        getYml().addDefault("map-selector.menus.maps-menu.items." + name + ".enabled", true);
        getYml().addDefault("map-selector.menus.maps-menu.items." + name + ".extra", extra);
        getYml().addDefault("map-selector.menus.maps-menu.items." + name + ".slot", slot);
        getYml().addDefault("map-selector.menus.maps-menu.items." + name + ".command", "default-action");
        getYml().addDefault("map-selector.menus.maps-menu.items." + name + ".head-value", "Head value here (see: https://bit.ly/3eF7h8l)");
        getYml().addDefault("map-selector.menus.maps-menu.items." + name + ".data", 0);
        getYml().addDefault("map-selector.menus.maps-menu.items." + name + ".name", itemName);
        getYml().addDefault("map-selector.menus.maps-menu.items." + name + ".lore", itemLore);
        getYml().addDefault("map-selector.menus.maps-menu.items." + name + ".enchanted", false);
    }

    public void addSelectionsType(YamlConfiguration yml, String name, String permission, int uses, boolean unlimited) {
        yml.addDefault("map-selector.selections.selections." + name + ".permission", permission);
        yml.addDefault("map-selector.selections.selections." + name + ".daily-uses", uses);
        yml.addDefault("map-selector.selections.selections." + name + ".unlimited", unlimited);
    }
}
