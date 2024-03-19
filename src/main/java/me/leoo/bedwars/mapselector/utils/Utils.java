package me.leoo.bedwars.mapselector.utils;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.proxy.BedWarsProxy;
import com.andrei1058.bedwars.proxy.api.ArenaStatus;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import lombok.experimental.UtilityClass;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.database.Yaml;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@UtilityClass
public class Utils {

    public String getSelectionsType(Player player) {
        for (String s : MapSelector.get().getMainConfig().getYml().getConfigurationSection("map-selector.selections.selections").getKeys(false)) {
            if (player.hasPermission(MapSelector.get().getMainConfig().getString("map-selector.selections.selections." + s + ".permission"))) {
                if (MapSelector.get().getMainConfig().getBoolean("map-selector.selections.selections." + s + ".unlimited")) {
                   return MapSelector.get().getMainConfig().getString("map-selector.selections.unlimited-message");
                } else {
                   return String.valueOf(MapSelector.get().getMainConfig().getInt("map-selector.selections.selections." + s + ".daily-uses"));
                }
            }
        }

        return "0";
    }

    public void joinRandomGroup(Player player, String group, boolean unlimited, boolean favorite) {
        if (MapSelector.get().getBedwarsMode().equals(BedwarsMode.PROXY)) {
            List<CachedArena> arenas = new ArrayList<>();
            List<CachedArena> arenas1;

            String noMapsMessage;

            if (favorite) {
                arenas1 = new ArrayList<>(Yaml.getFavoritesBungee(player, group));
                noMapsMessage = MapSelector.get().getMainConfig().getString("map-selector.messages.no-favorites-maps");
            } else {
                arenas1 = new ArrayList<>(ArenaManager.getArenas());
                noMapsMessage = MapSelector.get().getMainConfig().getString("map-selector.messages.no-maps");
            }

            for (CachedArena cachedArena : arenas1) {
                if (Arrays.asList(group.split(",")).contains(cachedArena.getArenaGroup())
                        && (cachedArena.getStatus().equals(ArenaStatus.WAITING) || cachedArena.getStatus().equals(ArenaStatus.STARTING))
                        && cachedArena.getCurrentPlayers() < cachedArena.getMaxPlayers()) {
                    arenas.add(cachedArena);
                }
            }

            if (arenas.isEmpty()) {
                player.sendMessage(noMapsMessage);
            } else {
                Collections.shuffle(arenas);
                arenas.sort((a1, a2) -> Integer.compare(a2.getCurrentPlayers(), a1.getCurrentPlayers()));

                joinArena(player, arenas.get(0).getArenaName(), arenas.get(0).getArenaGroup(), unlimited);
            }
        } else {
            List<IArena> arenas = new ArrayList<>();
            List<IArena> arenas1;

            String noMapsMessage;

            if (favorite) {
                arenas1 = new ArrayList<>(Yaml.getFavorites(player, group));
                noMapsMessage = MapSelector.get().getMainConfig().getString("map-selector.messages.no-favorites-maps");
            } else {
                arenas1 = new ArrayList<>(Arena.getArenas());
                noMapsMessage = MapSelector.get().getMainConfig().getString("map-selector.messages.no-maps");
            }

            for (IArena iArena : arenas1) {
                if (Arrays.asList(group.split(",")).contains(iArena.getGroup())
                        && (iArena.getStatus().equals(GameState.waiting) || iArena.getStatus().equals(GameState.starting))
                        && iArena.getPlayers().size() < iArena.getMaxPlayers()) {
                    arenas.add(iArena);
                }
            }

            if (arenas.isEmpty()) {
                player.sendMessage(noMapsMessage);
            } else {
                Collections.shuffle(arenas);
                arenas.sort((a1, a2) -> Integer.compare(a2.getPlayers().size(), a1.getPlayers().size()));

                joinArena(player, arenas.get(0).getArenaName(), arenas.get(0).getGroup(), unlimited);
            }
        }
    }

    public void joinArena(Player player, String name, String group, boolean unlimited) {
        if (MapSelector.get().getBedwarsMode().equals(BedwarsMode.PROXY)) {
            List<CachedArena> arenas = new ArrayList<>();

            for (CachedArena cachedArena : ArenaManager.getArenas()) {
                if (cachedArena.getArenaGroup().equals(group)
                        && cachedArena.getArenaName().equals(name)
                        && (cachedArena.getStatus().equals(ArenaStatus.WAITING) || cachedArena.getStatus().equals(ArenaStatus.STARTING))
                        && cachedArena.getCurrentPlayers() < cachedArena.getMaxPlayers()) {
                    arenas.add(cachedArena);
                }
            }

            if (arenas.isEmpty()) {
                player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.no-maps"));
                return;
            }

            if (BedWarsProxy.getParty().hasParty(player.getUniqueId()) && BedWarsProxy.getParty().getMembers(player.getUniqueId()).size() > 1) {
                if (!BedWarsProxy.getParty().isOwner(player.getUniqueId())) {
                    player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.not-party-leader"));
                    return;
                }
                for (UUID uuid : BedWarsProxy.getParty().getMembers(player.getUniqueId())) {
                    Player player1 = Bukkit.getPlayer(uuid);
                    arenas.get(0).addPlayer(player1, player1.getName());
                }
            } else {
                arenas.get(0).addPlayer(player, player.getName());
            }
        } else {
            List<IArena> arenas = new ArrayList<>();

            for (IArena iArena : Arena.getArenas()) {
                if (iArena.getGroup().equals(group)
                        && iArena.getArenaName().equals(name)
                        && (iArena.getStatus().equals(GameState.waiting) || iArena.getStatus().equals(GameState.starting))
                        && iArena.getPlayers().size() < iArena.getMaxPlayers()) {
                    arenas.add(iArena);
                }
            }

            if (arenas.isEmpty()) {
                player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.no-maps"));
                return;
            }

            if (BedWars.getParty().hasParty(player) && BedWars.getParty().getMembers(player).size() > 1) {
                if (!BedWars.getParty().isOwner(player)) {
                    player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.not-party-leader"));
                    return;
                }
                for (Player player1 : BedWars.getParty().getMembers(player)) {
                    arenas.get(0).addPlayer(player1, false);
                }
            } else {
                arenas.get(0).addPlayer(player, false);
            }
        }

        Yaml.addMapJoin(player, name);

        if (!unlimited) {
            MapSelector.get().getDatabaseManager().setPlayerUses(player.getUniqueId(), MapSelector.get().getDatabaseManager().getPlayerUses(player.getUniqueId()) + 1);
        }
    }

    public void checkDate() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (day != MapSelector.get().getMainConfig().getInt("map-selector.last-date")) {
            MapSelector.get().getDatabaseManager().setAllPlayersUses(0);
            MapSelector.get().getMainConfig().set("map-selector.last-date", day);
        }
    }
}
