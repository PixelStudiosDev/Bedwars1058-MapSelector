package me.leoo.bedwars.mapselector.configuration;

import me.leoo.bedwars.mapselector.Main;
import me.leoo.bedwars.mapselector.utils.SelectorUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Config {

	public static ConfigHandler config;

	public static void setupConfig() {
		String getType = Main.bungee ? "BedWarsProxy" : "BedWars1058";
		(new File("plugins/" + getType + "/Addons/MapSelector")).mkdirs();
		config = new ConfigHandler(Main.plugin, "config", "plugins/" + getType + "/Addons/MapSelector");
		YamlConfiguration yml = config.getYml();

		//header
		yml.options().header(Main.plugin.getDescription().getName() + " v" + Main.plugin.getDescription().getVersion() + " made by " + Main.plugin.getDescription().getAuthors() + ".\n" +
			"Dependencies: " + Main.plugin.getDescription().getDepend() + ".\n" +
			"SoftDependencies: " + Main.plugin.getDescription().getSoftDepend() + ".\n" +
			"See the wiki: https://leo18bernese.gitbook.io/bedwars1058-mapselector/\n" +
			"Join my discord for support: https://discord.gg/dtwanz4GQg\n" +
			"Cache storage available options: MySQL / SQLite.\n");

		//options
		yml.addDefault("data-save", "MySQL");
		yml.addDefault("current-date", SelectorUtil.getDate());

		//default items
		addSelectorItem(yml, "join_random", true, 1, false, "default_action", 12, Material.BED, 0, "Put here the value of a head (see more on: https://bit.ly/3eF7h8l)", "&aBed Wars ({group_name})", Arrays.asList("&7Play Bed Wars {group_name}.", "", "&eClick to play!"), false);
		addSelectorItem(yml, "map_selector", true, 1, false, "default_action",14, Material.SIGN, 0, "Put here the value of a head (see more on: https://bit.ly/3eF7h8l)","&aMap Selector ({group_name})", Arrays.asList("&7Pick which map you want to play", "&7from a list of available games.", "", "&eClick to browse!"), false);
		addSelectorItem(yml, "close", true, 1, false, "default_action",31, Material.BARRIER, 0, "Put here the value of a head (see more on: https://bit.ly/3eF7h8l)","&cClose", Arrays.asList(), false);
		addSelectorItem(yml, "rejoin", true, 1, false, "default_action",35, Material.ENDER_PEARL, 0, "Put here the value of a head (see more on: https://bit.ly/3eF7h8l)","&cClick here to rejoin!", Arrays.asList("&7Click here to rejoin your game", "&7if you have been disconnected", "&7from it."), false);

		addSelectorItem(yml, "map", true, 1, false, "default_action",0, Material.PAPER, 0, "Put here the value of a head (see more on: https://bit.ly/3eF7h8l)","&a{map_name}", Arrays.asList("&8{group_name}", "", "&7Available Games: &a{available_games}", "&7Times Joined: &a{times_joined}", "&7Map Selections: &a{selections_type}", "", "&a▸ Click to Play", "&eRight click to toggle favorite!"), false);
		addSelectorItem(yml, "map_favorite", true, 1, false, "default_action",0, Material.EMPTY_MAP, 0, "Put here the value of a head (see more on: https://bit.ly/3eF7h8l)","&b✫ &a{map_name}", Arrays.asList("&8{group_name}", "", "&7Available Games: &a{available_games}", "&7Times Joined: &a{times_joined}", "&7Map Selections: &a{selections_type}", "", "&a▸ Click to Play", "&eRight click to toggle favorite!"), false);
		addSelectorItem(yml, "map_no_permissions_no_uses", true, 1, false, "default_action",0, Material.SULPHUR, 0, "Put here the value of a head (see more on: https://bit.ly/3eF7h8l)","&a{map_name}", Arrays.asList("&8{group_name}", "", "&7Available Games: &a{available_games}", "&7Times Joined: &a{times_joined}", "&7Map Selections: &a{selections_type}", "", "&c✘ You don't have the required rank", "&cor you have reached", "&cthe daily map selections limit!"), false);

		addSelectorItem(yml, "random_map", true, 1, false, "default_action",39, Material.FIREWORK, 0, "Put here the value of a head (see more on: https://bit.ly/3eF7h8l)", "&aRandom Map", Arrays.asList("&8{group_name}", "", "&7Map selections: &a{selections_type}", "", "&a▸ Click to Play"), false);
		addSelectorItem(yml, "random_favourite", true, 1, false, "default_action",41, Material.DIAMOND, 0, "Put here the value of a head (see more on: https://bit.ly/3eF7h8l)", "&aRandom Favourite", Arrays.asList("&8{group_name}", "", "&7Map selections: &a{selections_type}", "", "&a▸ Click to Play"), false);
		addSelectorItem(yml, "back", true, 1, false, "default_action",49, Material.ARROW, 0, "Put here the value of a head (see more on: https://bit.ly/3eF7h8l)","&aBack", Arrays.asList(), false);
		addSelectorItem(yml, "next_page", true, 1, false, "default_action",26, Material.ARROW, 0, "Put here the value of a head (see more on: https://bit.ly/3eF7h8l)","&aNext Page", Arrays.asList("&ePage {next_page}"), false);
		addSelectorItem(yml, "previous_page", true, 1, false, "default_action",18, Material.ARROW, 0, "Put here the value of a head (see more on: https://bit.ly/3eF7h8l)","&aPrevious Page", Arrays.asList("&ePage {previous_page}"), false);

		//extras
		addSelectorItem(yml, "practice", true, 1, true, "default_action",27, Material.WOOL, 0, "Put here the value of a head (see more on: https://bit.ly/3eF7h8l)","&aPractice", Arrays.asList("&7Improve your gameplay by", "&7practicing different aspects of", "&7Bed Wars!", "", "&eClick to view modes!"), false);
		addSelectorItem(yml, "book", true, 2, true, "default_action",53, Material.BOOK, 0, "Put here the value of a head (see more on: https://bit.ly/3eF7h8l)","&aMap Selection", Arrays.asList("&7Each day you can choose which", "&7map you want to play 1 time.", "&7Unlock unlimited map selection!", "&7by upgrading to &bMVP&c+ &7rank", "&7on our store!", "", "&ehttps://store.hypixel.net!"), false);

		//other values
		yml.addDefault("map_selector.selection.permission", "bwselector.selector");
		yml.addDefault("map_selector.selection.unlimited_message", "Unlimited");

		//menu settings
		yml.addDefault("map_selector.menu.per_group_menu.title", "&8Play Bed Wars");
		yml.addDefault("map_selector.menu.per_group_menu.slots", 36);
		yml.addDefault("map_selector.menu.selector.title", "&8Play Bed Wars ");
		yml.addDefault("map_selector.menu.selector.slots", 54);

		//cmds
		yml.addDefault("map_selector.menu.open.group_doesnt_exist", "&cThis group does not exist");
		yml.addDefault("map_selector.menu.open.missing", "&cUse: /bwmenu <group>");
		yml.addDefault("map_selector.menu.open.missing2", "&cUse: /bwmap <group>");
		yml.addDefault("map_selector.menu.reload.missing", "&cUse: /bwselector reload");
		yml.addDefault("map_selector.menu.reload.success", "&aConfiguration reloaded!");

		//error messages
		yml.addDefault("map_selector.menu.limit_reached", "&cYou have reached the limit of map selections today");
		yml.addDefault("map_selector.menu.no_favorites_maps", "&cYou don't have favorite maps");
		yml.addDefault("map_selector.menu.no_maps", "&cThere are no available maps of this group");
		yml.addDefault("map_selector.menu.not_party_leader", "&cYou can't join arenas because your aren't the leader of your party");

		//slots
		yml.addDefault("map_selector.menu.slots", "10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34");

		//selection options
		addSelectionsType(yml, "default", "bwselector.default", 3, false);
		addSelectionsType(yml, "mvp+", "bwselector.mvp+", 0, true);
		addSelectionsType(yml, "admin", "bwselector.admin", 0, true);

		yml.options().copyDefaults(true);
		config.save();
	}

	public static void addSelectorItem(YamlConfiguration yml, String sectionName, boolean enabled, int gui, boolean extra, String command, int slot, Material itemstack, int data, String headUrl, String name, List<String> lore, boolean enchanted) {
		yml.addDefault("map_selector.menu.item.%path%.item".replace("%path%", String.valueOf(sectionName)), itemstack.toString());
		yml.addDefault("map_selector.menu.item.%path%.enabled".replace("%path%", String.valueOf(sectionName)), enabled);
		yml.addDefault("map_selector.menu.item.%path%.gui".replace("%path%", String.valueOf(sectionName)), gui);
		yml.addDefault("map_selector.menu.item.%path%.extra".replace("%path%", String.valueOf(sectionName)), extra);
		yml.addDefault("map_selector.menu.item.%path%.command".replace("%path%", String.valueOf(sectionName)), command);
		yml.addDefault("map_selector.menu.item.%path%.slot".replace("%path%", String.valueOf(sectionName)), slot);
		yml.addDefault("map_selector.menu.item.%path%.name".replace("%path%", String.valueOf(sectionName)), name);
		yml.addDefault("map_selector.menu.item.%path%.head_url".replace("%path%", String.valueOf(sectionName)), headUrl);
		yml.addDefault("map_selector.menu.item.%path%.data".replace("%path%", String.valueOf(sectionName)), data);
		yml.addDefault("map_selector.menu.item.%path%.lore".replace("%path%", String.valueOf(sectionName)), lore);
		yml.addDefault("map_selector.menu.item.%path%.enchanted".replace("%path%", String.valueOf(sectionName)), enchanted);
	}

	public static void addSelectionsType(YamlConfiguration yml, String s, String permission, int uses, boolean unlimited) {
		yml.addDefault("map_selector.selections_type.%path%.permission".replace("%path%", String.valueOf(s)), permission);
		yml.addDefault("map_selector.selections_type.%path%.daily_uses".replace("%path%", String.valueOf(s)), uses);
		yml.addDefault("map_selector.selections_type.%path%.unlimited".replace("%path%", String.valueOf(s)), unlimited);
	}
}
