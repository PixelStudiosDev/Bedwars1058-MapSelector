package me.leoo.bedwars.mapselector.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.arena.Arena;
import me.leoo.bedwars.mapselector.Main;
import me.leoo.bedwars.mapselector.configuration.Config;
import me.leoo.bedwars.mapselector.menu.MapSelectorMenu;
import me.leoo.bedwars.mapselector.database.Yaml;
import me.leoo.bedwars.mapselector.utils.SelectorUtil;
import me.leoo.bedwars.mapselector.utils.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SelectorListeners implements Listener {

	@EventHandler
	public void InventoryClickEvent(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		ItemStack i = e.getCurrentItem();
		if (i == null)
			return;
		if (i.getType().equals(Material.AIR))
			return;
		if (i.getItemMeta() == null)
			return;
		if (i.getItemMeta().getDisplayName() == null)
			return;

		String n1 = BedWars.nms.getTag(i, "n1");
		String n2 = BedWars.nms.getTag(i, "n2");
		String n3 = BedWars.nms.getTag(i, "n3");
		String n4 = BedWars.nms.getTag(i, "n4");
		String n5 = BedWars.nms.getTag(i, "n5");

		//first gui
		if (e.getView().getTitle().equals(Config.config.getString("map_selector.menu.per_group_menu.title"))) {
			if (i.getItemMeta().getDisplayName().equals(Config.config.getString("map_selector.menu.item.join_random.name").replace("{group_name}", StringUtil.firstLetterUpperCase(n1)))) {
				if(Config.config.getString("map_selector.menu.item.join_random.command").equals("default_action")){
					List<String> groups;
					if(n1.contains(",")){
						groups = Arrays.asList(n1.split(","));
					}else{
						groups = Collections.singletonList(n1);
					}
					Random r = new Random();
					String re = groups.get(r.nextInt(groups.size()));
					SelectorUtil.joinRandomGroup(p, re, false, false);
					p.closeInventory();
				}else{
					p.performCommand(Config.config.getString("map_selector.menu.item.join_random.command"));
				}
			} else if (i.getItemMeta().getDisplayName().equals(Config.config.getString("map_selector.menu.item.map_selector.name").replace("{group_name}", StringUtil.firstLetterUpperCase(n1)))) {
				if(Config.config.getString("map_selector.menu.item.map_selector.command").equals("default_action")){
					MapSelectorMenu.OpenSelectorMenu(p, n1, 0, 0);
				}else{
					p.performCommand(Config.config.getString("map_selector.menu.item.map_selector.command"));
				}
			} else if (i.getItemMeta().getDisplayName().equals(Config.config.getString("map_selector.menu.item.close.name"))) {
				if(Config.config.getString("map_selector.menu.item.close.command").equals("default_action")){
					p.closeInventory();
				}else{
					p.performCommand(Config.config.getString("map_selector.menu.item.close.command"));
				}
			} else if (i.getItemMeta().getDisplayName().equals(Config.config.getString("map_selector.menu.item.rejoin.name"))) {
				if(Config.config.getString("map_selector.menu.item.rejoin.command").equals("default_action")){
					p.performCommand("bw rejoin");
				}else{
					p.performCommand(Config.config.getString("map_selector.menu.item.rejoin.command"));
				}
			}
			for(String extraItems : Config.config.getYml().getConfigurationSection("map_selector.menu.item").getKeys(false)){
				if(Config.config.getBoolean("map_selector.menu.item." + extraItems + ".extra")){
					if(Config.config.getInt("map_selector.menu.item." + extraItems + ".gui") == 1){
						if (i.getItemMeta().getDisplayName().equals(Config.config.getString("map_selector.menu.item." + extraItems + ".name"))) {
							if(Config.config.getString("map_selector.menu.item." + extraItems + ".command").equals("default_action")){
								if(p.isOp() || p.hasPermission("bwselector.reload")){
									p.sendMessage("§4§l[Map Selector] §7You clicked on an extra item in one of the selector menu. §7You must edit the command or disable this item from the plugin's config §c(plugins/BedWarsProxy/Addons/MapSelector/config.yml)§7. §7To edit the item's command you have to modify the string §ccommand (it's under map_selector.menu.item." + extraItems + ")§7. §7To disable this item you have to set to §cfalse §7the boolean §cenabled (it's under map_selector.menu.item.\" + extraItems + \")§7.");
								}
							}else{
								p.performCommand(Config.config.getString("map_selector.menu.item." + extraItems + ".command"));
							}
						}
					}
				}
			}
			e.setCancelled(true);
		}
		//second gui
		if (e.getView().getTitle().equals(Config.config.getString("map_selector.menu.selector.title"))) {
			if (i.getItemMeta().getDisplayName().equals(Config.config.getString("map_selector.menu.item.map.name").replace("{map_name}", StringUtil.firstLetterUpperCase(n3)))) {
				if(Config.config.getString("map_selector.menu.item.map.command").equals("default_action")){
					if(e.isRightClick()){
						Yaml.setFavorite(p, n1);
						MapSelectorMenu.OpenSelectorMenu(p, n2, MapSelectorMenu.getStartMaps(p), MapSelectorMenu.getCurrentPage(p));
					}else{
						if(SelectorUtil.getSelectionsType(p).equals(Config.config.getString("map_selector.selection.unlimited_message"))){
							SelectorUtil.joinArena(p, n1, n2, true);
							p.closeInventory();
						}else if(!SelectorUtil.getSelectionsType(p).equals(Config.config.getString("map_selector.selection.unlimited_message"))){
							if(Main.database.getPlayerUses(p.getUniqueId()) < Integer.parseInt(SelectorUtil.getSelectionsType(p))){
								SelectorUtil.joinArena(p, n1, n2, false);
								p.closeInventory();
							}else{
								p.closeInventory();
								p.sendMessage(Config.config.getString("map_selector.menu.limit_reached"));
							}
						}
					}
				}else{
					p.performCommand(Config.config.getString("map_selector.menu.item.map.command"));
				}
			}else if (i.getItemMeta().getDisplayName().equals(Config.config.getString("map_selector.menu.item.map_favorite.name").replace("{map_name}", StringUtil.firstLetterUpperCase(n3)))) {
				if(Config.config.getString("map_selector.menu.item.map_favorite.command").equals("default_action")){
					if(e.isRightClick()){
						Yaml.unsetFavorite(p, n1);
						MapSelectorMenu.OpenSelectorMenu(p, n2, MapSelectorMenu.getStartMaps(p), MapSelectorMenu.getCurrentPage(p));
					}else{
						if(SelectorUtil.getSelectionsType(p).equals(Config.config.getString("map_selector.selection.unlimited_message"))){
							SelectorUtil.joinArena(p, n1, n2, true);
							p.closeInventory();
						}else if(!SelectorUtil.getSelectionsType(p).equals(Config.config.getString("map_selector.selection.unlimited_message"))){
							if(Main.database.getPlayerUses(p.getUniqueId()) < Integer.parseInt(SelectorUtil.getSelectionsType(p))){
								SelectorUtil.joinArena(p, n1, n2, false);
								p.closeInventory();
							}else{
								p.closeInventory();
								p.sendMessage(Config.config.getString("map_selector.menu.limit_reached"));
							}
						}
					}
				}else{
					p.performCommand(Config.config.getString("map_selector.menu.item.map_favorite.command"));
				}
			} else if (i.getItemMeta().getDisplayName().equalsIgnoreCase(Config.config.getString("map_selector.menu.item.map_no_permissions_no_uses.name").replace("{map_name}", n4))) {
				if(Config.config.getString("map_selector.menu.item.map_no_permissions_no_uses.command").equals("default_action")){
					p.closeInventory();
					p.sendMessage(Config.config.getString("map_selector.menu.limit_reached"));
				}else{
					p.performCommand(Config.config.getString("map_selector.menu.item.map_no_permissions_no_uses.command"));
				}
			} else if (i.getItemMeta().getDisplayName().equals(Config.config.getString("map_selector.menu.item.random_map.name").replace("{group_name}", n2))) {
				if(Config.config.getString("map_selector.menu.item.random_map.command").equals("default_action")){
					List<String> groups;
					if(n1.contains(",")){
						groups = Arrays.asList(n1.split(","));
					}else{
						groups = Collections.singletonList(n1);
					}
					Random r = new Random();
					String re = groups.get(r.nextInt(groups.size()));
					SelectorUtil.joinRandomGroup(p, re, false, false);
					p.closeInventory();
				}else{
					p.performCommand(Config.config.getString("map_selector.menu.item.random_map.command"));
				}
			}else if (i.getItemMeta().getDisplayName().equals(Config.config.getString("map_selector.menu.item.random_favourite.name").replace("{group_name}", n2))) {
				if(Config.config.getString("map_selector.menu.item.random_favourite.command").equals("default_action")){
					if(SelectorUtil.getSelectionsType(p).equals(Config.config.getString("map_selector.selection.unlimited_message"))){
						SelectorUtil.joinRandomGroup(p, n2, true, true);
					}else if(!SelectorUtil.getSelectionsType(p).equals(Config.config.getString("map_selector.selection.unlimited_message"))){
						if(Main.database.getPlayerUses(p.getUniqueId()) < Integer.parseInt(SelectorUtil.getSelectionsType(p))){
							List<String> groups;
							if(n2.contains(",")){
								groups = Arrays.asList(n2.split(","));
							}else{
								groups = Collections.singletonList(n2);
							}
							Random r = new Random();
							String re = groups.get(r.nextInt(groups.size()));
							SelectorUtil.joinRandomGroup(p, re, false, true);
							p.closeInventory();
						}else{
							p.closeInventory();
							p.sendMessage(Config.config.getString("map_selector.menu.limit_reached"));
						}
					}
				}else{
					p.performCommand(Config.config.getString("map_selector.menu.item.random_favourite.command"));
				}
			}else if (i.getItemMeta().getDisplayName().equals(Config.config.getString("map_selector.menu.item.back.name"))) {
				if(Config.config.getString("map_selector.menu.item.back.command").equals("default_action")){
					MapSelectorMenu.OpenGroupMenu(p, n2);
				}else{
					p.performCommand(Config.config.getString("map_selector.menu.item.back.command"));
				}
			}else if (i.getItemMeta().getDisplayName().equals(Config.config.getString("map_selector.menu.item.next_page.name"))) {
				if(Config.config.getString("map_selector.menu.item.next_page.command").equals("default_action")){
					MapSelectorMenu.OpenSelectorMenu(p, n5, MapSelectorMenu.getStartMaps(p) + Config.config.getString("map_selector.menu.slots").split(",").length, MapSelectorMenu.getCurrentPage(p) + 1);
				}else{
					p.performCommand(Config.config.getString("map_selector.menu.item.next_page.command"));
				}
			}else if (i.getItemMeta().getDisplayName().equals(Config.config.getString("map_selector.menu.item.previous_page.name"))) {
				if(Config.config.getString("map_selector.menu.item.previous_page.command").equals("default_action")){
					MapSelectorMenu.OpenSelectorMenu(p, n5, MapSelectorMenu.getStartMaps(p) - Config.config.getString("map_selector.menu.slots").split(",").length, MapSelectorMenu.getCurrentPage(p) - 1);
				}else{
					p.performCommand(Config.config.getString("map_selector.menu.item.previous_page.command"));
				}
			}
			for(String extraItems : Config.config.getYml().getConfigurationSection("map_selector.menu.item").getKeys(false)){
				if(Config.config.getBoolean("map_selector.menu.item." + extraItems + ".extra")){
					if(Config.config.getInt("map_selector.menu.item." + extraItems + ".gui") == 2){
						if (i.getItemMeta().getDisplayName().equals(Config.config.getString("map_selector.menu.item." + extraItems + ".name"))) {
							if(Config.config.getString("map_selector.menu.item." + extraItems + ".command").equals("default_action")){
								if(p.isOp() || p.hasPermission("bwselector.reload")){
									p.sendMessage("§4§l[Map Selector] §7You clicked on an extra item in one of the selector menu. §7You must edit the command or disable this item from the plugin's config §c(plugins/BedWarsProxy/Addons/MapSelector/config.yml)§7. §7To edit the item's command you have to modify the string §ccommand (it's under map_selector.menu.item." + extraItems + ")§7. §7To disable this item you have to set to §cfalse §7the boolean §cenabled (it's under map_selector.menu.item.\" + extraItems + \")§7.");
								}
							}else{
								p.performCommand(Config.config.getString("map_selector.menu.item." + extraItems + ".command"));
							}
						}
					}
				}
			}
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void InventoryDropEvent (PlayerDropItemEvent e){
		Player p = e.getPlayer();
		if (p.getOpenInventory().getTitle().equals(Config.config.getString("map_selector.menu.per_group_menu.title"))) {
			e.setCancelled(true);
		}
	}
}
