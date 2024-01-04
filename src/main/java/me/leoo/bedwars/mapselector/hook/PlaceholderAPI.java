

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

package me.leoo.bedwars.mapselector.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.database.Yaml;
import me.leoo.bedwars.mapselector.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return "itz_leoo";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mapselector";
    }

    @Override
    public @NotNull String getVersion() {
        return MapSelector.get().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.contains("map_joins_")) {
            return String.valueOf(Yaml.getMapJoins(Bukkit.getPlayer(player.getUniqueId()), params.replace("map_joins_", "")));
        }
        if (params.contains("is_map_favorite_")) {
            return String.valueOf(Yaml.isFavorite(Bukkit.getPlayer(player.getUniqueId()), params.replace("is_map_favorite_", "")));
        }
        if (params.contains("get_selections_type")) {
            return String.valueOf(Utils.getSelectionsType(Bukkit.getPlayer(player.getUniqueId())));
        }
        return null;
    }
}
