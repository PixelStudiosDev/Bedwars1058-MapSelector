package me.leoo.bedwars.mapselector.menu;

import com.andrei1058.bedwars.proxy.api.ArenaStatus;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import com.andrei1058.bedwars.proxy.language.LanguageManager;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.database.Yaml;
import me.leoo.bedwars.mapselector.utils.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SelectorMenuProxy {

    protected SelectorMenuProxy() {
    }

    public static void openFirstGui(Player player, String group) {
        Inventory inventory = Bukkit.createInventory(null, MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.bedwars-menu.slots"), MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.title"));

        for (String extraItems : MapSelector.getPlugin().getMainConfig().getYml().getConfigurationSection("map-selector.menus.bedwars-menu.items").getKeys(false)) {
            if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.bedwars-menu.items." + extraItems + ".enabled") &&
                    MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.bedwars-menu.items." + extraItems + ".extra")) {
                inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.bedwars-menu.items." + extraItems + ".slot"),
                        Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items." + extraItems + ".material")),
                                MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items." + extraItems + ".head-value"),
                                MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.bedwars-menu.items." + extraItems + ".data"),
                                MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items." + extraItems + ".name"),
                                MapSelector.getPlugin().getMainConfig().getList("map-selector.menus.bedwars-menu.items." + extraItems + ".lore"),
                                MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.bedwars-menu.items." + extraItems + ".enchanted"),
                                null, null, null, null, null
                        )
                );
            }
        }

        List<String> joinRandomLore = new ArrayList<>();
        for (String s : MapSelector.getPlugin().getMainConfig().getList("map-selector.menus.bedwars-menu.items.join-random.lore")) {
            s = s.replace("{groupName}", Misc.firstLetterUpperCase(group));
            joinRandomLore.add(s);
        }

        if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.bedwars-menu.items.join-random.enabled")) {
            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.bedwars-menu.items.join-random.slot"),
                    Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items.join-random.material")),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items.join-random.head-value"),
                            MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.bedwars-menu.items.join-random.data"),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items.join-random.name").replace("{groupName}", Misc.firstLetterUpperCase(group)),
                            joinRandomLore,
                            MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.bedwars-menu.items.join-random.enchanted"),
                            group, null, null, null, null
                    )
            );
        }
        if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.bedwars-menu.items.map-selector.enabled")) {
            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.bedwars-menu.items.map-selector.slot"),
                    Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items.map-selector.material")),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items.map-selector.head-value"),
                            MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.bedwars-menu.items.map-selector.data"),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items.map-selector.name").replace("{groupName}", Misc.firstLetterUpperCase(group)),
                            MapSelector.getPlugin().getMainConfig().getList("map-selector.menus.bedwars-menu.items.map-selector.lore"),
                            MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.bedwars-menu.items.map-selector.enchanted"),
                            group, null, null, null, null
                    )
            );
        }
        if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.bedwars-menu.items.close.enabled")) {
            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.bedwars-menu.items.close.slot"),
                    Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items.close.material")),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items.close.head-value"),
                            MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.bedwars-menu.items.close.data"),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items.close.name"),
                            MapSelector.getPlugin().getMainConfig().getList("map-selector.menus.bedwars-menu.items.close.lore"),
                            MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.bedwars-menu.items.close.enchanted"),
                            group, null, null, null, null
                    )
            );
        }
        if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.bedwars-menu.items.rejoin.enabled")) {
            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.bedwars-menu.items.rejoin.slot"),
                    Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items.rejoin.material")),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items.rejoin.head-value"),
                            MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.bedwars-menu.items.rejoin.data"),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.bedwars-menu.items.rejoin.name"),
                            MapSelector.getPlugin().getMainConfig().getList("map-selector.menus.bedwars-menu.items.rejoin.lore"),
                            MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.bedwars-menu.items.rejoin.enchanted"),
                            group, null, null, null, null
                    )
            );
        }

        player.openInventory(inventory);
    }

    public static void openSecondGui(Player player, String group, int page) {
        Inventory inventory = Bukkit.createInventory(null, MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.slots"), MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.title"));

        List<String> groups;
        groups = Arrays.asList(group.split(","));

        Misc.checkDate();

        List<CachedArena> arenas = new ArrayList<>();
        for (CachedArena arena : ArenaManager.getArenas()) {
            if (groups.contains(arena.getArenaGroup()) && arena.getStatus().equals(ArenaStatus.WAITING) || arena.getStatus().equals(ArenaStatus.STARTING)) {
                arenas.add(arena);
            }
        }
        if (arenas.isEmpty()) {
            player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.no-maps"));
            return;
        }

        com.andrei1058.bedwars.proxy.api.Language language = LanguageManager.get().getPlayerLanguage(player);
        arenas.sort(Comparator.comparing(arena -> arena.getDisplayName(language)));

        for (String extraItems : MapSelector.getPlugin().getMainConfig().getYml().getConfigurationSection("map-selector.menus.maps-menu.items").getKeys(false)) {
            if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items." + extraItems + ".enabled")
                    && MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items." + extraItems + ".extra")) {
                inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items." + extraItems + ".slot"),
                        Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items." + extraItems + ".material")),
                                MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items." + extraItems + ".head-value"),
                                MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items." + extraItems + ".data"),
                                MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items." + extraItems + ".name"),
                                MapSelector.getPlugin().getMainConfig().getList("map-selector.menus.maps-menu.items." + extraItems + ".lore"),
                                MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items." + extraItems + ".enchanted"),
                                null, null, null, null, null
                        )
                );
            }
        }

        String[] slots = MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.maps-slots").split(",");

        int mapsIndex = slots.length * page;

        for (String slot : slots) {
            if (mapsIndex >= arenas.size()) continue;

            List<String> mapLore = new ArrayList<>();
            for (String s : MapSelector.getPlugin().getMainConfig().getList("map-selector.menus.maps-menu.items.map.lore")) {
                s = s.replace("{groupName}", Misc.firstLetterUpperCase(arenas.get(mapsIndex).getArenaGroup()));
                s = s.replace("{availableGames}", "1");
                s = s.replace("{timesJoined}", String.valueOf(Yaml.getMapJoins(player, arenas.get(mapsIndex).getArenaName())));
                s = s.replace("{selectionsType}", Misc.getSelectionsType(player));
                s = s.replace("{remainingUses}", Misc.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map-selector.selections.unlimited-message")) ? MapSelector.getPlugin().getMainConfig().getString("map-selector.selections.unlimited-message") : String.valueOf(Integer.parseInt(Misc.getSelectionsType(player)) - MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId())));
                s = s.replace("{status}", arenas.get(mapsIndex).getStatus().name());
                s = s.replace("{on}", String.valueOf(arenas.get(mapsIndex).getCurrentPlayers()));
                s = s.replace("{max}", String.valueOf(arenas.get(mapsIndex).getMaxPlayers()));
                mapLore.add(s);
            }

            List<String> mapFavoriteLore = new ArrayList<>();
            for (String s : MapSelector.getPlugin().getMainConfig().getList("map-selector.menus.maps-menu.items.map-favorite.lore")) {
                s = s.replace("{groupName}", Misc.firstLetterUpperCase(arenas.get(mapsIndex).getArenaGroup()));
                s = s.replace("{availableGames}", "1");
                s = s.replace("{timesJoined}", String.valueOf(Yaml.getMapJoins(player, arenas.get(mapsIndex).getArenaName())));
                s = s.replace("{selectionsType}", Misc.getSelectionsType(player));
                s = s.replace("{remainingUses}", Misc.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map-selector.selections.unlimited-message")) ? MapSelector.getPlugin().getMainConfig().getString("map-selector.selections.unlimited-message") : String.valueOf(Integer.parseInt(Misc.getSelectionsType(player)) - MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId())));
                s = s.replace("{status}", arenas.get(mapsIndex).getStatus().name());
                s = s.replace("{on}", String.valueOf(arenas.get(mapsIndex).getCurrentPlayers()));
                s = s.replace("{max}", String.valueOf(arenas.get(mapsIndex).getMaxPlayers()));
                mapFavoriteLore.add(s);
            }

            List<String> mapNoUsesNoPermissionsLore = new ArrayList<>();
            for (String s : MapSelector.getPlugin().getMainConfig().getList("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.lore")) {
                s = s.replace("{groupName}", Misc.firstLetterUpperCase(arenas.get(mapsIndex).getArenaGroup()));
                s = s.replace("{availableGames}", "1");
                s = s.replace("{timesJoined}", String.valueOf(Yaml.getMapJoins(player, arenas.get(mapsIndex).getArenaName())));
                s = s.replace("{selectionsType}", Misc.getSelectionsType(player));
                s = s.replace("{remainingUses}", Misc.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map-selector.selections.unlimited-message")) ? MapSelector.getPlugin().getMainConfig().getString("map-selector.selections.unlimited-message") : String.valueOf(Integer.parseInt(Misc.getSelectionsType(player)) - MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId())));
                s = s.replace("{status}", arenas.get(mapsIndex).getStatus().name());
                s = s.replace("{on}", String.valueOf(arenas.get(mapsIndex).getCurrentPlayers()));
                s = s.replace("{max}", String.valueOf(arenas.get(mapsIndex).getMaxPlayers()));
                mapNoUsesNoPermissionsLore.add(s);
            }

            if (Misc.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map-selector.selections.unlimited-message"))) {
                if (Yaml.isFavorite(player, String.valueOf(arenas.get(mapsIndex).getArenaName()))) {
                    if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.map-favorite.enabled")) {
                        inventory.setItem(Integer.parseInt(slot),
                                Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map-favorite.material")),
                                        MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map-favorite.head-value"),
                                        MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.map-favorite.data"),
                                        MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map-favorite.name").replace("{mapName}", arenas.get(mapsIndex).getDisplayName(language)),
                                        mapFavoriteLore,
                                        MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.map-favorite.enchanted"),
                                        arenas.get(mapsIndex).getArenaName(), arenas.get(mapsIndex).getArenaGroup(), arenas.get(mapsIndex).getDisplayName(language), group, String.valueOf(page)
                                )
                        );
                    }
                } else {
                    if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.map.enabled")) {
                        inventory.setItem(Integer.parseInt(slot),
                                Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map.material")),
                                        MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map.head-value"),
                                        MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.map.data"),
                                        MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map.name").replace("{mapName}", arenas.get(mapsIndex).getDisplayName(language)),
                                        mapLore,
                                        MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.map.enchanted"),
                                        arenas.get(mapsIndex).getArenaName(), arenas.get(mapsIndex).getArenaGroup(), arenas.get(mapsIndex).getDisplayName(language), group, String.valueOf(page)
                                )
                        );
                    }
                }
            } else {
                if (player.hasPermission(MapSelector.getPlugin().getMainConfig().getString("map-selector.selections.permission"))
                        && MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId()) < Integer.parseInt(Misc.getSelectionsType(player))) {
                    if (Yaml.isFavorite(player, String.valueOf(arenas.get(mapsIndex).getArenaName()))) {
                        if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.map-favorite.enabled")) {
                            inventory.setItem(Integer.parseInt(slot),
                                    Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map-favorite.material")),
                                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map-favorite.head-value"),
                                            MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.map-favorite.data"),
                                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map-favorite.name").replace("{mapName}", arenas.get(mapsIndex).getDisplayName(language)),
                                            mapFavoriteLore,
                                            MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.map-favorite.enchanted"),
                                            arenas.get(mapsIndex).getArenaName(), arenas.get(mapsIndex).getArenaGroup(), arenas.get(mapsIndex).getDisplayName(language), group, String.valueOf(page)
                                    )
                            );
                        }
                    } else {
                        if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.map.enabled")) {
                            inventory.setItem(Integer.parseInt(slot),
                                    Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map.material")),
                                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map.head-value"),
                                            MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.map.data"),
                                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map.name").replace("{mapName}", arenas.get(mapsIndex).getDisplayName(language)),
                                            mapLore,
                                            MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.map.enchanted"),
                                            arenas.get(mapsIndex).getArenaName(), arenas.get(mapsIndex).getArenaGroup(), arenas.get(mapsIndex).getDisplayName(language), group, String.valueOf(page)
                                    )
                            );
                        }
                    }
                } else {
                    if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.enabled")) {
                        inventory.setItem(Integer.parseInt(slot),
                                Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.material")),
                                        MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.head-value"),
                                        MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.data"),
                                        MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.name").replace("{mapName}", arenas.get(mapsIndex).getDisplayName(language)),
                                        mapNoUsesNoPermissionsLore,
                                        MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.enchanted"),
                                        arenas.get(mapsIndex).getArenaName(), arenas.get(mapsIndex).getArenaGroup(), arenas.get(mapsIndex).getDisplayName(language), null, null
                                )
                        );
                    }
                }
            }

            mapsIndex++;
        }

        if (arenas.size() - (slots.length * page) > slots.length &&
                MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.next-page.enabled")) {
            List<String> nextPageLore = new ArrayList<>();
            for (String s : MapSelector.getPlugin().getMainConfig().getList("map-selector.menus.maps-menu.items.next-page.lore")) {
                s = s.replace("{nextPage}", String.valueOf(page + 2));
                nextPageLore.add(s);
            }

            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.next-page.slot"),
                    Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.next-page.material")),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.next-page.head-value"),
                            MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.next-page.data"),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.next-page.name"),
                            nextPageLore,
                            MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.next-page.enchanted"),
                            group, String.valueOf(page + 1), null, null, null
                    )
            );
        }
        if (page > 0 && MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.previous-page.enabled")) {
            List<String> previousPageLore = new ArrayList<>();
            for (String s : MapSelector.getPlugin().getMainConfig().getList("map-selector.menus.maps-menu.items.previous-page.lore")) {
                s = s.replace("{previousPage}", String.valueOf(page));
                previousPageLore.add(s);
            }

            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.previous-page.slot"),
                    Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.previous-page.material")),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.previous-page.head-value"),
                            MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.previous-page.data"),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.previous-page.name"),
                            previousPageLore, MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.previous-page.enchanted"),
                            group, String.valueOf(page - 1), null, null, null
                    )
            );
        }

        if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.random-map.enabled")) {
            List<String> randomMapLore = new ArrayList<>();
            for (String s : MapSelector.getPlugin().getMainConfig().getList("map-selector.menus.maps-menu.items.random-map.lore")) {
                s = s.replace("{groupName}", Misc.firstLetterUpperCase(group));
                s = s.replace("{selectionsType}", Misc.getSelectionsType(player));
                s = s.replace("{remainingUses}", Misc.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map-selector.selections.unlimited-message")) ? MapSelector.getPlugin().getMainConfig().getString("map-selector.selections.unlimited-message") : String.valueOf(Integer.parseInt(Misc.getSelectionsType(player)) - MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId())));
                randomMapLore.add(s);
            }

            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.random-map.slot"),
                    Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.random-map.material")),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.random-map.head-value"),
                            MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.random-map.data"),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.random-map.name").replace("{groupName}", Misc.firstLetterUpperCase(group)),
                            randomMapLore,
                            MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.random-map.enchanted"),
                            group, null, null, null, null
                    )
            );
        }
        if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.random-favourite.enabled")) {
            List<String> randomfavouriteLore = new ArrayList<>();
            for (String s : MapSelector.getPlugin().getMainConfig().getList("map-selector.menus.maps-menu.items.random-favourite.lore")) {
                s = s.replace("{groupName}", Misc.firstLetterUpperCase(group));
                s = s.replace("{selectionsType}", Misc.getSelectionsType(player));
                s = s.replace("{remainingUses}", Misc.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map-selector.selections.unlimited-message")) ? MapSelector.getPlugin().getMainConfig().getString("map-selector.selections.unlimited-message") : String.valueOf(Integer.parseInt(Misc.getSelectionsType(player)) - MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId())));
                randomfavouriteLore.add(s);
            }

            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.random-favourite.slot"),
                    Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.random-favourite.material")),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.random-favourite.head-value"),
                            MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.random-favourite.data"),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.random-favourite.name").replace("{groupName}", Misc.firstLetterUpperCase(group)),
                            randomfavouriteLore,
                            MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.random-favourite.enchanted"),
                            group, null, null, null, null
                    )
            );
        }
        if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.back.enabled")) {
            inventory.setItem(MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.back.slot"),
                    Misc.item(Material.valueOf(MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.back.material")),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.back.head-value"),
                            MapSelector.getPlugin().getMainConfig().getInt("map-selector.menus.maps-menu.items.back.data"),
                            MapSelector.getPlugin().getMainConfig().getString("map-selector.menus.maps-menu.items.back.name").replace("{groupName}", Misc.firstLetterUpperCase(group)),
                            MapSelector.getPlugin().getMainConfig().getList("map-selector.menus.maps-menu.items.back.lore"),
                            MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.menus.maps-menu.items.back.enchanted"),
                            group, null, null, null, null
                    )
            );
        }

        player.openInventory(inventory);
    }
}
