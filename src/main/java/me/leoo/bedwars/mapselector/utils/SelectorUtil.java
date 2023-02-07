package me.leoo.bedwars.mapselector.utils;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.proxy.BedWarsProxy;
import com.andrei1058.bedwars.proxy.api.ArenaStatus;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.api.Messages;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import com.andrei1058.bedwars.proxy.language.LanguageManager;
import me.leoo.bedwars.mapselector.Main;
import me.leoo.bedwars.mapselector.configuration.Config;
import me.leoo.bedwars.mapselector.database.Yaml;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SelectorUtil {

	public static String getSelectionsType(Player p){
		String type = String.valueOf(0);
		//if(Config.config.getString("data-save").equalsIgnoreCase("yaml")){
			for(String s : Config.config.getYml().getConfigurationSection("map_selector.selections_type").getKeys(false)){
				String permission = Config.config.getString("map_selector.selections_type." + s + ".permission");
				int daily_uses = Config.config.getInt("map_selector.selections_type." + s + ".daily_uses");
				boolean unlimited = Config.config.getBoolean("map_selector.selections_type." + s + ".unlimited");
				if(p.hasPermission(permission)){
					if(unlimited){
						type = Config.config.getString("map_selector.selection.unlimited_message");
					}else{
						type = String.valueOf(daily_uses);
					}
				}
			}
		//}
		return type;
	}

	public static void joinRandomGroup(Player p, String group, boolean unlimited, boolean favorite) {
		if(Main.bungee){

			if(favorite){
				List<CachedArena> favarenas = new ArrayList<>(Yaml.getFavoritesBungee(p, group));
				if(favarenas.isEmpty()){
					p.sendMessage(Config.config.getString("map_selector.menu.no_favorites_maps"));
				}else{
					for (CachedArena a : favarenas) {
						if (a.getCurrentPlayers() >= a.getMaxPlayers()) continue;
						joinArena(p, a.getArenaName(), group, unlimited);
					}
				}
			}else{
				List<CachedArena> arenas = new ArrayList<>();
				ArenaManager.getArenas().forEach(a -> {
					if(a.getArenaGroup().equalsIgnoreCase(group)){
						if (a.getStatus().equals(ArenaStatus.WAITING) || a.getStatus().equals(ArenaStatus.STARTING)) {
							if(a.getCurrentPlayers() < a.getMaxPlayers()) arenas.add(a);
						}
					}
				});
				if(arenas.isEmpty()){
					p.sendMessage(Config.config.getString("map_selector.menu.no_maps"));
				}else{
					Collections.sort(arenas, (a1, a2) -> {
						if (a1.getCurrentPlayers() > a2.getCurrentPlayers()) return -1;
						if (a1.getCurrentPlayers() > a2.getCurrentPlayers()) return 1;
						return 0;
					});
					joinArena(p, arenas.get(0).getArenaName(), group, unlimited);
				}
			}

		}else{

			if(favorite){
				List<IArena> favarenas = new ArrayList<>(Yaml.getFavorites(p, group));
				if(favarenas.isEmpty()){
					p.sendMessage(Config.config.getString("map_selector.menu.no_favorites_maps"));
				}else{
					for (IArena a : favarenas) {
						if (a.getPlayers().size() >= a.getMaxPlayers()) continue;
						joinArena(p, a.getArenaName(), group, unlimited);
					}
				}
			}else{
				List<IArena> arenas = new ArrayList<>();
				Arena.getArenas().forEach(a -> {
					if(a.getGroup().equalsIgnoreCase(group)){
						if (a.getStatus().equals(GameState.waiting) || a.getStatus().equals(GameState.starting)) {
							if(a.getPlayers().size() < a.getMaxPlayers()) arenas.add(a);
						}
					}
				});
				if(arenas.isEmpty()){
					p.sendMessage(Config.config.getString("map_selector.menu.no_maps"));
				}else{
					Collections.sort(arenas, (a1, a2) -> {
						if (a1.getPlayers().size() > a2.getPlayers().size()) return -1;
						if (a1.getPlayers().size() > a2.getPlayers().size()) return 1;
						return 0;
					});
					joinArena(p, arenas.get(0).getArenaName(), group, unlimited);
				}
			}
		}
	}

	public static void joinArena(Player p, String map, String group, boolean unlimited) {
		if(Main.bungee){
			ArrayList<CachedArena> arenas = new ArrayList<>();
			ArenaManager.getArenas().forEach(a -> {
				if (a.getArenaName().equals(map)) {
					if(a.getArenaGroup().equalsIgnoreCase(group)){
						if (a.getStatus().equals(ArenaStatus.WAITING) || a.getStatus().equals(ArenaStatus.STARTING)) {
							arenas.add(a);
						}
					}

				}
			});
			if(arenas.isEmpty())
				return;
			if(BedWarsProxy.getParty().hasParty(p.getUniqueId())){
				if(BedWarsProxy.getParty().getMembers(p.getUniqueId()).size() > 1){
					if(!BedWarsProxy.getParty().isOwner(p.getUniqueId())){
						p.sendMessage(Config.config.getString("map_selector.menu.not_party_leader"));
						return;
					}
					for(UUID uuid : BedWarsProxy.getParty().getMembers(p.getUniqueId())){
						Player pl = Bukkit.getPlayer(uuid);
						arenas.get(0).addPlayer(pl, pl.getName());
					}
				}else{
					arenas.get(0).addPlayer(p, p.getName());
				}
			}else{
				arenas.get(0).addPlayer(p, p.getName());
			}
		}else{
			if(BedWars.getParty().hasParty(p)){
				if(BedWars.getParty().getMembers(p).size() > 1){
					if(!BedWars.getParty().isOwner(p)){
						p.sendMessage(Config.config.getString("map_selector.menu.not_party_leader"));
						return;
					}
					for(Player pl : BedWars.getParty().getMembers(p)){
						Arena.getArenaByName(map).addPlayer(pl, false);
					}
				}else{
					Arena.getArenaByName(map).addPlayer(p, false);
				}
			}else{
				Arena.getArenaByName(map).addPlayer(p, false);
			}
		}
		Yaml.addMapJoin(p, map);
		if(!unlimited) {
			Main.database.setPlayerUses(p.getUniqueId(), Main.database.getPlayerUses(p.getUniqueId()) + 1);
		}
	}

	public static int getDate(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static boolean isOldDate() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if(day == Config.config.getInt("current-date")){
			return  false;
		}else {
			Config.config.set("current-date", day);
			return true;
		}
	}
}
