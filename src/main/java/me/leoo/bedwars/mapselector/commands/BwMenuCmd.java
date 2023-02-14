package me.leoo.bedwars.mapselector.commands;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import me.leoo.bedwars.mapselector.Main;
import me.leoo.bedwars.mapselector.configuration.Config;
import me.leoo.bedwars.mapselector.configuration.ConfigHandler;
import me.leoo.bedwars.mapselector.database.Database;
import me.leoo.bedwars.mapselector.menu.MapSelectorMenu;
import me.leoo.bedwars.mapselector.menu.MapSelectorMenuProxy;
import me.leoo.bedwars.mapselector.utils.BedwarsMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BwMenuCmd implements CommandExecutor, TabCompleter {

	private static final ConfigHandler config = Config.config;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player player = (Player) sender;
		if(cmd.getName().equals("bedwarsmenu")){
			if(args.length == 1){
				String group = args[0];
				if(Main.getMode().equals(BedwarsMode.BEDWARSPROXY)){
					List<String> groups = new ArrayList<>();
					for(CachedArena arena : ArenaManager.getArenas()){
						if(!groups.contains(arena.getArenaGroup())){
							groups.add(arena.getArenaGroup());
						}
					}
					if(group.contains(",")){
						String[] groups2 = group.split(",");
						for(String s : groups2){
							if(!groups.contains(s)){
								player.sendMessage(config.getString("map_selector.menu.open.group_doesnt_exist"));
							}else{
								MapSelectorMenuProxy.OpenGroupMenu(player, group);
							}
						}
					}else{
						if(groups.contains(group)){
							MapSelectorMenuProxy.OpenGroupMenu(player, group);
						}else{
							player.sendMessage(config.getString("map_selector.menu.open.group_doesnt_exist"));
						}
					}
				}else{
					if(group.contains(",")){
						String[] groups2 = group.split(",");
						for(String s : groups2){
							if(BedWars.config.getList("arenaGroups").contains(s)){
								MapSelectorMenu.OpenGroupMenu(player, group);
							}else{
								player.sendMessage(config.getString("map_selector.menu.open.group_doesnt_exist"));
							}
						}
					}else{
						if(BedWars.config.getList("arenaGroups").contains(group)){
							MapSelectorMenu.OpenGroupMenu(player, group);
						}else{
							player.sendMessage(config.getString("map_selector.menu.open.group_doesnt_exist"));
						}
					}
				}
			}else{
				player.sendMessage(config.getString("map_selector.menu.open.missing2"));
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if(cmd.getName().equals("bedwarsmenu")){
			if(args.length == 1){
				if(Main.getMode().equals(BedwarsMode.BEDWARSPROXY)){
					List<String> groups = new ArrayList<>();
					for(CachedArena arena : ArenaManager.getArenas()){
						if(!groups.contains(arena.getArenaGroup())){
							groups.add(arena.getArenaGroup());
						}
					}
					return groups;
				}else{
					return new ArrayList<>(BedWars.config.getList("arenaGroups"));
				}
			}
		}
		return null;
	}
}
