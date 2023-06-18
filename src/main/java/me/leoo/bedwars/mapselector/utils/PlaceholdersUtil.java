/*
 *
 */

package me.leoo.bedwars.mapselector.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.database.Yaml;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholdersUtil extends PlaceholderExpansion {

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
		return MapSelector.getPlugin().getDescription().getVersion();
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public String onRequest(OfflinePlayer player, String params) {
		if(params.contains("map_joins_")) {
			return String.valueOf(Yaml.getMapJoins(Bukkit.getPlayer(player.getUniqueId()), params.replace("map_joins_", "")));
		}
		if(params.contains("is_map_favorite_")) {
			return String.valueOf(Yaml.isFavorite(Bukkit.getPlayer(player.getUniqueId()), params.replace("is_map_favorite_", "")));
		}
		if(params.contains("get_selections_type")) {
			return String.valueOf(Misc.getSelectionsType(Bukkit.getPlayer(player.getUniqueId())));
		}
		return null;
	}
}
