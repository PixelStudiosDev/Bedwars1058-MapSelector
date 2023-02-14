package me.leoo.bedwars.mapselector.utils;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.proxy.BedWarsProxy;
import com.andrei1058.bedwars.proxy.api.ArenaStatus;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import me.leoo.bedwars.mapselector.Main;
import me.leoo.bedwars.mapselector.configuration.Config;
import me.leoo.bedwars.mapselector.configuration.ConfigHandler;
import me.leoo.bedwars.mapselector.database.Yaml;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SelectorUtil {

	private static String groupName;
	private static final ConfigHandler config = Config.config;
	private static final HashMap<Player, Integer> startMaps = new HashMap<>();
	private static final HashMap<Player, Integer> page = new HashMap<>();

	public static String firstLetterUpperCase(String string) {
		if (string != null) {
			char[] letter = string.toCharArray();
			letter[0] = Character.toUpperCase(letter[0]);
			return new String(letter);
		}
		return null;
	}

	public static String getSelectionsType(Player player) {
		String type = String.valueOf(0);
		for (String s : config.getYml().getConfigurationSection("map_selector.selections_type").getKeys(false)) {
			if (player.hasPermission(config.getString("map_selector.selections_type." + s + ".permission"))) {
				if (config.getBoolean("map_selector.selections_type." + s + ".unlimited")) {
					type = config.getString("map_selector.selection.unlimited_message");
				} else {
					type = String.valueOf(config.getInt("map_selector.selections_type." + s + ".daily_uses"));
				}
			}
		}
		return type;
	}

	public static void joinRandomGroup(Player player, String group, boolean unlimited, boolean favorite) {
		if (Main.getMode().equals(BedwarsMode.BEDWARSPROXY)) {
			if (favorite) {
				List<CachedArena> favoriteArenas = new ArrayList<>(Yaml.getFavoritesBungee(player, group));
				if (favoriteArenas.isEmpty()) {
					player.sendMessage(config.getString("map_selector.menu.no_favorites_maps"));
				} else {
					for (CachedArena arena : favoriteArenas) {
						if (arena.getCurrentPlayers() >= arena.getMaxPlayers()) continue;
						joinArena(player, arena.getArenaName(), group, unlimited);
					}
				}
			} else {
				List<CachedArena> arenas = new ArrayList<>();
				ArenaManager.getArenas().forEach(arena -> {
					if (arena.getArenaGroup().equalsIgnoreCase(group)) {
						if (arena.getStatus().equals(ArenaStatus.WAITING) || arena.getStatus().equals(ArenaStatus.STARTING)) {
							if (arena.getCurrentPlayers() < arena.getMaxPlayers()) arenas.add(arena);
						}
					}
				});
				if (arenas.isEmpty()) {
					player.sendMessage(config.getString("map_selector.menu.no_maps"));
				} else {
					arenas.sort((a1, a2) -> Integer.compare(a2.getCurrentPlayers(), a1.getCurrentPlayers()));
					joinArena(player, arenas.get(0).getArenaName(), group, unlimited);
				}
			}
		} else {
			if (favorite) {
				List<IArena> favoriteArenas = new ArrayList<>(Yaml.getFavorites(player, group));
				if (favoriteArenas.isEmpty()) {
					player.sendMessage(config.getString("map_selector.menu.no_favorites_maps"));
				} else {
					for (IArena arena : favoriteArenas) {
						if (arena.getPlayers().size() >= arena.getMaxPlayers()) continue;
						joinArena(player, arena.getArenaName(), group, unlimited);
					}
				}
			} else {
				List<IArena> arenas = new ArrayList<>();
				Arena.getArenas().forEach(arena -> {
					if (arena.getGroup().equalsIgnoreCase(group)) {
						if (arena.getStatus().equals(GameState.waiting) || arena.getStatus().equals(GameState.starting)) {
							if (arena.getPlayers().size() < arena.getMaxPlayers()) arenas.add(arena);
						}
					}
				});
				if (arenas.isEmpty()) {
					player.sendMessage(config.getString("map_selector.menu.no_maps"));
				} else {
					arenas.sort((a1, a2) -> Integer.compare(a2.getPlayers().size(), a1.getPlayers().size()));
					joinArena(player, arenas.get(0).getArenaName(), group, unlimited);
				}
			}
		}
	}

	public static void joinArena(Player player, String name, String group, boolean unlimited) {
		if (Main.getMode().equals(BedwarsMode.BEDWARSPROXY)) {
			List<CachedArena> arenas = new ArrayList<>();
			ArenaManager.getArenas().forEach(arena -> {
				if (arena.getArenaName().equals(name) && arena.getArenaGroup().equalsIgnoreCase(group) && arena.getStatus().equals(ArenaStatus.WAITING) || arena.getStatus().equals(ArenaStatus.STARTING)) {
					arenas.add(arena);
				}
			});

			if (arenas.isEmpty()) return;

			if (BedWarsProxy.getParty().hasParty(player.getUniqueId()) && BedWarsProxy.getParty().getMembers(player.getUniqueId()).size() > 1) {
				if (!BedWarsProxy.getParty().isOwner(player.getUniqueId())) {
					player.sendMessage(config.getString("map_selector.menu.not_party_leader"));
					return;
				}
				BedWarsProxy.getParty().getMembers(player.getUniqueId()).forEach(partyPlayerUuid -> arenas.get(0).addPlayer(Bukkit.getPlayer(partyPlayerUuid), Bukkit.getPlayer(partyPlayerUuid).getName()));
			} else {
				arenas.get(0).addPlayer(player, player.getName());
			}
		} else {
			if (BedWars.getParty().hasParty(player) && BedWars.getParty().getMembers(player).size() > 1) {
				if (!BedWars.getParty().isOwner(player)) {
					player.sendMessage(config.getString("map_selector.menu.not_party_leader"));
					return;
				}
				BedWars.getParty().getMembers(player).forEach(partyPlayer -> Arena.getArenaByName(name).addPlayer(partyPlayer, false));
			} else {
				Arena.getArenaByName(name).addPlayer(player, false);
			}
		}

		Yaml.addMapJoin(player, name);

		if (!unlimited) {
			Main.getMapSelectorDatabase().setPlayerUses(player.getUniqueId(), Main.getMapSelectorDatabase().getPlayerUses(player.getUniqueId()) + 1);
		}
	}

	public static boolean isOldDate() {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if (day == config.getInt("map_selector.last-date")) {
			return false;
		} else {
			config.getYml().set("current-date", day);
			return true;
		}
	}

	public static void setGroupName(String groupName) {
		SelectorUtil.groupName = groupName;
	}

	public static String getGroupName() {
		return groupName;
	}

	public static HashMap<Player, Integer> getPage() {
		return page;
	}

	public static int getCurrentPage(Player p) {
		if (page.containsKey(p)) {
			return page.get(p);
		}
		return -1;
	}

	public static HashMap<Player, Integer> getStartMaps() {
		return startMaps;
	}

	public static int getStartMaps(Player p) {
		if (startMaps.containsKey(p)) {
			return startMaps.get(p);
		}
		return -1;
	}
}
