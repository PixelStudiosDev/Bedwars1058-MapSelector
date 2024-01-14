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

import com.andrei1058.bedwars.proxy.api.Messages;
import com.andrei1058.bedwars.proxy.language.LanguageManager;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.utils.Utils;
import me.leoo.utils.bukkit.config.ConfigManager;
import me.leoo.utils.bukkit.items.ItemBuilder;
import me.leoo.utils.bukkit.menu.MenuBuilder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SelectorMenuProxy extends MenuBuilder {

    private final ConfigManager config = MapSelector.get().getMainConfig();
    private final String group;

    public SelectorMenuProxy(String group) {
        super(MapSelector.get().getMainConfig().getInt("map-selector.menus.bedwars-menu.slots") / 9);

        this.group = group;
    }

    @Override
    public List<ItemBuilder> getItems(Player player) {
        List<ItemBuilder> items = new ArrayList<>();

        String displayGroup = group;
        if (!group.contains(",")) {
            displayGroup = LanguageManager.get().getPlayerLanguage(player).getMsg(Messages.ARENA_DISPLAY_GROUP_PATH + group.toLowerCase());
        }

        for (String key : config.getSection("map-selector.menus.bedwars-menu.items")) {
            items.add(ItemBuilder.parseFromConfig("map-selector.menus.bedwars-menu.items." + key, config)
                    .addReplacement("{groupName}", displayGroup)
                    .setEventCallback(event -> {
                        switch (key) {
                            case "join-random":
                                Utils.joinRandomGroup(player, group, false, false);
                                player.closeInventory();

                                break;
                            case "map-selector":
                                new ArenasMenuProxy(group).open(player);

                                break;
                            case "close":
                                player.closeInventory();

                                break;
                            case "rejoin":
                                player.performCommand("rejoin");

                                break;
                        }

                        return true;
                    })
            );
        }


        return items;
    }

    @Override
    public String getTitle(Player player) {
        return config.getString("map-selector.menus.bedwars-menu.title");
    }
}
