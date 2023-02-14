package me.leoo.bedwars.mapselector.menu;

import com.andrei1058.bedwars.proxy.api.ArenaStatus;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import com.andrei1058.bedwars.proxy.language.LanguageManager;
import me.leoo.bedwars.mapselector.Main;
import me.leoo.bedwars.mapselector.configuration.Config;
import me.leoo.bedwars.mapselector.configuration.ConfigHandler;
import me.leoo.bedwars.mapselector.database.Database;
import me.leoo.bedwars.mapselector.database.Yaml;
import me.leoo.bedwars.mapselector.utils.ItemUtil;
import me.leoo.bedwars.mapselector.utils.SelectorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MapSelectorMenuProxy {

	private static final ConfigHandler config = Config.config;
	private static final Database database = Main.getMapSelectorDatabase();

	public static void OpenGroupMenu(Player player, String group) {
		SelectorUtil.setGroupName(SelectorUtil.firstLetterUpperCase(group));
		Inventory inv = Bukkit.createInventory(null, config.getInt("map_selector.menu.per_group_menu.slots"), config.getString("map_selector.menu.per_group_menu.title"));

		for (String extraItems : config.getYml().getConfigurationSection("map_selector.menu.item").getKeys(false)) {
			if (config.getBoolean("map_selector.menu.item." + extraItems + ".enabled")) {
				if (config.getBoolean("map_selector.menu.item." + extraItems + ".extra")) {
					if (config.getInt("map_selector.menu.item." + extraItems + ".gui") == 1) {
						inv.setItem(config.getInt("map_selector.menu.item." + extraItems + ".slot"), ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item." + extraItems + ".item")), config.getString("map_selector.menu.item." + extraItems + ".head_url"), config.getInt("map_selector.menu.item." + extraItems + ".data"), config.getString("map_selector.menu.item." + extraItems + ".name"), config.getList("map_selector.menu.item." + extraItems + ".lore"), config.getBoolean("map_selector.menu.item." + extraItems + ".enchanted"), group, null, "true", "map_selector.menu.item." + extraItems, null));
					}
				}
			}
		}

		List<String> joinRandomLore = new ArrayList<>();
		for (String s : config.getList("map_selector.menu.item.join_random.lore")) {
			if (s.contains("{group_name}")) s = s.replace("{group_name}", SelectorUtil.firstLetterUpperCase(group));
			joinRandomLore.add(s);
		}

		if (config.getBoolean("map_selector.menu.item.join_random.enabled")) {
			inv.setItem(config.getInt("map_selector.menu.item.join_random.slot"), ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.join_random.item")), config.getString("map_selector.menu.item.join_random.head_url"), config.getInt("map_selector.menu.item.join_random.data"), config.getString("map_selector.menu.item.join_random.name").replace("{group_name}", SelectorUtil.firstLetterUpperCase(group)), joinRandomLore, config.getBoolean("map_selector.menu.item.join_random.enchanted"), group, null, "true", null, null));
		}
		if (config.getBoolean("map_selector.menu.item.map_selector.enabled")) {
			inv.setItem(config.getInt("map_selector.menu.item.map_selector.slot"), ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.map_selector.item")), config.getString("map_selector.menu.item.map_selector.head_url"), config.getInt("map_selector.menu.item.map_selector.data"), config.getString("map_selector.menu.item.map_selector.name").replace("{group_name}", SelectorUtil.firstLetterUpperCase(group)), config.getList("map_selector.menu.item.map_selector.lore"), config.getBoolean("map_selector.menu.item.map_selector.enchanted"), group, null, "true", null, null));
		}
		if (config.getBoolean("map_selector.menu.item.close.enabled")) {
			inv.setItem(config.getInt("map_selector.menu.item.close.slot"), ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.close.item")), config.getString("map_selector.menu.item.close.head_url"), config.getInt("map_selector.menu.item.close.data"), config.getString("map_selector.menu.item.close.name"), config.getList("map_selector.menu.item.close.lore"), config.getBoolean("map_selector.menu.item.close.enchanted"), group, null, "true", null, null));
		}
		if (config.getBoolean("map_selector.menu.item.rejoin.enabled")) {
			inv.setItem(config.getInt("map_selector.menu.item.rejoin.slot"), ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.rejoin.item")), config.getString("map_selector.menu.item.rejoin.head_url"), config.getInt("map_selector.menu.item.rejoin.data"), config.getString("map_selector.menu.item.rejoin.name"), config.getList("map_selector.menu.item.rejoin.lore"), config.getBoolean("map_selector.menu.item.rejoin.enchanted"), group, null, "true", null, null));
		}

		player.openInventory(inv);
	}

	public static void OpenSelectorMenu(Player player, String group, int startMaps, int currentpage) {

		Inventory inv = Bukkit.createInventory(null, config.getInt("map_selector.menu.selector.slots"), config.getString("map_selector.menu.selector.title"));

		SelectorUtil.setGroupName(SelectorUtil.firstLetterUpperCase(group));
		SelectorUtil.getStartMaps().put(player, startMaps);
		SelectorUtil.getPage().put(player, currentpage);

		List<String> groups;
		if (group.contains(",")) {
			groups = Arrays.asList(group.split(","));
		} else {
			groups = Collections.singletonList(group);
		}

		if (SelectorUtil.isOldDate()) {
			database.setAllPlayersUses(0);
		}

		List<CachedArena> arenas = new ArrayList<>();
		for (CachedArena arena : ArenaManager.getArenas()) {
			if (groups.contains(arena.getArenaGroup()) && arena.getStatus().equals(ArenaStatus.WAITING) || arena.getStatus().equals(ArenaStatus.STARTING)) {
				arenas.add(arena);
			}
		}
		if (arenas.isEmpty()) {
			player.sendMessage(config.getString("map_selector.menu.no_maps"));
			return;
		}

		com.andrei1058.bedwars.proxy.api.Language language = LanguageManager.get().getPlayerLanguage(player);
		arenas.sort(Comparator.comparing(arena -> arena.getDisplayName(language)));

		for (String extraItems : config.getYml().getConfigurationSection("map_selector.menu.item").getKeys(false)) {
			if (config.getBoolean("map_selector.menu.item." + extraItems + ".enabled")) {
				if (config.getBoolean("map_selector.menu.item." + extraItems + ".extra")) {
					if (config.getInt("map_selector.menu.item." + extraItems + ".gui") == 2) {
						inv.setItem(config.getInt("map_selector.menu.item." + extraItems + ".slot"), ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item." + extraItems + ".item")), config.getString("map_selector.menu.item." + extraItems + ".head_url"), config.getInt("map_selector.menu.item." + extraItems + ".data"), config.getString("map_selector.menu.item." + extraItems + ".name"), config.getList("map_selector.menu.item." + extraItems + ".lore"), config.getBoolean("map_selector.menu.item." + extraItems + ".enchanted"), group, "null", "true", "map_selector.menu.item." + extraItems, null));
					}
				}
			}
		}

		for (String slotUsed : config.getString("map_selector.menu.slots").split(",")) {
			int slot;
			try {
				slot = Integer.parseInt(slotUsed);
			} catch (Exception e) {
				continue;
			}
			if (startMaps >= arenas.size()) {
				continue;
			}

			List<String> mapLore = new ArrayList<>();
			for (String s : config.getList("map_selector.menu.item.map.lore")) {
				if (s.contains("{group_name}")) s = s.replace("{group_name}", SelectorUtil.firstLetterUpperCase(arenas.get(startMaps).getArenaGroup()));
				if (s.contains("{available_games}")) s = s.replace("{available_games}", "1");
				if (s.contains("{times_joined}")) s = s.replace("{times_joined}", String.valueOf(Yaml.getMapJoins(player, arenas.get(startMaps).getArenaName())));
				if (s.contains("{selections_type}")) s = s.replace("{selections_type}", SelectorUtil.firstLetterUpperCase(arenas.get(startMaps).getArenaGroup()));
				if (s.contains("{group_name}")) s = s.replace("{group_name}", SelectorUtil.getSelectionsType(player));
				if (s.contains("{remaining_uses}")) s = s.replace("{remaining_uses}", SelectorUtil.getSelectionsType(player).equals(config.getString("map_selector.selection.unlimited_message")) ? config.getString("map_selector.selection.unlimited_message") : String.valueOf(Integer.parseInt(SelectorUtil.getSelectionsType(player)) - database.getPlayerUses(player.getUniqueId())));
				if (s.contains("{status}")) s = s.replace("{status}", arenas.get(startMaps).getStatus().toString());
				if (s.contains("{on}")) s = s.replace("{on}", String.valueOf(arenas.get(startMaps).getCurrentPlayers()));
				if (s.contains("{max}")) s = s.replace("{max}", String.valueOf(arenas.get(startMaps).getMaxPlayers()));
				mapLore.add(s);
			}

			List<String> mapFavoriteLore = new ArrayList<>();
			for (String s : config.getList("map_selector.menu.item.map_favorite.lore")) {
				if (s.contains("{group_name}")) s = s.replace("{group_name}", SelectorUtil.firstLetterUpperCase(arenas.get(startMaps).getArenaGroup()));
				if (s.contains("{available_games}")) s = s.replace("{available_games}", "1");
				if (s.contains("{times_joined}")) s = s.replace("{times_joined}", String.valueOf(Yaml.getMapJoins(player, arenas.get(startMaps).getArenaName())));
				if (s.contains("{selections_type}")) s = s.replace("{selections_type}", SelectorUtil.firstLetterUpperCase(arenas.get(startMaps).getArenaGroup()));
				if (s.contains("{group_name}")) s = s.replace("{group_name}", SelectorUtil.getSelectionsType(player));
				if (s.contains("{remaining_uses}")) s = s.replace("{remaining_uses}", SelectorUtil.getSelectionsType(player).equals(config.getString("map_selector.selection.unlimited_message")) ? config.getString("map_selector.selection.unlimited_message") : String.valueOf(Integer.parseInt(SelectorUtil.getSelectionsType(player)) - database.getPlayerUses(player.getUniqueId())));
				if (s.contains("{status}")) s = s.replace("{status}", arenas.get(startMaps).getStatus().toString());
				if (s.contains("{on}")) s = s.replace("{on}", String.valueOf(arenas.get(startMaps).getCurrentPlayers()));
				if (s.contains("{max}")) s = s.replace("{max}", String.valueOf(arenas.get(startMaps).getMaxPlayers()));
				mapFavoriteLore.add(s);
			}

			List<String> mapNoUsesNoPermissionsLore = new ArrayList<>();
			for (String s : config.getList("map_selector.menu.item.map_no_permissions_no_uses.lore")) {
				if (s.contains("{group_name}")) s = s.replace("{group_name}", SelectorUtil.firstLetterUpperCase(arenas.get(startMaps).getArenaGroup()));
				if (s.contains("{available_games}")) s = s.replace("{available_games}", "1");
				if (s.contains("{times_joined}")) s = s.replace("{times_joined}", String.valueOf(Yaml.getMapJoins(player, arenas.get(startMaps).getArenaName())));
				if (s.contains("{selections_type}")) s = s.replace("{selections_type}", SelectorUtil.firstLetterUpperCase(arenas.get(startMaps).getArenaGroup()));
				if (s.contains("{group_name}")) s = s.replace("{group_name}", SelectorUtil.getSelectionsType(player));
				if (s.contains("{remaining_uses}")) s = s.replace("{remaining_uses}", SelectorUtil.getSelectionsType(player).equals(config.getString("map_selector.selection.unlimited_message")) ? config.getString("map_selector.selection.unlimited_message") : String.valueOf(Integer.parseInt(SelectorUtil.getSelectionsType(player)) - database.getPlayerUses(player.getUniqueId())));
				if (s.contains("{status}")) s = s.replace("{status}", arenas.get(startMaps).getStatus().toString());
				if (s.contains("{on}")) s = s.replace("{on}", String.valueOf(arenas.get(startMaps).getCurrentPlayers()));
				if (s.contains("{max}")) s = s.replace("{max}", String.valueOf(arenas.get(startMaps).getMaxPlayers()));
				mapNoUsesNoPermissionsLore.add(s);
			}

			if (SelectorUtil.getSelectionsType(player).equals(config.getString("map_selector.selection.unlimited_message"))) {
				if (Yaml.isFavorite(player, String.valueOf(arenas.get(startMaps).getArenaName()))) {
					if (config.getBoolean("map_selector.menu.item.map_favorite.enabled")) {
						inv.setItem(slot, ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.map_favorite.item")), config.getString("map_selector.menu.item.map_favorite.head_url"), config.getInt("map_selector.menu.item.map_favorite.data"), config.getString("map_selector.menu.item.map_favorite.name").replace("{map_name}", arenas.get(startMaps).getDisplayName(language)), mapFavoriteLore, config.getBoolean("map_selector.menu.item.map_favorite.enchanted"), arenas.get(startMaps).getArenaName(), arenas.get(startMaps).getArenaGroup(), "true", arenas.get(startMaps).getDisplayName(language), group));
					}
				} else {
					if (config.getBoolean("map_selector.menu.item.map.enabled")) {
						inv.setItem(slot, ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.map.item")), config.getString("map_selector.menu.item.map.head_url"), config.getInt("map_selector.menu.item.map.data"), config.getString("map_selector.menu.item.map.name").replace("{map_name}", arenas.get(startMaps).getDisplayName(language)), mapLore, config.getBoolean("map_selector.menu.item.map.enchanted"), arenas.get(startMaps).getArenaName(), arenas.get(startMaps).getArenaGroup(), "true", arenas.get(startMaps).getDisplayName(language), group));
					}
				}
			} else {
				if (player.hasPermission(config.getString("map_selector.selection.permission"))) {
					if (database.getPlayerUses(player.getUniqueId()) < Integer.parseInt(SelectorUtil.getSelectionsType(player))) {
						if (Yaml.isFavorite(player, String.valueOf(arenas.get(startMaps).getArenaName()))) {
							if (config.getBoolean("map_selector.menu.item.map_favorite.enabled")) {
								inv.setItem(slot, ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.map_favorite.item")), config.getString("map_selector.menu.item.map_favorite.head_url"), config.getInt("map_selector.menu.item.map_favorite.data"), config.getString("map_selector.menu.item.map_favorite.name").replace("{map_name}", arenas.get(startMaps).getDisplayName(language)), mapFavoriteLore, config.getBoolean("map_selector.menu.item.map_favorite.enchanted"), arenas.get(startMaps).getArenaName(), arenas.get(startMaps).getArenaGroup(), "true", arenas.get(startMaps).getDisplayName(language), group));
							}
						} else {
							if (config.getBoolean("map_selector.menu.item.map.enabled")) {
								inv.setItem(slot, ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.map.item")), config.getString("map_selector.menu.item.map.head_url"), config.getInt("map_selector.menu.item.map.data"), config.getString("map_selector.menu.item.map.name").replace("{map_name}", arenas.get(startMaps).getDisplayName(language)), mapLore, config.getBoolean("map_selector.menu.item.map.enchanted"), arenas.get(startMaps).getArenaName(), arenas.get(startMaps).getArenaGroup(), "true", arenas.get(startMaps).getDisplayName(language), group));
							}
						}
					} else {
						if (config.getBoolean("map_selector.menu.item.map_no_permissions_no_uses.enabled")) {
							inv.setItem(slot, ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.map_no_permissions_no_uses.item")), config.getString("map_selector.menu.item.map_no_permissions_no_uses.head_url"), config.getInt("map_selector.menu.item.map_no_permissions_no_uses.data"), config.getString("map_selector.menu.item.map_no_permissions_no_uses.name").replace("{map_name}", arenas.get(startMaps).getDisplayName(language)), mapNoUsesNoPermissionsLore, config.getBoolean("map_selector.menu.item.map_no_permissions_no_uses.enchanted"), arenas.get(startMaps).getArenaName(), arenas.get(startMaps).getArenaGroup(), "true", arenas.get(startMaps).getDisplayName(language), group));
						}
					}
				} else {
					ItemStack item = ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.map_no_permissions_no_uses.item")), config.getString("map_selector.menu.item.map_no_permissions_no_uses.head_url"), config.getInt("map_selector.menu.item.map_no_permissions_no_uses.data"), config.getString("map_selector.menu.item.map_no_permissions_no_uses.name").replace("{map_name}", arenas.get(startMaps).getDisplayName(language)), mapNoUsesNoPermissionsLore, config.getBoolean("map_selector.menu.item.map_no_permissions_no_uses.enchanted"), arenas.get(startMaps).getArenaName(), arenas.get(startMaps).getArenaGroup(), "true", arenas.get(startMaps).getDisplayName(language), group);
					if (config.getBoolean("map_selector.menu.item.map_no_permissions_no_uses.enabled")) {
						inv.setItem(slot, item);
					}
				}
			}

			startMaps++;
		}

		List<String> nextPageLore = new ArrayList<>();
		for (String s : config.getList("map_selector.menu.item.next_page.lore")) {
			nextPageLore.add(s.replace("{next_page}", String.valueOf((currentpage + 1) + 1)));
		}

		List<String> previousPageLore = new ArrayList<>();
		for (String s : config.getList("map_selector.menu.item.previous_page.lore")) {
			previousPageLore.add(s.replace("{previous_page}", String.valueOf((currentpage - 1) + 1)));
		}

		if (arenas.size() - (config.getString("map_selector.menu.slots").split(",").length * currentpage) > config.getString("map_selector.menu.slots").split(",").length) {
			if (config.getBoolean("map_selector.menu.item.next_page.enabled")) {
				inv.setItem(config.getInt("map_selector.menu.item.next_page.slot"), ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.next_page.item")), config.getString("map_selector.menu.item.next_page.head_url"), config.getInt("map_selector.menu.item.next_page.data"), config.getString("map_selector.menu.item.next_page.name"), nextPageLore, config.getBoolean("map_selector.menu.item.next_page.enchanted"), null, null, "true", null, group));
			}
		}

		if (currentpage > 0) {
			if (config.getBoolean("map_selector.menu.item.previous_page.enabled")) {
				inv.setItem(config.getInt("map_selector.menu.item.previous_page.slot"), ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.previous_page.item")), config.getString("map_selector.menu.item.previous_page.head_url"), config.getInt("map_selector.menu.item.previous_page.data"), config.getString("map_selector.menu.item.previous_page.name"), previousPageLore, config.getBoolean("map_selector.menu.item.previous_page.enchanted"), null, null, "true", null, group));
			}
		}

		List<String> randommapLore = new ArrayList<>();
		for (String s : config.getList("map_selector.menu.item.random_map.lore")) {
			randommapLore.add(s.replace("{group_name}", SelectorUtil.firstLetterUpperCase(group)).replace("{selections_type}", SelectorUtil.getSelectionsType(player)));
		}
		List<String> randomfavouriteLore = new ArrayList<>();
		for (String s : config.getList("map_selector.menu.item.random_favourite.lore")) {
			randomfavouriteLore.add(s.replace("{group_name}", SelectorUtil.firstLetterUpperCase(group)).replace("{selections_type}", SelectorUtil.getSelectionsType(player)));
		}

		if (config.getBoolean("map_selector.menu.item.random_map.enabled")) {
			inv.setItem(config.getInt("map_selector.menu.item.random_map.slot"), ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.random_map.item")), config.getString("map_selector.menu.item.random_map.head_url"), config.getInt("map_selector.menu.item.random_map.data"), config.getString("map_selector.menu.item.random_map.name").replace("{group_name}", SelectorUtil.firstLetterUpperCase(group)), randommapLore, config.getBoolean("map_selector.menu.item.random_map.enchanted"), null, group, "true", null, null));
		}
		if (config.getBoolean("map_selector.menu.item.random_favourite.enabled")) {
			inv.setItem(config.getInt("map_selector.menu.item.random_favourite.slot"), ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.random_favourite.item")), config.getString("map_selector.menu.item.random_favourite.head_url"), config.getInt("map_selector.menu.item.random_favourite.data"), config.getString("map_selector.menu.item.random_favourite.name").replace("{group_name}", SelectorUtil.firstLetterUpperCase(group)), randomfavouriteLore, config.getBoolean("map_selector.menu.item.random_favourite.enchanted"), null, group, "true", null, null));
		}
		if (config.getBoolean("map_selector.menu.item.back.enabled")) {
			inv.setItem(config.getInt("map_selector.menu.item.back.slot"), ItemUtil.item(Material.valueOf(config.getString("map_selector.menu.item.back.item")), config.getString("map_selector.menu.item.back.head_url"), config.getInt("map_selector.menu.item.back.data"), config.getString("map_selector.menu.item.back.name").replace("{group_name}", SelectorUtil.firstLetterUpperCase(group)), config.getList("map_selector.menu.item.back.lore"), config.getBoolean("map_selector.menu.item.back.enchanted"), null, group, "true", null, null));
		}

		player.openInventory(inv);
	}
}
