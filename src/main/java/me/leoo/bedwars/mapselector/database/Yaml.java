package me.leoo.bedwars.mapselector.database;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.proxy.api.ArenaStatus;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import me.leoo.bedwars.mapselector.configuration.PlayerCache;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Yaml {

	public static void storePlayer(Player player) {
		PlayerCache.config.getYml().set(player.getUniqueId() + ".favorite_maps", Collections.emptyList());
		PlayerCache.config.getYml().set(player.getUniqueId() + ".per_map_times_joined", Collections.emptyList());
	}

	public static boolean isStoredPlayer(Player player) {
		return PlayerCache.config.getYml().contains(String.valueOf(player.getUniqueId()));
	}

	public static boolean isFavorite(Player p, String map) {
		if (isStoredPlayer(p)) {
			return PlayerCache.config.getYml().getBoolean(p.getUniqueId() + ".favorite_maps." + map);
		} else {
			storePlayer(p);
		}
		return false;
	}

	public static void setFavorite(Player p, String map) {
		if (!isStoredPlayer(p)) {
			storePlayer(p);
		}
		PlayerCache.config.getYml().set(p.getUniqueId() + ".favorite_maps." + map, Boolean.TRUE);
	}

	public static void unsetFavorite(Player p, String map) {
		if (!isStoredPlayer(p)) {
			storePlayer(p);
		}
		PlayerCache.config.getYml().set(p.getUniqueId() + ".favorite_maps." + map, Boolean.FALSE);
	}

	public static List<IArena> getFavorites(Player p, String group) {
		if (!isStoredPlayer(p)) {
			storePlayer(p);
		}
		List<IArena> favoriteMaps = new ArrayList<>();
		for (IArena a : Arena.getArenas()) {
			if (a.getGroup().equalsIgnoreCase(group)) {
				if (a.getStatus().equals(GameState.waiting) || a.getStatus().equals(GameState.starting)) {
					if (PlayerCache.config.getBoolean(p.getUniqueId() + ".favorite_maps." + a.getArenaName())) {
						favoriteMaps.add(a);
					}
				}
			}
		}
		return favoriteMaps;
	}

	public static List<CachedArena> getFavoritesBungee(Player p, String group) {
		if (!isStoredPlayer(p)) {
			storePlayer(p);
		}
		List<CachedArena> favoriteMaps = new ArrayList<>();
		for (CachedArena a : ArenaManager.getArenas()) {
			if (a.getArenaGroup().equalsIgnoreCase(group)) {
				if (a.getStatus().equals(ArenaStatus.WAITING) || a.getStatus().equals(ArenaStatus.STARTING)) {
					if (PlayerCache.config.getBoolean(p.getUniqueId() + ".favorite_maps." + a.getArenaName())) {
						favoriteMaps.add(a);
					}
				}
			}
		}
		return favoriteMaps;
	}

	public static void addMapJoin(Player p, String map) {
		if (!isStoredPlayer(p)) {
			storePlayer(p);
		}
		PlayerCache.config.getYml().set(p.getUniqueId() + ".per_map_times_joined." + map, PlayerCache.config.getInt(p.getUniqueId() + ".per_map_times_joined." + map) + 1);
	}

	public static int getMapJoins(Player p, String map) {
		if (!isStoredPlayer(p)) {
			storePlayer(p);
		}
		return PlayerCache.config.getInt(p.getUniqueId() + ".per_map_times_joined." + map);
	}
}
