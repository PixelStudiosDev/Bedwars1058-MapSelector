package me.leoo.bedwars.mapselector.database;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.proxy.api.ArenaStatus;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import me.leoo.bedwars.mapselector.configuration.PlayerCache;
import org.bukkit.entity.Player;

import java.util.*;

public class Yaml {

	public static void storePlayer(Player p) {
		//if(Config.config.getString("data-save").equalsIgnoreCase("yaml")){
			PlayerCache.cache.set(p.getUniqueId() + ".favorite_maps", Arrays.asList());
			PlayerCache.cache.set(p.getUniqueId() + ".per_map_times_joined", Arrays.asList());
		//}
	}

	public static boolean isStoredPlayer(Player p){
		//if(Config.config.getString("data-save").equalsIgnoreCase("yaml")){
		//}
		return PlayerCache.cache.getYml().contains(String.valueOf(p.getUniqueId()));
	}

	public static boolean isFavorite(Player p, String map) {
		boolean favorite = false;
		//if(Config.config.getString("data-save").equalsIgnoreCase("yaml")){
			if(isStoredPlayer(p)){
				if(PlayerCache.cache.getYml().getBoolean(p.getUniqueId() + ".favorite_maps." + map)){
					favorite = true;
				}
			}else{
				storePlayer(p);
			}
		//}
		return favorite;
	}

	public static void setFavorite(Player p, String map) {
		//if(Config.config.getString("data-save").equalsIgnoreCase("yaml")){
			if(isStoredPlayer(p)){
				PlayerCache.cache.set(p.getUniqueId() + ".favorite_maps." + map, Boolean.TRUE);
			}else{
				storePlayer(p);
				PlayerCache.cache.set(p.getUniqueId() + ".favorite_maps." + map, Boolean.TRUE);
			}
		//}
	}

	public static void unsetFavorite(Player p, String map) {
		//if(Config.config.getString("data-save").equalsIgnoreCase("yaml")){
			if(isStoredPlayer(p)){
				PlayerCache.cache.set(p.getUniqueId() + ".favorite_maps." + map, Boolean.FALSE);
			}else{
				storePlayer(p);
				PlayerCache.cache.set(p.getUniqueId() + ".favorite_maps." + map, Boolean.FALSE);
			}
		//}
	}

	public static List<IArena> getFavorites(Player p, String group) {
		if(isStoredPlayer(p)){
			List<IArena> favoriteMaps = new ArrayList<>();
			for(IArena a : Arena.getArenas()){
				if(a.getGroup().equalsIgnoreCase(group)){
					if(a.getStatus().equals(GameState.waiting) || a.getStatus().equals(GameState.starting)){
						if(PlayerCache.cache.getBoolean(p.getUniqueId() + ".favorite_maps." + a.getArenaName())){
							favoriteMaps.add(a);
						}
					}
				}
			}
			return favoriteMaps;
		}else{
			storePlayer(p);

			List<IArena> favoriteMaps = new ArrayList<>();
			for(IArena a : Arena.getArenas()){
				if(a.getGroup().equalsIgnoreCase(group)){
					if(a.getStatus().equals(GameState.waiting) || a.getStatus().equals(GameState.starting)){
						if(PlayerCache.cache.getBoolean(p.getUniqueId() + ".favorite_maps." + a.getArenaName())){
							favoriteMaps.add(a);
						}
					}
				}
			}
			return favoriteMaps;
		}
	}

	public static List<CachedArena> getFavoritesBungee(Player p, String group) {
		if(isStoredPlayer(p)){
			List<CachedArena> favoriteMaps = new ArrayList<>();
			for(CachedArena a : ArenaManager.getArenas()){
				if(a.getArenaGroup().equalsIgnoreCase(group)){
					if(a.getStatus().equals(ArenaStatus.WAITING) || a.getStatus().equals(ArenaStatus.STARTING)){
						if(PlayerCache.cache.getBoolean(p.getUniqueId() + ".favorite_maps." + a.getArenaName())){
							favoriteMaps.add(a);
						}
					}
				}
			}
			return favoriteMaps;
		}else{
			storePlayer(p);
			List<CachedArena> favoriteMaps = new ArrayList<>();
			for(CachedArena a : ArenaManager.getArenas()){
				if(a.getArenaGroup().equalsIgnoreCase(group)){
					if(a.getStatus().equals(ArenaStatus.WAITING) || a.getStatus().equals(ArenaStatus.STARTING)){
						if(PlayerCache.cache.getBoolean(p.getUniqueId() + ".favorite_maps." + a.getArenaName())){
							favoriteMaps.add(a);
						}
					}
				}
			}
			return favoriteMaps;
		}
	}

	public static void addMapJoin(Player p, String map) {
		//if(Config.config.getString("data-save").equalsIgnoreCase("yaml")){
			if(isStoredPlayer(p)){
				int mapJoins = PlayerCache.cache.getInt(p.getUniqueId() + ".per_map_times_joined." + map);
				PlayerCache.cache.set(p.getUniqueId() + ".per_map_times_joined." + map, mapJoins + 1);
			}else{
				storePlayer(p);
				int mapJoins = PlayerCache.cache.getInt(p.getUniqueId() + ".per_map_times_joined." + map);
				PlayerCache.cache.set(p.getUniqueId() + ".per_map_times_joined." + map, mapJoins + 1);
			}
		//}
	}

	public static int getMapJoins(Player p, String map) {
		int joins;
		//if(Config.config.getString("data-save").equalsIgnoreCase("yaml")){
			if(isStoredPlayer(p)){
				joins = PlayerCache.cache.getInt(p.getUniqueId() + ".per_map_times_joined." + map);
			}else{
				storePlayer(p);
				joins = PlayerCache.cache.getInt(p.getUniqueId() + ".per_map_times_joined." + map);
			}
		//}
		return joins;
	}
}
