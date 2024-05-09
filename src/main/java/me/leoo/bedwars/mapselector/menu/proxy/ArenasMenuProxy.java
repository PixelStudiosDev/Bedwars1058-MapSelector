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

package me.leoo.bedwars.mapselector.menu.proxy;

import com.andrei1058.bedwars.proxy.api.ArenaStatus;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.api.Language;
import com.andrei1058.bedwars.proxy.api.Messages;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import com.andrei1058.bedwars.proxy.language.LanguageManager;
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

public class ArenasMenuProxy extends PaginatedMenuBuilder {

    private final ConfigManager CONFIG = MapSelector.get().getMainConfig();
    private final String group;

    public ArenasMenuProxy(Player player, String group) {
        super(player, MapSelector.get().getMainConfig().getInt("map-selector.menus.maps-menu.slots") / 9);

        this.group = group;
    }

    @Override
    public List<Integer> getPaginatedSlots() {
        return CONFIG.getIntegerSplit("map-selector.menus.maps-menu.maps-slots");
    }

    @Override
    public String getPaginationTitle() {
        return CONFIG.getString("map-selector.menus.maps-menu.title");
    }

    @Override
    public List<ItemBuilder> getAllPageItems() {
        List<ItemBuilder> items = new ArrayList<>();

        //check
        List<String> groups;
        groups = Arrays.asList(group.split(","));

        Utils.checkDate();

        //arenas
        List<CachedArena> arenas = new ArrayList<>();
        for (CachedArena arena : ArenaManager.getArenas()) {
            if (groups.contains(arena.getArenaGroup()) && arena.getStatus().equals(ArenaStatus.WAITING) || arena.getStatus().equals(ArenaStatus.STARTING)) {
                arenas.add(arena);
            }
        }
        if (arenas.isEmpty()) {
            player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.no-maps"));
            return items;
        }

        Language language = LanguageManager.get().getPlayerLanguage(player);

        String displayGroup = group;
        if (!group.contains(",")) {
            displayGroup = language.getMsg(Messages.ARENA_DISPLAY_GROUP_PATH + group.toLowerCase());
        }

        arenas.sort(Comparator.comparing(arena -> arena.getDisplayName(language)));

        boolean unlimited = Utils.getSelectionsType(player).equals(MapSelector.get().getMainConfig().getString("map-selector.selections.unlimited-message"));

        //items
        for (CachedArena arena : arenas) {
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

            items.add(ItemBuilder.parse("map-selector.menus.maps-menu.items." + path, CONFIG)
                    .addReplacement("{mapName}", arena.getDisplayName(language))
                    .addReplacement("{groupName}", displayGroup)
                    .addReplacement("{availableGames}", "1")
                    .addReplacement("{timesJoined}", String.valueOf(Yaml.getMapJoins(player, arena.getArenaName())))
                    .addReplacement("{selectionsType}", Utils.getSelectionsType(player))
                    .addReplacement("{remainingUses}", unlimited ? MapSelector.get().getMainConfig().getString("map-selector.selections.unlimited-message") : String.valueOf(Integer.parseInt(Utils.getSelectionsType(player)) - MapSelector.get().getDatabaseManager().getPlayerUses(player.getUniqueId())))
                    .addReplacement("{status}", arena.getStatus().name())
                    .addReplacement("{on}", String.valueOf(arena.getCurrentPlayers()))
                    .addReplacement("{max}", String.valueOf(arena.getMaxPlayers()))
                    .event(event -> {
                        switch (path) {
                            case "map":
                            case "map-favorite":
                                if (event.isRightClick()) {
                                    if (path.contains("favorite")) {
                                        Yaml.unsetFavorite(player, arena.getArenaName());
                                    } else {
                                        Yaml.setFavorite(player, arena.getArenaName());
                                    }

                                    update();
                                } else {
                                    if (Utils.getSelectionsType(player).equals(MapSelector.get().getMainConfig().getString("map-selector.selections.unlimited-message"))) {
                                        Utils.joinArena(player, arena.getArenaName(), arena.getArenaGroup(), true);

                                        player.closeInventory();
                                    } else {
                                        if (MapSelector.get().getDatabaseManager().getPlayerUses(player.getUniqueId()) < Integer.parseInt(Utils.getSelectionsType(player))) {
                                            Utils.joinArena(player, arena.getArenaName(), arena.getArenaGroup(), false);

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
    public List<ItemBuilder> getGlobalItems() {
        List<ItemBuilder> items = new ArrayList<>();

        String displayGroup = group;
        if (!group.contains(",")) {
            displayGroup = LanguageManager.get().getPlayerLanguage(player).getMsg(Messages.ARENA_DISPLAY_GROUP_PATH + group.toLowerCase());
        }

        //other
        boolean unlimited = Utils.getSelectionsType(player).equals(MapSelector.get().getMainConfig().getString("map-selector.selections.unlimited-message"));


        for (String key : CONFIG.getKeys("map-selector.menus.maps-menu.items")) {
            if (key.startsWith("map") || key.contains("page")) continue;

            items.add(ItemBuilder.parse("map-selector.menus.maps-menu.items." + key, CONFIG)
                    .addReplacement("{groupName}", displayGroup)
                    .addReplacement("{selectionsType}", Utils.getSelectionsType(player))
                    .addReplacement("{remainingUses}", unlimited ? MapSelector.get().getMainConfig().getString("map-selector.selections.unlimited-message") : String.valueOf(Integer.parseInt(Utils.getSelectionsType(player)) - MapSelector.get().getDatabaseManager().getPlayerUses(player.getUniqueId())))
                    .event(event -> {
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
                                new SelectorMenuProxy(player, group).open();
                                break;
                        }

                        return true;
                    })
            );
        }


        return items;
    }

    @Override
    public ItemBuilder getNextPageItem() {
        return ItemBuilder.parse("map-selector.menus.maps-menu.items.next-page", CONFIG)
                .addReplacement("{nextPage}", String.valueOf(getPage() + 1));
    }

    @Override
    public ItemBuilder getPreviousPageItem() {
        return ItemBuilder.parse("map-selector.menus.maps-menu.items.previous-page", CONFIG)
                .addReplacement("{previousPage}", String.valueOf(getPage() - 1));
    }
}
