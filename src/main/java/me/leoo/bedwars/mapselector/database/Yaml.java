package me.leoo.bedwars.mapselector.database;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.proxy.api.ArenaStatus;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import lombok.experimental.UtilityClass;
import me.leoo.bedwars.mapselector.MapSelector;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class Yaml {

    public void storePlayer(Player player) {
        MapSelector.get().getCacheConfig().set(player.getUniqueId() + ".favorite-maps", Collections.emptyList());
        MapSelector.get().getCacheConfig().set(player.getUniqueId() + ".per-map-times-joined", Collections.emptyList());
    }

    public boolean isStoredPlayer(Player player) {
        return MapSelector.get().getCacheConfig().getYml().contains(String.valueOf(player.getUniqueId()));
    }

    public void checkStored(Player player) {
        if (!isStoredPlayer(player)) {
            storePlayer(player);
        }
    }

    public boolean isFavorite(Player player, String map) {
        checkStored(player);
        return MapSelector.get().getCacheConfig().getYml().getBoolean(player.getUniqueId() + ".favorite-maps." + map);
    }

    public void setFavorite(Player player, String map) {
        checkStored(player);
        MapSelector.get().getCacheConfig().set(player.getUniqueId() + ".favorite-maps." + map, Boolean.TRUE);
    }

    public void unsetFavorite(Player player, String map) {
        checkStored(player);
        MapSelector.get().getCacheConfig().set(player.getUniqueId() + ".favorite-maps." + map, Boolean.FALSE);
    }

    public List<IArena> getFavorites(Player player, String group) {
        checkStored(player);
        List<IArena> favoriteMaps = new ArrayList<>();
        for (IArena arena : Arena.getArenas()) {
            if (arena.getGroup().equalsIgnoreCase(group) && (arena.getStatus().equals(GameState.waiting) || arena.getStatus().equals(GameState.starting)) && MapSelector.get().getCacheConfig().getBoolean(player.getUniqueId() + ".favorite-maps." + arena.getArenaName())) {
                favoriteMaps.add(arena);
            }
        }
        return favoriteMaps;
    }

    public List<CachedArena> getFavoritesBungee(Player player, String group) {
        checkStored(player);
        List<CachedArena> favoriteMaps = new ArrayList<>();
        for (CachedArena arena : ArenaManager.getArenas()) {
            if (arena.getArenaGroup().equalsIgnoreCase(group) && (arena.getStatus().equals(ArenaStatus.WAITING) || arena.getStatus().equals(ArenaStatus.STARTING)) && MapSelector.get().getCacheConfig().getBoolean(player.getUniqueId() + ".favorite-maps." + arena.getArenaName())) {
                favoriteMaps.add(arena);
            }
        }
        return favoriteMaps;
    }

    public void addMapJoin(Player player, String map) {
        checkStored(player);
        MapSelector.get().getCacheConfig().set(player.getUniqueId() + ".per-map-times-joined." + map, MapSelector.get().getCacheConfig().getInt(player.getUniqueId() + ".per-map-times-joined." + map) + 1);
    }

    public int getMapJoins(Player player, String map) {
        checkStored(player);
        return MapSelector.get().getCacheConfig().getInt(player.getUniqueId() + ".per-map-times-joined." + map);
    }
}
