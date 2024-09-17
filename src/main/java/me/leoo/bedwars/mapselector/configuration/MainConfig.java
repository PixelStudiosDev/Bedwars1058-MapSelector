package me.leoo.bedwars.mapselector.configuration;

import com.cryptomorin.xseries.XMaterial;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.utils.bukkit.config.ConfigManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Calendar;

public class MainConfig extends ConfigManager {

    public MainConfig(String name, String directory) {
        super(name, directory);

        YamlConfiguration yml = getYml();

        //header
        yml.options().header(MapSelector.get().getDescription().getName() + " v" + MapSelector.get().getDescription().getVersion() + " made by " + MapSelector.get().getDescription().getAuthors() + ".\n" +
                "Dependencies: " + MapSelector.get().getDescription().getDepend() + ".\n" +
                "Soft Dependencies: " + MapSelector.get().getDescription().getSoftDepend() + ".\n" +
                "Read the wiki: https://leo18bernese.gitbook.io/bedwars1058-mapselector/\n" +
                "Join my discord for support: https://pixelstudios.dev/discord\n" +
                "Storage methods: MySQL / SQLite.\n");

        //settings
        add("map-selector.debug", false);
        add("map-selector.update-checker", true);
        add("map-selector.storage", "SQLite");
        add("map-selector.last-date", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        //commands messages
        add("map-selector.messages.open.group-doesnt-exists", "&cThis group doesn't exists");
        add("map-selector.messages.open.missing", "&cUse: /bwmenu <group>");
        add("map-selector.messages.open.missing2", "&cUse: /bwmap <group>");
        add("map-selector.messages.reload.success", "&aConfiguration reloaded!");
        add("map-selector.messages.reset-uses.missing", "&cUse: /bwselector resetuses <player>");
        add("map-selector.messages.reset-uses.not-found", "&cPlayer not found!");
        add("map-selector.messages.reset-uses.success", "&aReset uses of {player}!");
        add("map-selector.messages.set-uses.missing", "&cUse: /bwselector setuses <player> <uses>");
        add("map-selector.messages.set-uses.not-found", "&cPlayer not found!");
        add("map-selector.messages.set-uses.success", "&aSet uses of {player} to {uses}!");

        //error messages
        add("map-selector.messages.limit-reached", "&cYou've reached your limit of daily map selections today!");
        add("map-selector.messages.no-favorites-maps", "&cYou don't have any favorite maps");
        add("map-selector.messages.no-maps", "&cThere aren't any available arenas of this group");
        add("map-selector.messages.not-party-leader", "&cYou can't join arenas because your aren't the leader of your party");

        //other values
        add("map-selector.selections.permission", "bwselector.selector");
        add("map-selector.selections.unlimited-message", "Unlimited");

        //menu settings
        add("map-selector.menus.bedwars-menu.title", "&8Play Bed Wars ");
        add("map-selector.menus.bedwars-menu.slots", 36);
        add("map-selector.menus.maps-menu.title", "&8Bed Wars ");
        add("map-selector.menus.maps-menu.slots", 54);
        add("map-selector.menus.maps-menu.maps-slots", "10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34");

        //first gui items
        addFirstGuiItem("join-random", 12, XMaterial.RED_BED,
                "&aBed Wars ({groupName})",
                "&7Play a game of Bed Wars {groupName}.", "", "&eClick to play!");
        addFirstGuiItem("map-selector", 14, XMaterial.OAK_SIGN,
                "&aMap Selector ({groupName})",
                "&7Pick which map you want to play", "&7from a list of available games.", "", "&eClick to browse!");
        addFirstGuiItem("close", 31, XMaterial.BARRIER,
                "&cClose");
        addFirstGuiItem("rejoin", 35, XMaterial.ENDER_PEARL,
                "&cClick here to rejoin!",
                "&7Click here to rejoin your game", "&7if you have been disconnected", "&7from it.");

        //second gui items
        addSecondGuiItem("map", 0, XMaterial.PAPER,
                "&a{mapName}",
                "&8{groupName}", "", "&7Available Games: &a{availableGames}", "&7Times Joined: &a{timesJoined}", "&7Map Selections: &a{selectionsType}", "", "&a▸ Click to Play", "&eRight click to toggle favorite!");
        addSecondGuiItem("map-favorite", 0, XMaterial.MAP,
                "&b✫ &a{mapName}",
                "&8{groupName}", "", "&7Available Games: &a{availableGames}", "&7Times Joined: &a{timesJoined}", "&7Map Selections: &a{selectionsType}", "", "&a▸ Click to Play", "&eRight click to toggle favorite!");
        addSecondGuiItem("map-no-permissions-no-uses", 0, XMaterial.GUNPOWDER,
                "&a{mapName}",
                "&8{groupName}", "", "&7Available Games: &a{availableGames}", "&7Times Joined: &a{timesJoined}", "&7Map Selections: &a{selectionsType}", "", "&c✘ You don't have the required rank(s)", "&cor you have reached", "&cthe daily map selections limit!");

        addSecondGuiItem("random-map", 39, XMaterial.FIREWORK_ROCKET,
                "&aRandom Map",
                "&8{groupName}", "", "&7Map selections: &a{selectionsType}", "", "&a▸ Click to Play");
        addSecondGuiItem("random-favourite", 41, XMaterial.DIAMOND,
                "&aRandom Favourite",
                "&8{groupName}", "", "&7Map selections: &a{selectionsType}", "", "&a▸ Click to Play");
        addSecondGuiItem("back", 49, XMaterial.ARROW,
                "&aBack");
        addSecondGuiItem("next-page", 26, XMaterial.ARROW,
                "&aNext Page",
                "&ePage {nextPage}");
        addSecondGuiItem("previous-page", 18, XMaterial.ARROW,
                "&aPrevious Page",
                "&ePage {previousPage}");

        //extra items
        addFirstGuiItem("practice", 27, XMaterial.RED_BED,
                "&aPractice",
                "&7Improve your gameplay by", "&7practicing different aspects of", "&7Bed Wars!", "", "&eClick to view modes!");
        addSecondGuiItem("book", 53, XMaterial.BOOK,
                "&aMap Selection",
                "&7Each day you can choose which", "&7map you want to play 1 time.", "&7Unlock unlimited map selection!", "&7by upgrading to &bMVP&c+ &7rank", "&7on our store!", "", "&ehttps://store.myserver.net!");

        //selections settings
        if (isFirstTime()) {
            addSelectionsType("default", "bwselector.default", 3, false);
            addSelectionsType("mvp+", "bwselector.mvp+", 0, true);
            addSelectionsType("admin", "bwselector.admin", 0, true);
        }

        yml.options().copyDefaults(true);
        save();
    }


    private void addFirstGuiItem(String name, int slot, XMaterial material, String itemName, String... itemLore) {
        saveItem("map-selector.menus.bedwars-menu.items." + name, slot, material, itemName, itemLore);
    }

    private void addSecondGuiItem(String name, int slot, XMaterial material, String itemName, String... itemLore) {
        saveItem("map-selector.menus.maps-menu.items." + name, slot, material, itemName, itemLore);
    }

    private void addSelectionsType(String name, String permission, int uses, boolean unlimited) {
        String path = "map-selector.selections.selections." + name + ".";

        add(path + "permission", permission);
        add(path + "daily-uses", uses);
        add(path + "unlimited", unlimited);
    }
}
