/*
 *
 */

package me.leoo.bedwars.mapselector.menu;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.configuration.ConfigManager;
import me.leoo.bedwars.mapselector.database.Yaml;
import me.leoo.bedwars.mapselector.utils.Misc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SelectorMenu {
    
    private static final MapSelector plugin = MapSelector.getPlugin();
    private static final ConfigManager config = plugin.getMainConfig();

    public static void openFirstGui(Player player, String group) {
        String path = "map-selector.menus.bedwars-menu.";
        Inventory inventory = Bukkit.createInventory(null, config.getInt(path + "slots"), config.getString(path + "title"));

        for (String extraItems : config.getYml().getConfigurationSection(path + "items").getKeys(false)) {
            if (config.getBoolean(path + "items." + extraItems + ".enabled")
                    && config.getBoolean(path + "items." + extraItems + ".extra")) {

                int slot = config.getInt(path + "items." + extraItems + ".slot");
                inventory.setItem(slot,
                        Misc.item(
                                config.getString(path + "items." + extraItems + ".material"),
                                config.getString(path + "items." + extraItems + ".head-value"),
                                config.getInt(path + "items." + extraItems + ".data"),
                                config.getString(path + "items." + extraItems + ".name"),
                                config.getList(path + "items." + extraItems + ".lore"),
                                config.getBoolean(path + "items." + extraItems + ".enchanted"),
                                null, null, null, null, null
                        )
                );
            }
        }

        String displayGroup = group;
        if(!group.contains(",")){
            displayGroup = Language.getPlayerLanguage(player).m(Messages.ARENA_DISPLAY_GROUP_PATH + group.toLowerCase());
        }

        List<String> joinRandomLore = new ArrayList<>();
        for (String s : config.getList(path + "items.join-random.lore")) {
            s = s.replace("{groupName}", displayGroup);
            joinRandomLore.add(s);
        }

        if (config.getBoolean(path + "items.join-random.enabled")) {
            inventory.setItem(config.getInt(path + "items.join-random.slot"),
                    Misc.item(config.getString(path + "items.join-random.material"),
                            config.getString(path + "items.join-random.head-value"),
                            config.getInt(path + "items.join-random.data"),
                            config.getString(path + "items.join-random.name").replace("{groupName}", displayGroup),
                            joinRandomLore,
                            config.getBoolean(path + "items.join-random.enchanted"),
                            group, displayGroup, null, null, null
                    )
            );
        }
        if (config.getBoolean(path + "items.map-selector.enabled")) {
            inventory.setItem(config.getInt(path + "items.map-selector.slot"),
                    Misc.item(config.getString(path + "items.map-selector.material"),
                            config.getString(path + "items.map-selector.head-value"),
                            config.getInt(path + "items.map-selector.data"),
                            config.getString(path + "items.map-selector.name").replace("{groupName}", displayGroup),
                            config.getList(path + "items.map-selector.lore"),
                            config.getBoolean(path + "items.map-selector.enchanted"),
                            group, displayGroup, null, null, null
                    )
            );
        }
        if (config.getBoolean(path + "items.close.enabled")) {
            inventory.setItem(config.getInt(path + "items.close.slot"),
                    Misc.item(config.getString(path + "items.close.material"),
                            config.getString(path + "items.close.head-value"),
                            config.getInt(path + "items.close.data"),
                            config.getString(path + "items.close.name"),
                            config.getList(path + "items.close.lore"),
                            config.getBoolean(path + "items.close.enchanted"),
                            group, null, null, null, null
                    )
            );
        }
        if (config.getBoolean(path + "items.rejoin.enabled")) {
            inventory.setItem(config.getInt(path + "items.rejoin.slot"),
                    Misc.item(config.getString(path + "items.rejoin.material"),
                            config.getString(path + "items.rejoin.head-value"),
                            config.getInt(path + "items.rejoin.data"),
                            config.getString(path + "items.rejoin.name"),
                            config.getList(path + "items.rejoin.lore"),
                            config.getBoolean(path + "items.rejoin.enchanted"),
                            group, null, null, null, null
                    )
            );
        }

        player.openInventory(inventory);
    }

    public static void openSecondGui(Player player, String group, int page) {
        Inventory inventory = Bukkit.createInventory(null, config.getInt("map-selector.menus.maps-menu.slots"), config.getString("map-selector.menus.maps-menu.title"));

        List<String> groups;
        groups = Arrays.asList(group.split(","));

        Misc.checkDate();

        List<IArena> arenas = new ArrayList<>();
        for (IArena arena : Arena.getArenas()) {
            if (groups.contains(arena.getGroup()) && arena.getStatus().equals(GameState.waiting) || arena.getStatus().equals(GameState.starting)) {
                arenas.add(arena);
            }
        }
        if (arenas.isEmpty()) {
            player.sendMessage(config.getString("map-selector.messages.no-maps"));
            return;
        }

        String displayGroup = group;
        if(!group.contains(",")){
            displayGroup = Language.getPlayerLanguage(player).m(Messages.ARENA_DISPLAY_GROUP_PATH + group.toLowerCase());
        }

        arenas.sort(Comparator.comparing(IArena::getDisplayName));

        for (String extraItems : config.getYml().getConfigurationSection("map-selector.menus.maps-menu.items").getKeys(false)) {
            if (config.getBoolean("map-selector.menus.maps-menu.items." + extraItems + ".enabled")
                    && config.getBoolean("map-selector.menus.maps-menu.items." + extraItems + ".extra")) {
                inventory.setItem(config.getInt("map-selector.menus.maps-menu.items." + extraItems + ".slot"),
                        Misc.item(config.getString("map-selector.menus.maps-menu.items." + extraItems + ".material"),
                                config.getString("map-selector.menus.maps-menu.items." + extraItems + ".head-value"),
                                config.getInt("map-selector.menus.maps-menu.items." + extraItems + ".data"),
                                config.getString("map-selector.menus.maps-menu.items." + extraItems + ".name"),
                                config.getList("map-selector.menus.maps-menu.items." + extraItems + ".lore"),
                                config.getBoolean("map-selector.menus.maps-menu.items." + extraItems + ".enchanted"),
                                null, null, null, null, null
                        )
                );
            }
        }

        String[] slots = config.getString("map-selector.menus.maps-menu.maps-slots").split(",");

        int mapsIndex = slots.length * page;

        for (String slot : slots) {
            if (mapsIndex >= arenas.size()) continue;

            List<String> mapLore = new ArrayList<>();
            for (String s : config.getList("map-selector.menus.maps-menu.items.map.lore")) {
                s = s.replace("{groupName}", arenas.get(mapsIndex).getDisplayGroup(player));
                s = s.replace("{availableGames}", "1");
                s = s.replace("{timesJoined}", String.valueOf(Yaml.getMapJoins(player, arenas.get(mapsIndex).getArenaName())));
                s = s.replace("{selectionsType}", Misc.getSelectionsType(player));
                s = s.replace("{remainingUses}", Misc.getSelectionsType(player).equals(config.getString("map-selector.selections.unlimited-message")) ? config.getString("map-selector.selections.unlimited-message") : String.valueOf(Integer.parseInt(Misc.getSelectionsType(player)) - plugin.getMapSelectorDatabase().getPlayerUses(player.getUniqueId())));
                s = s.replace("{status}", arenas.get(mapsIndex).getStatus().name());
                s = s.replace("{on}", String.valueOf(arenas.get(mapsIndex).getPlayers().size()));
                s = s.replace("{max}", String.valueOf(arenas.get(mapsIndex).getMaxPlayers()));
                mapLore.add(s);
            }

            List<String> mapFavoriteLore = new ArrayList<>();
            for (String s : config.getList("map-selector.menus.maps-menu.items.map-favorite.lore")) {
                s = s.replace("{groupName}", arenas.get(mapsIndex).getDisplayGroup(player));
                s = s.replace("{availableGames}", "1");
                s = s.replace("{timesJoined}", String.valueOf(Yaml.getMapJoins(player, arenas.get(mapsIndex).getArenaName())));
                s = s.replace("{selectionsType}", Misc.getSelectionsType(player));
                s = s.replace("{remainingUses}", Misc.getSelectionsType(player).equals(config.getString("map-selector.selections.unlimited-message")) ? config.getString("map-selector.selections.unlimited-message") : String.valueOf(Integer.parseInt(Misc.getSelectionsType(player)) - plugin.getMapSelectorDatabase().getPlayerUses(player.getUniqueId())));
                s = s.replace("{status}", arenas.get(mapsIndex).getStatus().name());
                s = s.replace("{on}", String.valueOf(arenas.get(mapsIndex).getPlayers().size()));
                s = s.replace("{max}", String.valueOf(arenas.get(mapsIndex).getMaxPlayers()));
                mapFavoriteLore.add(s);
            }

            List<String> mapNoUsesNoPermissionsLore = new ArrayList<>();
            for (String s : config.getList("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.lore")) {
                s = s.replace("{groupName}", arenas.get(mapsIndex).getDisplayGroup(player));
                s = s.replace("{availableGames}", "1");
                s = s.replace("{timesJoined}", String.valueOf(Yaml.getMapJoins(player, arenas.get(mapsIndex).getArenaName())));
                s = s.replace("{selectionsType}", Misc.getSelectionsType(player));
                s = s.replace("{remainingUses}", Misc.getSelectionsType(player).equals(config.getString("map-selector.selections.unlimited-message")) ? config.getString("map-selector.selections.unlimited-message") : String.valueOf(Integer.parseInt(Misc.getSelectionsType(player)) - plugin.getMapSelectorDatabase().getPlayerUses(player.getUniqueId())));
                s = s.replace("{status}", arenas.get(mapsIndex).getStatus().name());
                s = s.replace("{on}", String.valueOf(arenas.get(mapsIndex).getPlayers().size()));
                s = s.replace("{max}", String.valueOf(arenas.get(mapsIndex).getMaxPlayers()));
                mapNoUsesNoPermissionsLore.add(s);
            }

            if (Misc.getSelectionsType(player).equals(config.getString("map-selector.selections.unlimited-message"))) {
                if (Yaml.isFavorite(player, String.valueOf(arenas.get(mapsIndex).getArenaName()))) {
                    if (config.getBoolean("map-selector.menus.maps-menu.items.map-favorite.enabled")) {
                        inventory.setItem(Integer.parseInt(slot),
                                Misc.item(config.getString("map-selector.menus.maps-menu.items.map-favorite.material"),
                                        config.getString("map-selector.menus.maps-menu.items.map-favorite.head-value"),
                                        config.getInt("map-selector.menus.maps-menu.items.map-favorite.data"),
                                        config.getString("map-selector.menus.maps-menu.items.map-favorite.name").replace("{mapName}", arenas.get(mapsIndex).getDisplayName()),
                                        mapFavoriteLore,
                                        config.getBoolean("map-selector.menus.maps-menu.items.map-favorite.enchanted"),
                                        arenas.get(mapsIndex).getArenaName(), arenas.get(mapsIndex).getGroup(), arenas.get(mapsIndex).getDisplayName(), group, String.valueOf(page)
                                )
                        );
                    }
                } else {
                    if (config.getBoolean("map-selector.menus.maps-menu.items.map.enabled")) {
                        inventory.setItem(Integer.parseInt(slot),
                                Misc.item(config.getString("map-selector.menus.maps-menu.items.map.material"),
                                        config.getString("map-selector.menus.maps-menu.items.map.head-value"),
                                        config.getInt("map-selector.menus.maps-menu.items.map.data"),
                                        config.getString("map-selector.menus.maps-menu.items.map.name").replace("{mapName}", arenas.get(mapsIndex).getDisplayName()),
                                        mapLore,
                                        config.getBoolean("map-selector.menus.maps-menu.items.map.enchanted"),
                                        arenas.get(mapsIndex).getArenaName(), arenas.get(mapsIndex).getGroup(), arenas.get(mapsIndex).getDisplayName(), group, String.valueOf(page)
                                )
                        );
                    }
                }
            } else {
                if (player.hasPermission(config.getString("map-selector.selections.permission"))
                        && plugin.getMapSelectorDatabase().getPlayerUses(player.getUniqueId()) < Integer.parseInt(Misc.getSelectionsType(player))) {
                    if (Yaml.isFavorite(player, String.valueOf(arenas.get(mapsIndex).getArenaName()))) {
                        if (config.getBoolean("map-selector.menus.maps-menu.items.map-favorite.enabled")) {
                            inventory.setItem(Integer.parseInt(slot),
                                    Misc.item(config.getString("map-selector.menus.maps-menu.items.map-favorite.material"),
                                            config.getString("map-selector.menus.maps-menu.items.map-favorite.head-value"),
                                            config.getInt("map-selector.menus.maps-menu.items.map-favorite.data"),
                                            config.getString("map-selector.menus.maps-menu.items.map-favorite.name").replace("{mapName}", arenas.get(mapsIndex).getDisplayName()),
                                            mapFavoriteLore,
                                            config.getBoolean("map-selector.menus.maps-menu.items.map-favorite.enchanted"),
                                            arenas.get(mapsIndex).getArenaName(), arenas.get(mapsIndex).getGroup(), arenas.get(mapsIndex).getDisplayName(), group, String.valueOf(page)
                                    )
                            );
                        }
                    } else {
                        if (config.getBoolean("map-selector.menus.maps-menu.items.map.enabled")) {
                            inventory.setItem(Integer.parseInt(slot),
                                    Misc.item(config.getString("map-selector.menus.maps-menu.items.map.material"),
                                            config.getString("map-selector.menus.maps-menu.items.map.head-value"),
                                            config.getInt("map-selector.menus.maps-menu.items.map.data"),
                                            config.getString("map-selector.menus.maps-menu.items.map.name").replace("{mapName}", arenas.get(mapsIndex).getDisplayName()),
                                            mapLore,
                                            config.getBoolean("map-selector.menus.maps-menu.items.map.enchanted"),
                                            arenas.get(mapsIndex).getArenaName(), arenas.get(mapsIndex).getGroup(), arenas.get(mapsIndex).getDisplayName(), group, String.valueOf(page)
                                    )
                            );
                        }
                    }
                } else {
                    if (config.getBoolean("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.enabled")) {
                        inventory.setItem(Integer.parseInt(slot),
                                Misc.item(config.getString("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.material"),
                                        config.getString("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.head-value"),
                                        config.getInt("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.data"),
                                        config.getString("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.name").replace("{mapName}", arenas.get(mapsIndex).getDisplayName()),
                                        mapNoUsesNoPermissionsLore,
                                        config.getBoolean("map-selector.menus.maps-menu.items.map-no-permissions-no-uses.enchanted"),
                                        arenas.get(mapsIndex).getArenaName(), arenas.get(mapsIndex).getGroup(), arenas.get(mapsIndex).getDisplayName(), null, null
                                )
                        );
                    }
                }
            }

            mapsIndex++;
        }

        if (arenas.size() - (slots.length * page) > slots.length &&
                config.getBoolean("map-selector.menus.maps-menu.items.next-page.enabled")) {
            List<String> nextPageLore = new ArrayList<>();
            for (String s : config.getList("map-selector.menus.maps-menu.items.next-page.lore")) {
                s = s.replace("{nextPage}", String.valueOf(page + 2));
                nextPageLore.add(s);
            }

            inventory.setItem(config.getInt("map-selector.menus.maps-menu.items.next-page.slot"),
                    Misc.item(config.getString("map-selector.menus.maps-menu.items.next-page.material"),
                            config.getString("map-selector.menus.maps-menu.items.next-page.head-value"),
                            config.getInt("map-selector.menus.maps-menu.items.next-page.data"),
                            config.getString("map-selector.menus.maps-menu.items.next-page.name"),
                            nextPageLore,
                            config.getBoolean("map-selector.menus.maps-menu.items.next-page.enchanted"),
                            group, String.valueOf(page + 1), null, null, null
                    )
            );
        }
        if (page > 0 && config.getBoolean("map-selector.menus.maps-menu.items.previous-page.enabled")) {
            List<String> previousPageLore = new ArrayList<>();
            for (String s : config.getList("map-selector.menus.maps-menu.items.previous-page.lore")) {
                s = s.replace("{previousPage}", String.valueOf(page));
                previousPageLore.add(s);
            }

            inventory.setItem(config.getInt("map-selector.menus.maps-menu.items.previous-page.slot"),
                    Misc.item(config.getString("map-selector.menus.maps-menu.items.previous-page.material"),
                            config.getString("map-selector.menus.maps-menu.items.previous-page.head-value"),
                            config.getInt("map-selector.menus.maps-menu.items.previous-page.data"),
                            config.getString("map-selector.menus.maps-menu.items.previous-page.name"),
                            previousPageLore, config.getBoolean("map-selector.menus.maps-menu.items.previous-page.enchanted"),
                            group, String.valueOf(page - 1), null, null, null
                    )
            );
        }

        if (config.getBoolean("map-selector.menus.maps-menu.items.random-map.enabled")) {
            List<String> randomMapLore = new ArrayList<>();
            for (String s : config.getList("map-selector.menus.maps-menu.items.random-map.lore")) {
                s = s.replace("{groupName}", displayGroup);
                s = s.replace("{selectionsType}", Misc.getSelectionsType(player));
                s = s.replace("{remainingUses}", Misc.getSelectionsType(player).equals(config.getString("map-selector.selections.unlimited-message")) ? config.getString("map-selector.selections.unlimited-message") : String.valueOf(Integer.parseInt(Misc.getSelectionsType(player)) - plugin.getMapSelectorDatabase().getPlayerUses(player.getUniqueId())));
                randomMapLore.add(s);
            }

            inventory.setItem(config.getInt("map-selector.menus.maps-menu.items.random-map.slot"),
                    Misc.item(config.getString("map-selector.menus.maps-menu.items.random-map.material"),
                            config.getString("map-selector.menus.maps-menu.items.random-map.head-value"),
                            config.getInt("map-selector.menus.maps-menu.items.random-map.data"),
                            config.getString("map-selector.menus.maps-menu.items.random-map.name").replace("{groupName}", displayGroup),
                            randomMapLore,
                            config.getBoolean("map-selector.menus.maps-menu.items.random-map.enchanted"),
                            group, displayGroup, null, null, null
                    )
            );
        }
        if (config.getBoolean("map-selector.menus.maps-menu.items.random-favourite.enabled")) {
            List<String> randomfavouriteLore = new ArrayList<>();
            for (String s : config.getList("map-selector.menus.maps-menu.items.random-favourite.lore")) {
                s = s.replace("{groupName}", displayGroup);
                s = s.replace("{selectionsType}", Misc.getSelectionsType(player));
                s = s.replace("{remainingUses}", Misc.getSelectionsType(player).equals(config.getString("map-selector.selections.unlimited-message")) ? config.getString("map-selector.selections.unlimited-message") : String.valueOf(Integer.parseInt(Misc.getSelectionsType(player)) - plugin.getMapSelectorDatabase().getPlayerUses(player.getUniqueId())));
                randomfavouriteLore.add(s);
            }

            inventory.setItem(config.getInt("map-selector.menus.maps-menu.items.random-favourite.slot"),
                    Misc.item(config.getString("map-selector.menus.maps-menu.items.random-favourite.material"),
                            config.getString("map-selector.menus.maps-menu.items.random-favourite.head-value"),
                            config.getInt("map-selector.menus.maps-menu.items.random-favourite.data"),
                            config.getString("map-selector.menus.maps-menu.items.random-favourite.name").replace("{groupName}", displayGroup),
                            randomfavouriteLore,
                            config.getBoolean("map-selector.menus.maps-menu.items.random-favourite.enchanted"),
                            group, displayGroup, null, null, null
                    )
            );
        }
        if (config.getBoolean("map-selector.menus.maps-menu.items.back.enabled")) {
            inventory.setItem(config.getInt("map-selector.menus.maps-menu.items.back.slot"),
                    Misc.item(config.getString("map-selector.menus.maps-menu.items.back.material"),
                            config.getString("map-selector.menus.maps-menu.items.back.head-value"),
                            config.getInt("map-selector.menus.maps-menu.items.back.data"),
                            config.getString("map-selector.menus.maps-menu.items.back.name").replace("{groupName}", displayGroup),
                            config.getList("map-selector.menus.maps-menu.items.back.lore"),
                            config.getBoolean("map-selector.menus.maps-menu.items.back.enchanted"),
                            group, null, null, null, null
                    )
            );
        }

        player.openInventory(inventory);
    }
}
