package me.leoo.bedwars.mapselector.commands;

import me.leoo.bedwars.mapselector.Main;
import me.leoo.bedwars.mapselector.configuration.Config;
import me.leoo.bedwars.mapselector.configuration.ConfigHandler;
import me.leoo.bedwars.mapselector.configuration.PlayerCache;
import me.leoo.bedwars.mapselector.database.Database;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadSelectorMenuCmd implements CommandExecutor {

	private static final ConfigHandler config = Config.config;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player player = (Player) sender;
		if(cmd.getName().equals("bedwarsselector")){
			if(player.hasPermission("bwselector.reload")){
				if(args.length == 1){
					if(args[0].equalsIgnoreCase("reload")) {
						config.reload();
						PlayerCache.config.reload();
						player.sendMessage(config.getString("map_selector.menu.reload.success"));
					}
				}else{
					player.sendMessage(config.getString("map_selector.menu.reload.missing"));
				}
			}

		}
		return false;
	}
}
