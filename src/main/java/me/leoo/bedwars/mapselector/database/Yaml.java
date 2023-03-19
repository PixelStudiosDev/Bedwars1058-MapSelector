package me.leoo.bedwars.mapselector.database;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.proxy.api.ArenaStatus;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import me.leoo.bedwars.mapselector.MapSelector;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Yaml {

    public Yaml(){}

    public void storePlayer(Player player) {
        MapSelector.getPlugin().getCacheConfig().getYml().set(player.getUniqueId() + ".favorite_maps", Collections.emptyList());
        MapSelector.getPlugin().getCacheConfig().getYml().set(player.getUniqueId() + ".per_map_times_joined", Collections.emptyList());
    }

    public boolean isStoredPlayer(Player player) {
        return MapSelector.getPlugin().getCacheConfig().getYml().contains(String.valueOf(player.getUniqueId()));
    }

    public void checkStored(Player player){
        if (!isStoredPlayer(player)) {
            storePlayer(player);
        }
    }

    public boolean isFavorite(Player player, String map) {
        if (isStoredPlayer(player)) {
            return MapSelector.getPlugin().getCacheConfig().getYml().getBoolean(player.getUniqueId() + ".favorite_maps." + map);
        } else {
            storePlayer(player);
        }
        return false;
    }

    public void setFavorite(Player player, String map) {
        checkStored(player);
        MapSelector.getPlugin().getCacheConfig().getYml().set(player.getUniqueId() + ".favorite_maps." + map, Boolean.TRUE);
    }

    public void unsetFavorite(Player player, String map) {
        checkStored(player);
        MapSelector.getPlugin().getCacheConfig().getYml().set(player.getUniqueId() + ".favorite_maps." + map, Boolean.FALSE);
    }

    public List<IArena> getFavorites(Player player, String group) {
        checkStored(player);
        List<IArena> favoriteMaps = new ArrayList<>();
        for (IArena arena : Arena.getArenas()) {
            if (arena.getGroup().equalsIgnoreCase(group) && (arena.getStatus().equals(GameState.waiting) || arena.getStatus().equals(GameState.starting)) && MapSelector.getPlugin().getCacheConfig().getBoolean(player.getUniqueId() + ".favorite_maps." + arena.getArenaName())) {
                favoriteMaps.add(arena);
            }
        }
        return favoriteMaps;
    }

    public List<CachedArena> getFavoritesBungee(Player player, String group) {
        checkStored(player);
        List<CachedArena> favoriteMaps = new ArrayList<>();
        for (CachedArena arena : ArenaManager.getArenas()) {
            if (arena.getArenaGroup().equalsIgnoreCase(group) && (arena.getStatus().equals(ArenaStatus.WAITING) || arena.getStatus().equals(ArenaStatus.STARTING)) && MapSelector.getPlugin().getCacheConfig().getBoolean(player.getUniqueId() + ".favorite_maps." + arena.getArenaName())) {
                favoriteMaps.add(arena);
            }
        }
        return favoriteMaps;
    }

    public void addMapJoin(Player player, String map) {
        checkStored(player);
        MapSelector.getPlugin().getCacheConfig().getYml().set(player.getUniqueId() + ".per_map_times_joined." + map, MapSelector.getPlugin().getCacheConfig().getInt(player.getUniqueId() + ".per_map_times_joined." + map) + 1);
    }

    public int getMapJoins(Player player, String map) {
        checkStored(player);
        return MapSelector.getPlugin().getCacheConfig().getInt(player.getUniqueId() + ".per_map_times_joined." + map);
    }
}
