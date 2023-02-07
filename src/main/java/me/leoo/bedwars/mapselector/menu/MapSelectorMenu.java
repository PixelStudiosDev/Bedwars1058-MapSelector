package me.leoo.bedwars.mapselector.menu;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import me.leoo.bedwars.mapselector.Main;
import me.leoo.bedwars.mapselector.configuration.Config;
import me.leoo.bedwars.mapselector.utils.ItemUtil;
import me.leoo.bedwars.mapselector.database.Yaml;
import me.leoo.bedwars.mapselector.utils.SelectorUtil;
import me.leoo.bedwars.mapselector.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MapSelectorMenu {

	public static String groupName;
	public static final HashMap<Player, Integer> sm = new HashMap<>();
	public static final HashMap<Player, Integer> page = new HashMap<>();

	public static void OpenGroupMenu(Player p, String group) {
		groupName = StringUtil.firstLetterUpperCase(group);
		Inventory inv = Bukkit.createInventory(null, Config.config.getInt("map_selector.menu.per_group_menu.slots"), Config.config.getString("map_selector.menu.per_group_menu.title"));

		for(String extraItems : Config.config.getYml().getConfigurationSection("map_selector.menu.item").getKeys(false)){
			if(Config.config.getBoolean("map_selector.menu.item." + extraItems + ".enabled")){
				if(Config.config.getBoolean("map_selector.menu.item." + extraItems + ".extra")){
					if(Config.config.getInt("map_selector.menu.item." + extraItems + ".gui") == 1){
						inv.setItem(Config.config.getInt("map_selector.menu.item." + extraItems + ".slot"), ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item." + extraItems + ".item")), Config.config.getString("map_selector.menu.item." + extraItems + ".head_url"), Config.config.getInt("map_selector.menu.item." + extraItems + ".data"), Config.config.getString("map_selector.menu.item." + extraItems + ".name"), Config.config.getList("map_selector.menu.item." + extraItems + ".lore"), Config.config.getBoolean("map_selector.menu.item." + extraItems + ".enchanted"), group, "null", "false", null, null));
					}
				}
			}
		}

		List<String> joinrandomLore = new ArrayList<>();
		for (String s : Config.config.getList("map_selector.menu.item.join_random.lore")){
			joinrandomLore.add(s.replace("{group_name}", StringUtil.firstLetterUpperCase(group)));
		}

		if(Config.config.getBoolean("map_selector.menu.item.join_random.enabled")){
			inv.setItem(Config.config.getInt("map_selector.menu.item.join_random.slot"), ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.join_random.item")), Config.config.getString("map_selector.menu.item.join_random.head_url"), Config.config.getInt("map_selector.menu.item.join_random.data"), Config.config.getString("map_selector.menu.item.join_random.name").replace("{group_name}", StringUtil.firstLetterUpperCase(group)), joinrandomLore, Config.config.getBoolean("map_selector.menu.item.join_random.enchanted"), group, null, "false", null, null));
		}
		if(Config.config.getBoolean("map_selector.menu.item.map_selector.enabled")){
			inv.setItem(Config.config.getInt("map_selector.menu.item.map_selector.slot"), ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.map_selector.item")), Config.config.getString("map_selector.menu.item.map_selector.head_url"), Config.config.getInt("map_selector.menu.item.map_selector.data"), Config.config.getString("map_selector.menu.item.map_selector.name").replace("{group_name}", StringUtil.firstLetterUpperCase(group)), Config.config.getList("map_selector.menu.item.map_selector.lore"), Config.config.getBoolean("map_selector.menu.item.map_selector.enchanted"), group, null, "false", null, null));
		}
		if(Config.config.getBoolean("map_selector.menu.item.close.enabled")){
			inv.setItem(Config.config.getInt("map_selector.menu.item.close.slot"), ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.close.item")), Config.config.getString("map_selector.menu.item.close.head_url"), Config.config.getInt("map_selector.menu.item.close.data"), Config.config.getString("map_selector.menu.item.close.name"), Config.config.getList("map_selector.menu.item.close.lore"), Config.config.getBoolean("map_selector.menu.item.close.enchanted"), group, null, "false", null, null));
		}
		if(Config.config.getBoolean("map_selector.menu.item.rejoin.enabled")){
			inv.setItem(Config.config.getInt("map_selector.menu.item.rejoin.slot"), ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.rejoin.item")), Config.config.getString("map_selector.menu.item.rejoin.head_url"), Config.config.getInt("map_selector.menu.item.rejoin.data"), Config.config.getString("map_selector.menu.item.rejoin.name"), Config.config.getList("map_selector.menu.item.rejoin.lore"), Config.config.getBoolean("map_selector.menu.item.rejoin.enchanted"), group, null, "false", null, null));
		}

		p.openInventory(inv);
	}

	public static void OpenSelectorMenu(Player p, String group, int startMaps, int currentpage) {
		groupName = StringUtil.firstLetterUpperCase(group);

		sm.putIfAbsent(p, -1);
		sm.put(p, startMaps);
		page.putIfAbsent(p, -1);
		page.put(p, currentpage);
		List<String> groups;
		if(group.contains(",")){
			groups = Arrays.asList(group.split(","));
		}else{
			groups = Collections.singletonList(group);
		}
		Inventory inv = Bukkit.createInventory(null, Config.config.getInt("map_selector.menu.selector.slots"), Config.config.getString("map_selector.menu.selector.title"));

		//Main.initializeMapsConfig();
		if(SelectorUtil.isOldDate()){
			Main.database.setPlayerUses(p.getUniqueId(), 0);
		}

		List<IArena> arenas = new ArrayList<>();
		for (IArena a : Arena.getArenas()) {
			if (groups.contains(a.getGroup())) {
				if (a.getStatus().equals(GameState.waiting) || a.getStatus().equals(GameState.starting)) {
					arenas.add(a);
				}
			}
		}
		if(arenas.isEmpty()){
			p.sendMessage(Config.config.getString("map_selector.menu.no_maps"));
			return;
		}
		arenas.sort(Comparator.comparing(IArena::getDisplayName));

		for(String extraItems : Config.config.getYml().getConfigurationSection("map_selector.menu.item").getKeys(false)){
			if(Config.config.getBoolean("map_selector.menu.item." + extraItems + ".enabled")){
				if(Config.config.getBoolean("map_selector.menu.item." + extraItems + ".extra")){
					if(Config.config.getInt("map_selector.menu.item." + extraItems + ".gui") == 2){
						inv.setItem(Config.config.getInt("map_selector.menu.item." + extraItems + ".slot"), ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item." + extraItems + ".item")), Config.config.getString("map_selector.menu.item." + extraItems + ".head_url"), Config.config.getInt("map_selector.menu.item." + extraItems + ".data"), Config.config.getString("map_selector.menu.item." + extraItems + ".name"), Config.config.getList("map_selector.menu.item." + extraItems + ".lore"), Config.config.getBoolean("map_selector.menu.item." + extraItems + ".enchanted"), group, "null", "false", null, null));
					}
				}
			}
		}

		int in = startMaps;

		for (String slotUsed : Config.config.getString("map_selector.menu.slots").split(",")) {
			int slot;
			try{
				slot = Integer.parseInt(slotUsed);
			}catch (Exception e){
				continue;
			}
			if (in >= arenas.size()) {
				continue;
			}

			List<String> mapLore = new ArrayList<>();
			for (String s : Config.config.getList("map_selector.menu.item.map.lore")){
				mapLore.add(s.replace("{group_name}", StringUtil.firstLetterUpperCase(arenas.get(in).getGroup())).replace("{available_games}", "1").replace("{times_joined}", String.valueOf(Yaml.getMapJoins(p, arenas.get(in).getArenaName()))).replace("{selections_type}", SelectorUtil.getSelectionsType(p)).replace("{remaining_uses}", SelectorUtil.getSelectionsType(p).equals(Config.config.getString("map_selector.selection.unlimited_message")) ? Config.config.getString("map_selector.selection.unlimited_message") : String.valueOf(Integer.parseInt(SelectorUtil.getSelectionsType(p)) - Main.database.getPlayerUses(p.getUniqueId()))).replace("{status}", arenas.get(in).getStatus().toString()).replace("{on}", String.valueOf(arenas.get(in).getPlayers().size())).replace("{max}", String.valueOf(arenas.get(in).getMaxPlayers())));
			}

			List<String> mapFavoriteLore = new ArrayList<>();
			for (String s : Config.config.getList("map_selector.menu.item.map_favorite.lore")){
				mapFavoriteLore.add(s.replace("{group_name}", StringUtil.firstLetterUpperCase(arenas.get(in).getGroup())).replace("{available_games}", "1").replace("{times_joined}", String.valueOf(Yaml.getMapJoins(p, arenas.get(in).getArenaName()))).replace("{selections_type}", SelectorUtil.getSelectionsType(p)).replace("{remaining_uses}", SelectorUtil.getSelectionsType(p).equals(Config.config.getString("map_selector.selection.unlimited_message")) ? Config.config.getString("map_selector.selection.unlimited_message") : String.valueOf(Integer.parseInt(SelectorUtil.getSelectionsType(p)) - Main.database.getPlayerUses(p.getUniqueId()))).replace("{status}", arenas.get(in).getStatus().toString()).replace("{on}", String.valueOf(arenas.get(in).getPlayers().size())).replace("{max}", String.valueOf(arenas.get(in).getMaxPlayers())));
			}

			List<String> mapNoUsesNoPermsLore = new ArrayList<>();
			for (String s : Config.config.getList("map_selector.menu.item.map_no_permissions_no_uses.lore")){
				mapNoUsesNoPermsLore.add(s.replace("{group_name}", StringUtil.firstLetterUpperCase(arenas.get(in).getGroup())).replace("{available_games}", "1").replace("{times_joined}", String.valueOf(Yaml.getMapJoins(p, arenas.get(in).getArenaName()))).replace("{selections_type}", SelectorUtil.getSelectionsType(p)).replace("{remaining_uses}", SelectorUtil.getSelectionsType(p).equals(Config.config.getString("map_selector.selection.unlimited_message")) ? Config.config.getString("map_selector.selection.unlimited_message") : String.valueOf(Integer.parseInt(SelectorUtil.getSelectionsType(p)) - Main.database.getPlayerUses(p.getUniqueId()))).replace("{status}", arenas.get(in).getStatus().toString()).replace("{on}", String.valueOf(arenas.get(in).getPlayers().size())).replace("{max}", String.valueOf(arenas.get(in).getMaxPlayers())));
			}

			if(SelectorUtil.getSelectionsType(p).equals(Config.config.getString("map_selector.selection.unlimited_message"))){
				if(Yaml.isFavorite(p, String.valueOf(arenas.get(in).getArenaName()))){
					ItemStack item = ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.map_favorite.item")), Config.config.getString("map_selector.menu.item.map_favorite.head_url"), Config.config.getInt("map_selector.menu.item.map_favorite.data"), Config.config.getString("map_selector.menu.item.map_favorite.name").replace("{map_name}", arenas.get(in).getDisplayName()), mapFavoriteLore, Config.config.getBoolean("map_selector.menu.item.map_favorite.enchanted"), arenas.get(in).getArenaName(), group, arenas.get(in).getDisplayName(), null, null);
					if(Config.config.getBoolean("map_selector.menu.item.map_favorite.enabled")){
						inv.setItem(slot, item);
					}
				}else{
					ItemStack item = ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.map.item")), Config.config.getString("map_selector.menu.item.map.head_url"), Config.config.getInt("map_selector.menu.item.map.data"), Config.config.getString("map_selector.menu.item.map.name").replace("{map_name}", arenas.get(in).getDisplayName()), mapLore, Config.config.getBoolean("map_selector.menu.item.map.enchanted"), arenas.get(in).getArenaName(), group, arenas.get(in).getDisplayName(), null, null);
					if(Config.config.getBoolean("map_selector.menu.item.map.enabled")){
						inv.setItem(slot, item);
					}
				}
			}else{
				if(p.hasPermission(Config.config.getString("map_selector.selection.permission"))){
					if(Main.database.getPlayerUses(p.getUniqueId()) < Integer.parseInt(SelectorUtil.getSelectionsType(p))){
						if(Yaml.isFavorite(p, String.valueOf(arenas.get(in).getArenaName()))){
							ItemStack item = ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.map_favorite.item")), Config.config.getString("map_selector.menu.item.map_favorite.head_url"), Config.config.getInt("map_selector.menu.item.map_favorite.data"), Config.config.getString("map_selector.menu.item.map_favorite.name").replace("{map_name}", arenas.get(in).getDisplayName()), mapFavoriteLore, Config.config.getBoolean("map_selector.menu.item.map_favorite.enchanted"), arenas.get(in).getArenaName(), group, arenas.get(in).getDisplayName(), null, null);
							if(Config.config.getBoolean("map_selector.menu.item.map_favorite.enabled")){
								inv.setItem(slot, item);
							}
						}else{
							ItemStack item = ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.map.item")), Config.config.getString("map_selector.menu.item.map.head_url"), Config.config.getInt("map_selector.menu.item.map.data"), Config.config.getString("map_selector.menu.item.map.name").replace("{map_name}", arenas.get(in).getDisplayName()), mapLore, Config.config.getBoolean("map_selector.menu.item.map.enchanted"), arenas.get(in).getArenaName(), group, arenas.get(in).getDisplayName(), null, null);
							if(Config.config.getBoolean("map_selector.menu.item.map.enabled")){
								inv.setItem(slot, item);
							}
						}
					}else{
						ItemStack item = ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.map_no_permissions_no_uses.item")), Config.config.getString("map_selector.menu.item.map_no_permissions_no_uses.head_url"), Config.config.getInt("map_selector.menu.item.map_no_permissions_no_uses.data"), Config.config.getString("map_selector.menu.item.map_no_permissions_no_uses.name").replace("{map_name}", arenas.get(in).getDisplayName()), mapNoUsesNoPermsLore, Config.config.getBoolean("map_selector.menu.item.map_no_permissions_no_uses.enchanted"), arenas.get(in).getArenaName(), group, "false", null, null);
						if(Config.config.getBoolean("map_selector.menu.item.map_no_permissions_no_uses.enabled")){
							inv.setItem(slot, item);
						}
					}
				}else{
					ItemStack item = ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.map_no_permissions_no_uses.item")), Config.config.getString("map_selector.menu.item.map_no_permissions_no_uses.head_url"), Config.config.getInt("map_selector.menu.item.map_no_permissions_no_uses.data"), Config.config.getString("map_selector.menu.item.map_no_permissions_no_uses.name").replace("{map_name}", arenas.get(in).getDisplayName()), mapNoUsesNoPermsLore, Config.config.getBoolean("map_selector.menu.item.map_no_permissions_no_uses.enchanted"), arenas.get(in).getArenaName(), group, "false", null, null);
					if(Config.config.getBoolean("map_selector.menu.item.map_no_permissions_no_uses.enabled")){
						inv.setItem(slot, item);
					}
				}
			}

			in++;
		}

		List<String> nextPageLore = new ArrayList<>();
		for (String s : Config.config.getList("map_selector.menu.item.next_page.lore")){
			nextPageLore.add(s.replace("{next_page}", String.valueOf((currentpage + 1)+1)));
		}

		List<String> previousPageLore = new ArrayList<>();
		for (String s : Config.config.getList("map_selector.menu.item.previous_page.lore")){
			previousPageLore.add(s.replace("{previous_page}", String.valueOf((currentpage - 1)+1)));
		}

		if(arenas.size() - (Config.config.getString("map_selector.menu.slots").split(",").length * currentpage) > Config.config.getString("map_selector.menu.slots").split(",").length){
			if(Config.config.getBoolean("map_selector.menu.item.next_page.enabled")){
				inv.setItem(Config.config.getInt("map_selector.menu.item.next_page.slot"), ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.next_page.item")), Config.config.getString("map_selector.menu.item.next_page.head_url"), Config.config.getInt("map_selector.menu.item.next_page.data"), Config.config.getString("map_selector.menu.item.next_page.name"), nextPageLore, Config.config.getBoolean("map_selector.menu.item.next_page.enchanted"), null, null, "true", null, group));
			}
		}

		if(currentpage > 0){
			if(Config.config.getBoolean("map_selector.menu.item.previous_page.enabled")){
				inv.setItem(Config.config.getInt("map_selector.menu.item.previous_page.slot"), ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.previous_page.item")), Config.config.getString("map_selector.menu.item.previous_page.head_url"), Config.config.getInt("map_selector.menu.item.previous_page.data"), Config.config.getString("map_selector.menu.item.previous_page.name"), previousPageLore, Config.config.getBoolean("map_selector.menu.item.previous_page.enchanted"), null, null, "true", null, group));
			}
		}

		List<String> randommapLore = new ArrayList<>();
		for (String s : Config.config.getList("map_selector.menu.item.random_map.lore")){
			randommapLore.add(s.replace("{group_name}", StringUtil.firstLetterUpperCase(group)).replace("{selections_type}", SelectorUtil.getSelectionsType(p)));
		}
		List<String> randomfavouriteLore = new ArrayList<>();
		for (String s : Config.config.getList("map_selector.menu.item.random_favourite.lore")){
			randomfavouriteLore.add(s.replace("{group_name}", StringUtil.firstLetterUpperCase(group)).replace("{selections_type}", SelectorUtil.getSelectionsType(p)));
		}

		if(Config.config.getBoolean("map_selector.menu.item.random_map.enabled")){
			inv.setItem(Config.config.getInt("map_selector.menu.item.random_map.slot"), ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.random_map.item")), Config.config.getString("map_selector.menu.item.random_map.head_url"), Config.config.getInt("map_selector.menu.item.random_map.data"), Config.config.getString("map_selector.menu.item.random_map.name").replace("{group_name}", StringUtil.firstLetterUpperCase(group)), randommapLore, Config.config.getBoolean("map_selector.menu.item.random_map.enchanted"), null, group, "false", null, null));
		}
		if(Config.config.getBoolean("map_selector.menu.item.random_favourite.enabled")){
			inv.setItem(Config.config.getInt("map_selector.menu.item.random_favourite.slot"), ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.random_favourite.item")), Config.config.getString("map_selector.menu.item.random_favourite.head_url"), Config.config.getInt("map_selector.menu.item.random_favourite.data"), Config.config.getString("map_selector.menu.item.random_favourite.name").replace("{group_name}", StringUtil.firstLetterUpperCase(group)), randomfavouriteLore, Config.config.getBoolean("map_selector.menu.item.random_favourite.enchanted"), null, group, "false", null, null));
		}
		if(Config.config.getBoolean("map_selector.menu.item.back.enabled")){
			inv.setItem(Config.config.getInt("map_selector.menu.item.back.slot"), ItemUtil.item(Material.valueOf(Config.config.getString("map_selector.menu.item.back.item")), Config.config.getString("map_selector.menu.item.back.head_url"), Config.config.getInt("map_selector.menu.item.back.data"), Config.config.getString("map_selector.menu.item.back.name"), Config.config.getList("map_selector.menu.item.back.lore"), Config.config.getBoolean("map_selector.menu.item.back.enchanted"), null, group, "false", null, null));
		}

		p.openInventory(inv);
	}

	public static int getCurrentPage(Player p){
		if(page.containsKey(p)){
			return page.get(p);
		}
		return -1;
	}

	public static int getStartMaps(Player p){
		if(sm.containsKey(p)){
			return sm.get(p);
		}
		return -1;
	}
}
