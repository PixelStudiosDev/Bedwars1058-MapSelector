package me.leoo.bedwars.mapselector.listeners;

import me.leoo.bedwars.mapselector.Main;
import me.leoo.bedwars.mapselector.database.Yaml;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

	@EventHandler
	public void PlayerJoinEvent(PlayerJoinEvent e) {
		if(!Yaml.isStoredPlayer(e.getPlayer())) {
			Yaml.storePlayer(e.getPlayer());
		}
		if(!Main.database.isRegistered(e.getPlayer().getUniqueId())){
			Main.database.registerPlayer(e.getPlayer().getUniqueId());
		}
	}
}
