package me.leoo.bedwars.mapselector.listeners;

import me.leoo.bedwars.mapselector.Main;
import me.leoo.bedwars.mapselector.database.Database;
import me.leoo.bedwars.mapselector.database.Yaml;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

	private static final Database database = Main.getMapSelectorDatabase();

	@EventHandler
	public void PlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(!Yaml.isStoredPlayer(player)) {
			Yaml.storePlayer(player);
		}
		if(!database.isRegistered(player.getUniqueId())){
			database.registerPlayer(player.getUniqueId());
		}
	}
}
