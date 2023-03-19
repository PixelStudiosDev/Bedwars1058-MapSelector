package me.leoo.bedwars.mapselector.utils;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.proxy.BedWarsProxy;
import com.andrei1058.bedwars.proxy.api.ArenaStatus;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import me.leoo.bedwars.mapselector.MapSelector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class SelectorUtil {

    private static final HashMap<Player, Integer> startMaps = new HashMap<>();
    private static final HashMap<Player, Integer> page = new HashMap<>();

    public static String firstLetterUpperCase(String string) {
        if (string == null) return "";
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    public static String getSelectionsType(Player player) {
        String type = String.valueOf(0);
        for (String s : MapSelector.getPlugin().getMainConfig().getYml().getConfigurationSection("map_selector.selections_type").getKeys(false)) {
            if (player.hasPermission(MapSelector.getPlugin().getMainConfig().getString("map_selector.selections_type." + s + ".permission"))) {
                if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.selections_type." + s + ".unlimited")) {
                    type = MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.unlimited_message");
                } else {
                    type = String.valueOf(MapSelector.getPlugin().getMainConfig().getInt("map_selector.selections_type." + s + ".daily_uses"));
                }
            }
        }
        return type;
    }

    public static void joinRandomGroup(Player player, String group, boolean unlimited, boolean favorite) {
        if (MapSelector.getPlugin().getBedwarsMode().equals(BedwarsMode.BEDWARSPROXY)) {
            List<CachedArena> arenas;

            if (favorite) {
                arenas = new ArrayList<>(MapSelector.getPlugin().getYamlConfig().getFavoritesBungee(player, group));
            } else {
                arenas = new ArrayList<>(ArenaManager.getArenas());
            }

            arenas.removeIf(arena -> (arena.getArenaGroup().equalsIgnoreCase(group) && arena.getStatus().equals(ArenaStatus.WAITING) || arena.getStatus().equals(ArenaStatus.STARTING) && arena.getCurrentPlayers() < (arena.getMaxPlayers() + 1)));

            if (arenas.isEmpty()) {
                player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.no_maps"));
            } else {
                arenas.sort((arena1, arena2) -> Integer.compare(arena2.getCurrentPlayers(), arena1.getCurrentPlayers()));
                joinArena(player, arenas.get(0).getArenaName(), group, unlimited);
            }
        } else {
            List<IArena> arenas;

            if (favorite) {
                arenas = new ArrayList<>(MapSelector.getPlugin().getYamlConfig().getFavorites(player, group));
            } else {
                arenas = new ArrayList<>(Arena.getArenas());
            }

            arenas.removeIf(arena -> (arena.getGroup().equalsIgnoreCase(group) && arena.getStatus().equals(GameState.waiting) || arena.getStatus().equals(GameState.starting) && arena.getPlayers().size() < (arena.getMaxPlayers() + 1)));

            if (arenas.isEmpty()) {
                player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.no_maps"));
            } else {
                arenas.sort((arena1, arena2) -> Integer.compare(arena2.getPlayers().size(), arena1.getPlayers().size()));
                joinArena(player, arenas.get(0).getArenaName(), group, unlimited);
            }
        }
    }

    public static void joinArena(Player player, String name, String group, boolean unlimited) {
        if (MapSelector.getPlugin().getBedwarsMode().equals(BedwarsMode.BEDWARSPROXY)) {
            List<CachedArena> arena = ArenaManager.getArenas().stream().filter(arena1 -> arena1.getArenaName().equals(name)
                            && arena1.getArenaGroup().equalsIgnoreCase(group)
                            && (arena1.getStatus().equals(ArenaStatus.WAITING) || arena1.getStatus().equals(ArenaStatus.STARTING)))
                    .collect(Collectors.toList());

            if (arena.isEmpty()) return;

            if (BedWarsProxy.getParty().hasParty(player.getUniqueId()) && BedWarsProxy.getParty().getMembers(player.getUniqueId()).size() > 1) {
                if (!BedWarsProxy.getParty().isOwner(player.getUniqueId())) {
                    player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.not_party_leader"));
                    return;
                }
                for (UUID uuid : BedWarsProxy.getParty().getMembers(player.getUniqueId())) {
                    Player player1 = Bukkit.getPlayer(uuid);
                    arena.get(0).addPlayer(player1, player1.getName());
                }
            } else {
                arena.get(0).addPlayer(player, player.getName());
            }
        } else {
            if (BedWars.getParty().hasParty(player) && BedWars.getParty().getMembers(player).size() > 1) {
                if (!BedWars.getParty().isOwner(player)) {
                    player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.not_party_leader"));
                    return;
                }
                for (UUID uuid : BedWarsProxy.getParty().getMembers(player.getUniqueId())) {
                    Arena.getArenaByName(name).addPlayer(Bukkit.getPlayer(uuid), false);
                }
            } else {
                Arena.getArenaByName(name).addPlayer(player, false);
            }
        }

        MapSelector.getPlugin().getYamlConfig().addMapJoin(player, name);

        if (!unlimited) {
            MapSelector.getPlugin().getMapSelectorDatabase().setPlayerUses(player.getUniqueId(), MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId()) + 1);
        }
    }

    public static boolean isOldDate() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (day == MapSelector.getPlugin().getMainConfig().getInt("map_selector.last-date")) {
            return false;
        } else {
            MapSelector.getPlugin().getMainConfig().getYml().set("current-date", day);
            return true;
        }
    }

    public static int getCurrentPage(Player player) {
        if (page.containsKey(player)) {
            return page.get(player);
        }
        return -0;
    }

    public static int getStartMaps(Player player) {
        if (startMaps.containsKey(player)) {
            return startMaps.get(player);
        }
        return 0;
    }

    public static HashMap<Player, Integer> getStartMaps() {
        return startMaps;
    }

    public static HashMap<Player, Integer> getPage() {
        return page;
    }
}
