package me.leoo.bedwars.mapselector.listeners;

import com.andrei1058.bedwars.BedWars;
import me.leoo.bedwars.mapselector.Main;
import me.leoo.bedwars.mapselector.configuration.Config;
import me.leoo.bedwars.mapselector.configuration.ConfigHandler;
import me.leoo.bedwars.mapselector.database.Database;
import me.leoo.bedwars.mapselector.menu.MapSelectorMenu;
import me.leoo.bedwars.mapselector.database.Yaml;
import me.leoo.bedwars.mapselector.utils.SelectorUtil;
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

	private static final ConfigHandler config = Config.config;
	private static final Database database = Main.getMapSelectorDatabase();

	@EventHandler
	public void InventoryClickEvent(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		if (item == null)
			return;
		if (item.getType().equals(Material.AIR))
			return;
		if (item.getItemMeta() == null)
			return;
		if (item.getItemMeta().getDisplayName() == null)
			return;

		String n1 = BedWars.nms.getTag(item, "n1");
		String n2 = BedWars.nms.getTag(item, "n2");
		String n3 = BedWars.nms.getTag(item, "n3");
		String n4 = BedWars.nms.getTag(item, "n4");
		String n5 = BedWars.nms.getTag(item, "n5");

		//first gui
		if (event.getView().getTitle().equals(config.getString("map_selector.menu.per_group_menu.title"))) {
			if (item.getItemMeta().getDisplayName().equals(config.getString("map_selector.menu.item.join_random.name").replace("{group_name}", SelectorUtil.firstLetterUpperCase(n1)))) {
				if(config.getString("map_selector.menu.item.join_random.command").equals("default_action")){
					List<String> groups;
					if(n1.contains(",")){
						groups = Arrays.asList(n1.split(","));
					}else{
						groups = Collections.singletonList(n1);
					}
					SelectorUtil.joinRandomGroup(player, groups.get(new Random().nextInt(groups.size())), false, false);
					player.closeInventory();
				}else{
					player.performCommand(config.getString("map_selector.menu.item.join_random.command"));
				}
			} else if (item.getItemMeta().getDisplayName().equals(config.getString("map_selector.menu.item.map_selector.name").replace("{group_name}", SelectorUtil.firstLetterUpperCase(n1)))) {
				if(config.getString("map_selector.menu.item.map_selector.command").equals("default_action")){
					MapSelectorMenu.OpenSelectorMenu(player, n1, 0, 0);
				}else{
					player.performCommand(config.getString("map_selector.menu.item.map_selector.command"));
				}
			} else if (item.getItemMeta().getDisplayName().equals(config.getString("map_selector.menu.item.close.name"))) {
				if(config.getString("map_selector.menu.item.close.command").equals("default_action")){
					player.closeInventory();
				}else{
					player.performCommand(config.getString("map_selector.menu.item.close.command"));
				}
			} else if (item.getItemMeta().getDisplayName().equals(config.getString("map_selector.menu.item.rejoin.name"))) {
				if(config.getString("map_selector.menu.item.rejoin.command").equals("default_action")){
					player.performCommand("bw rejoin");
				}else{
					player.performCommand(config.getString("map_selector.menu.item.rejoin.command"));
				}
			}
			for(String extraItems : config.getYml().getConfigurationSection("map_selector.menu.item").getKeys(false)){
				if(config.getBoolean("map_selector.menu.item." + extraItems + ".extra")){
					if(config.getInt("map_selector.menu.item." + extraItems + ".gui") == 1){
						if (item.getItemMeta().getDisplayName().equals(config.getString("map_selector.menu.item." + extraItems + ".name"))) {
							if(config.getString("map_selector.menu.item." + extraItems + ".command").equals("default_action")){
								if(player.isOp() || player.hasPermission("bwselector.reload")){
									player.sendMessage("§4§l[Map Selector] §7You clicked on an extra item in one of the selector menu. §7You must edit the command or disable this item from the plugin's config §c(plugins/BedWarsProxy/Addons/MapSelector/config.yml)§7. §7To edit the item's command you have to modify the string §ccommand (it's under map_selector.menu.item." + extraItems + ")§7. §7To disable this item you have to set to §cfalse §7the boolean §cenabled (it's under map_selector.menu.item." + extraItems + ")§7.");
								}
							}else{
								player.performCommand(config.getString("map_selector.menu.item." + extraItems + ".command"));
							}
						}
					}
				}
			}
			event.setCancelled(true);
		}
		//second gui
		if (event.getView().getTitle().equals(config.getString("map_selector.menu.selector.title"))) {
			if (item.getItemMeta().getDisplayName().equals(config.getString("map_selector.menu.item.map.name").replace("{map_name}", SelectorUtil.firstLetterUpperCase(n3)))) {
				if(config.getString("map_selector.menu.item.map.command").equals("default_action")){
					if(event.isRightClick()){
						Yaml.setFavorite(player, n1);
						MapSelectorMenu.OpenSelectorMenu(player, n2, SelectorUtil.getStartMaps(player), SelectorUtil.getCurrentPage(player));
					}else{
						if(SelectorUtil.getSelectionsType(player).equals(config.getString("map_selector.selection.unlimited_message"))){
							SelectorUtil.joinArena(player, n1, n2, true);
							player.closeInventory();
						}else if(!SelectorUtil.getSelectionsType(player).equals(config.getString("map_selector.selection.unlimited_message"))){
							if(database.getPlayerUses(player.getUniqueId()) < Integer.parseInt(SelectorUtil.getSelectionsType(player))){
								SelectorUtil.joinArena(player, n1, n2, false);
								player.closeInventory();
							}else{
								player.closeInventory();
								player.sendMessage(config.getString("map_selector.menu.limit_reached"));
							}
						}
					}
				}else{
					player.performCommand(config.getString("map_selector.menu.item.map.command"));
				}
			}else if (item.getItemMeta().getDisplayName().equals(config.getString("map_selector.menu.item.map_favorite.name").replace("{map_name}", SelectorUtil.firstLetterUpperCase(n3)))) {
				if(config.getString("map_selector.menu.item.map_favorite.command").equals("default_action")){
					if(event.isRightClick()){
						Yaml.unsetFavorite(player, n1);
						MapSelectorMenu.OpenSelectorMenu(player, n2, SelectorUtil.getStartMaps(player), SelectorUtil.getCurrentPage(player));
					}else{
						if(SelectorUtil.getSelectionsType(player).equals(config.getString("map_selector.selection.unlimited_message"))){
							SelectorUtil.joinArena(player, n1, n2, true);
							player.closeInventory();
						}else if(!SelectorUtil.getSelectionsType(player).equals(config.getString("map_selector.selection.unlimited_message"))){
							if(database.getPlayerUses(player.getUniqueId()) < Integer.parseInt(SelectorUtil.getSelectionsType(player))){
								SelectorUtil.joinArena(player, n1, n2, false);
								player.closeInventory();
							}else{
								player.closeInventory();
								player.sendMessage(config.getString("map_selector.menu.limit_reached"));
							}
						}
					}
				}else{
					player.performCommand(config.getString("map_selector.menu.item.map_favorite.command"));
				}
			} else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(config.getString("map_selector.menu.item.map_no_permissions_no_uses.name").replace("{map_name}", n4))) {
				if(config.getString("map_selector.menu.item.map_no_permissions_no_uses.command").equals("default_action")){
					player.closeInventory();
					player.sendMessage(config.getString("map_selector.menu.limit_reached"));
				}else{
					player.performCommand(config.getString("map_selector.menu.item.map_no_permissions_no_uses.command"));
				}
			} else if (item.getItemMeta().getDisplayName().equals(config.getString("map_selector.menu.item.random_map.name").replace("{group_name}", n2))) {
				if(config.getString("map_selector.menu.item.random_map.command").equals("default_action")){
					List<String> groups;
					if(n1.contains(",")){
						groups = Arrays.asList(n1.split(","));
					}else{
						groups = Collections.singletonList(n1);
					}
					SelectorUtil.joinRandomGroup(player, groups.get(new Random().nextInt(groups.size())), false, false);
					player.closeInventory();
				}else{
					player.performCommand(config.getString("map_selector.menu.item.random_map.command"));
				}
			}else if (item.getItemMeta().getDisplayName().equals(config.getString("map_selector.menu.item.random_favourite.name").replace("{group_name}", n2))) {
				if(config.getString("map_selector.menu.item.random_favourite.command").equals("default_action")){
					if(SelectorUtil.getSelectionsType(player).equals(config.getString("map_selector.selection.unlimited_message"))){
						SelectorUtil.joinRandomGroup(player, n2, true, true);
					}else if(!SelectorUtil.getSelectionsType(player).equals(config.getString("map_selector.selection.unlimited_message"))){
						if(database.getPlayerUses(player.getUniqueId()) < Integer.parseInt(SelectorUtil.getSelectionsType(player))){
							List<String> groups;
							if(n2.contains(",")){
								groups = Arrays.asList(n2.split(","));
							}else{
								groups = Collections.singletonList(n2);
							}
							SelectorUtil.joinRandomGroup(player, groups.get(new Random().nextInt(groups.size())), false, true);
							player.closeInventory();
						}else{
							player.closeInventory();
							player.sendMessage(config.getString("map_selector.menu.limit_reached"));
						}
					}
				}else{
					player.performCommand(config.getString("map_selector.menu.item.random_favourite.command"));
				}
			}else if (item.getItemMeta().getDisplayName().equals(config.getString("map_selector.menu.item.back.name"))) {
				if(config.getString("map_selector.menu.item.back.command").equals("default_action")){
					MapSelectorMenu.OpenGroupMenu(player, n2);
				}else{
					player.performCommand(config.getString("map_selector.menu.item.back.command"));
				}
			}else if (item.getItemMeta().getDisplayName().equals(config.getString("map_selector.menu.item.next_page.name"))) {
				if(config.getString("map_selector.menu.item.next_page.command").equals("default_action")){
					MapSelectorMenu.OpenSelectorMenu(player, n5, SelectorUtil.getStartMaps(player) + config.getString("map_selector.menu.slots").split(",").length, SelectorUtil.getCurrentPage(player) + 1);
				}else{
					player.performCommand(config.getString("map_selector.menu.item.next_page.command"));
				}
			}else if (item.getItemMeta().getDisplayName().equals(config.getString("map_selector.menu.item.previous_page.name"))) {
				if(config.getString("map_selector.menu.item.previous_page.command").equals("default_action")){
					MapSelectorMenu.OpenSelectorMenu(player, n5, SelectorUtil.getStartMaps(player) - config.getString("map_selector.menu.slots").split(",").length, SelectorUtil.getCurrentPage(player) - 1);
				}else{
					player.performCommand(config.getString("map_selector.menu.item.previous_page.command"));
				}
			}
			for(String extraItems : config.getYml().getConfigurationSection("map_selector.menu.item").getKeys(false)){
				if(config.getBoolean("map_selector.menu.item." + extraItems + ".extra")){
					if(config.getInt("map_selector.menu.item." + extraItems + ".gui") == 2){
						if (item.getItemMeta().getDisplayName().equals(config.getString("map_selector.menu.item." + extraItems + ".name"))) {
							if(config.getString("map_selector.menu.item." + extraItems + ".command").equals("default_action")){
								if(player.isOp() || player.hasPermission("bwselector.reload")){
									player.sendMessage("§4§l[Map Selector] §7You clicked on an extra item in one of the selector menu. §7You must edit the command or disable this item from the plugin's config §c(plugins/BedWarsProxy/Addons/MapSelector/config.yml)§7. §7To edit the item's command you have to modify the string §ccommand (it's under map_selector.menu.item." + extraItems + ")§7. §7To disable this item you have to set to §cfalse §7the boolean §cenabled (it's under map_selector.menu.item." + extraItems + ")§7.");
								}
							}else{
								player.performCommand(config.getString("map_selector.menu.item." + extraItems + ".command"));
							}
						}
					}
				}
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void InventoryDropEvent (PlayerDropItemEvent event){
		Player player = event.getPlayer();
		if (player.getOpenInventory().getTitle().equals(config.getString("map_selector.menu.per_group_menu.title"))) {
			event.setCancelled(true);
		}
	}
}
