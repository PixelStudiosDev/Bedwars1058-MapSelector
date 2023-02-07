package me.leoo.bedwars.mapselector.commands;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import me.leoo.bedwars.mapselector.Main;
import me.leoo.bedwars.mapselector.configuration.Config;
import me.leoo.bedwars.mapselector.menu.MapSelectorMenu;
import me.leoo.bedwars.mapselector.menu.MapSelectorMenuBungee;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BwMenuCmd implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		if(cmd.getName().equals("bwmenu")){
			if(args.length == 1){
				String group;
				group = args[0];
				if(Main.bungee){
					List<String> groups = new ArrayList<>();
					for(CachedArena a : ArenaManager.getArenas()){
						if(!groups.contains(a.getArenaGroup())){
							groups.add(a.getArenaGroup());
						}
					}
					if(group.contains(",")){
						String[] groups2 = group.split(",");
						for(String s : groups2){
							if(!groups.contains(s)){
								p.sendMessage(Config.config.getString("map_selector.menu.open.group_doesnt_exist"));
							}else{
								MapSelectorMenuBungee.OpenGroupMenu(p, group);
							}
						}
					}else{
						if(groups.contains(group)){
							MapSelectorMenuBungee.OpenGroupMenu(p, group);
						}else{
							p.sendMessage(Config.config.getString("map_selector.menu.open.group_doesnt_exist"));
						}
					}
				}else{
					if(group.contains(",")){
						String[] groups2 = group.split(",");
						for(String s : groups2){
							if(BedWars.config.getList("arenaGroups").contains(s)){
								MapSelectorMenu.OpenGroupMenu(p, group);
							}else{
								p.sendMessage(Config.config.getString("map_selector.menu.open.group_doesnt_exist"));
							}
						}
					}else{
						if(BedWars.config.getList("arenaGroups").contains(group)){
							MapSelectorMenu.OpenGroupMenu(p, group);
						}else{
							p.sendMessage(Config.config.getString("map_selector.menu.open.group_doesnt_exist"));
						}
					}
				}
			}else{
				p.sendMessage(Config.config.getString("map_selector.menu.open.missing2"));
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if(cmd.getName().equals("bwmenu")){
			if(args.length == 1){
				if(Main.bungee){
					List<String> groups = new ArrayList<>();
					for(CachedArena a : ArenaManager.getArenas()){
						if(!groups.contains(a.getArenaGroup())){
							groups.add(a.getArenaGroup());
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
