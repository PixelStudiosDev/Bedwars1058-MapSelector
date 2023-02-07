package me.leoo.bedwars.mapselector.commands;

import me.leoo.bedwars.mapselector.configuration.Config;
import me.leoo.bedwars.mapselector.configuration.PlayerCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadSelectorMenuCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		if(cmd.getName().equals("bwselector")){
			if(p.hasPermission("bwselector.reload")){
				if(args.length == 1){
					if(args[0].equalsIgnoreCase("reload")) {
						Config.config.reload();
						PlayerCache.cache.reload();
						p.sendMessage(Config.config.getString("map_selector.menu.reload.success"));
					}
				}else{
					p.sendMessage(Config.config.getString("map_selector.menu.reload.missing"));
				}
			}

		}
		return false;
	}
}
