/*
 * Bedwars1058-MapSelector
 * Copyright (C) 2023  itz_leoo Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package me.leoo.bedwars.mapselector.menu.bedwars;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.database.Yaml;
import me.leoo.bedwars.mapselector.utils.Utils;
import me.leoo.utils.bukkit.config.ConfigManager;
import me.leoo.utils.bukkit.items.ItemBuilder;
import me.leoo.utils.bukkit.menu.pagination.PaginatedMenuBuilder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ArenasMenu extends PaginatedMenuBuilder {

    private final ConfigManager config = MapSelector.get().getMainConfig();
    private final String group;

    public ArenasMenu(String group) {
        super(MapSelector.get().getMainConfig().getInt("map-selector.menus.maps-menu.slots") / 9);

        this.group = group;
    }

    @Override
    public List<Integer> getPaginatedSlots() {
        return config.getIntegerSplitList("map-selector.menus.maps-menu.maps-slots");
    }

    @Override
    public String getPaginationTitle(Player player) {
        return config.getString("map-selector.menus.maps-menu.title");
    }

    @Override
    public List<ItemBuilder> getAllPageItems(Player player) {
        List<ItemBuilder> items = new ArrayList<>();

        //check
        List<String> groups;
        groups = Arrays.asList(group.split(","));

        Utils.checkDate();

        //arenas
        List<IArena> arenas = new ArrayList<>();
        for (IArena arena : Arena.getArenas()) {
            if (groups.contains(arena.getGroup()) && arena.getStatus().equals(GameState.waiting) || arena.getStatus().equals(GameState.starting)) {
                arenas.add(arena);
            }
        }
        if (arenas.isEmpty()) {
            player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.no-maps"));
            return items;
        }

        String displayGroup = group;
        if (!group.contains(",")) {
            displayGroup = Language.getPlayerLanguage(player).m(Messages.ARENA_DISPLAY_GROUP_PATH + group.toLowerCase());
        }

        arenas.sort(Comparator.comparing(IArena::getDisplayName));

        boolean unlimited = Utils.getSelectionsType(player).equals(MapSelector.get().getMainConfig().getString("map-selector.selections.unlimited-message"));

        //items
        for (IArena arena : arenas) {
            String path;

            if (unlimited) {
                if (Yaml.isFavorite(player, arena.getArenaName())) {
                    path = "map-favorite";
                } else {
                    path = "map";
                }
            } else {
                if (player.hasPermission(MapSelector.get().getMainConfig().getString("map-selector.selections.permission"))
                        && MapSelector.get().getDatabaseManager().getPlayerUses(player.getUniqueId()) < Integer.parseInt(Utils.getSelectionsType(player))) {
                    if (Yaml.isFavorite(player, arena.getArenaName())) {
                        path = "map-favorite";
                    } else {
                        path = "map";
                    }
                } else {
                    path = "map-no-permissions-no-uses";
                }
            }

            items.add(ItemBuilder.parseFromConfig("map-selector.menus.maps-menu.items." + path, config)
                    .addReplacement("{mapName}", arena.getDisplayName())
                    .addReplacement("{groupName}", displayGroup)
                    .addReplacement("{availableGames}", "1")
                    .addReplacement("{timesJoined}", String.valueOf(Yaml.getMapJoins(player, arena.getArenaName())))
                    .addReplacement("{selectionsType}", Utils.getSelectionsType(player))
                    .addReplacement("{remainingUses}", unlimited ? MapSelector.get().getMainConfig().getString("map-selector.selections.unlimited-message") : String.valueOf(Integer.parseInt(Utils.getSelectionsType(player)) - MapSelector.get().getDatabaseManager().getPlayerUses(player.getUniqueId())))
                    .addReplacement("{status}", arena.getStatus().name())
                    .addReplacement("{on}", String.valueOf(arena.getPlayers().size()))
                    .addReplacement("{max}", String.valueOf(arena.getMaxPlayers()))
                    .setEventCallback(event -> {
                        switch (path) {
                            case "map":
                            case "map-favorite":
                                if (event.isRightClick()) {
                                    if (path.contains("favorite")) {
                                        Yaml.unsetFavorite(player, arena.getArenaName());
                                    } else {
                                        Yaml.setFavorite(player, arena.getArenaName());
                                    }

                                    update(player);
                                } else {
                                    if (Utils.getSelectionsType(player).equals(MapSelector.get().getMainConfig().getString("map-selector.selections.unlimited-message"))) {
                                        Utils.joinArena(player, arena.getArenaName(), arena.getGroup(), true);

                                        player.closeInventory();
                                    } else {
                                        if (MapSelector.get().getDatabaseManager().getPlayerUses(player.getUniqueId()) < Integer.parseInt(Utils.getSelectionsType(player))) {
                                            Utils.joinArena(player, arena.getArenaName(), arena.getGroup(), false);

                                            player.closeInventory();
                                        } else {
                                            player.closeInventory();
                                            player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.limit-reached"));
                                        }
                                    }
                                }

                                break;
                            case "map-no-permissions-no-uses":
                                player.closeInventory();
                                player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.limit-reached"));

                                break;
                        }
                    })
            );
        }

        return items;
    }

    @Override
    public List<ItemBuilder> getGlobalItems(Player player) {
        List<ItemBuilder> items = new ArrayList<>();

        String displayGroup = group;
        if (!group.contains(",")) {
            displayGroup = Language.getPlayerLanguage(player).m(Messages.ARENA_DISPLAY_GROUP_PATH + group.toLowerCase());
        }

        //other
        boolean unlimited = Utils.getSelectionsType(player).equals(MapSelector.get().getMainConfig().getString("map-selector.selections.unlimited-message"));

        for (String key : config.getSection("map-selector.menus.maps-menu.items")) {
            if (key.startsWith("map") || key.contains("page")) continue;

            items.add(ItemBuilder.parseFromConfig("map-selector.menus.maps-menu.items." + key, config)
                    .addReplacement("{groupName}", displayGroup)
                    .addReplacement("{selectionsType}", Utils.getSelectionsType(player))
                    .addReplacement("{remainingUses}", unlimited ? MapSelector.get().getMainConfig().getString("map-selector.selections.unlimited-message") : String.valueOf(Integer.parseInt(Utils.getSelectionsType(player)) - MapSelector.get().getDatabaseManager().getPlayerUses(player.getUniqueId())))
                    .setEventCallback(event -> {
                        switch (key) {
                            case "random-map":
                                Utils.joinRandomGroup(player, group, false, false);

                                break;
                            case "random-favourite":
                                if (Utils.getSelectionsType(player).equals(MapSelector.get().getMainConfig().getString("map-selector.selections.unlimited-message"))) {
                                    Utils.joinRandomGroup(player, group, true, false);
                                } else {
                                    if (MapSelector.get().getDatabaseManager().getPlayerUses(player.getUniqueId()) < Integer.parseInt(Utils.getSelectionsType(player))) {
                                        Utils.joinRandomGroup(player, group, false, false);
                                    } else {
                                        player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.limit-reached"));
                                    }
                                }

                                player.closeInventory();

                                break;
                            case "back":
                                new SelectorMenu(group).open(player);
                                break;
                        }

                        return true;
                    })
            );
        }


        return items;
    }

    @Override
    public ItemBuilder getNextPageItem(Player player) {
        return ItemBuilder.parseFromConfig("map-selector.menus.maps-menu.items.next-page", config)
                .addReplacement("{nextPage}", String.valueOf(getPage() + 1));
    }

    @Override
    public ItemBuilder getPreviousPageItem(Player player) {
        return ItemBuilder.parseFromConfig("map-selector.menus.maps-menu.items.previous-page", config)
                .addReplacement("{previousPage}", String.valueOf(getPage() - 1));
    }
}
