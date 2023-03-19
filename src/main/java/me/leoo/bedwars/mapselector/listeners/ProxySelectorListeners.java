package me.leoo.bedwars.mapselector.listeners;

import com.andrei1058.bedwars.proxy.BedWarsProxy;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.menu.MapSelectorMenuProxy;
import me.leoo.bedwars.mapselector.utils.SelectorUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ProxySelectorListeners implements Listener {

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null)
            return;
        if (item.getType().equals(Material.AIR))
            return;
        if (item.getItemMeta() == null)
            return;
        if (item.getItemMeta().getDisplayName() == null)
            return;

        Player player = (Player) event.getWhoClicked();

        String tag1 = BedWarsProxy.getItemAdapter().getTag(item, "n1");
        String tag2 = BedWarsProxy.getItemAdapter().getTag(item, "n2");
        String tag3 = BedWarsProxy.getItemAdapter().getTag(item, "n3");
        String tag4 = BedWarsProxy.getItemAdapter().getTag(item, "n4");
        String tag5 = BedWarsProxy.getItemAdapter().getTag(item, "n5");

        if (tag3 != null && tag3.equals("true")) {
            //first gui
            if (event.getView().getTitle().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.per_group_menu.title"))) {
                if (item.getItemMeta().getDisplayName().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.join_random.name").replace("{group_name}", tag1 == null ? "" : SelectorUtil.firstLetterUpperCase(tag1)))) {
                    if (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.join_random.command").equals("default_action")) {
                        List<String> groups = new ArrayList<>();

                        if (tag1 != null) groups = Arrays.asList(tag1.split(","));

                        SelectorUtil.joinRandomGroup(player, groups.get(new Random().nextInt(groups.size())), false, false);
                        player.closeInventory();
                    } else {
                        player.performCommand(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.join_random.command"));
                    }
                } else if (item.getItemMeta().getDisplayName().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_selector.name").replace("{group_name}", tag1 == null ? "" : SelectorUtil.firstLetterUpperCase(tag1)))) {
                    if (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_selector.command").equals("default_action")) {
                        MapSelectorMenuProxy.openSecondGui(player, tag1, 0, 0);
                    } else {
                        player.performCommand(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_selector.command"));
                    }
                } else if (item.getItemMeta().getDisplayName().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.close.name"))) {
                    if (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.close.command").equals("default_action")) {
                        player.closeInventory();
                    } else {
                        player.performCommand(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.close.command"));
                    }
                } else if (item.getItemMeta().getDisplayName().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.rejoin.name"))) {
                    if (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.rejoin.command").equals("default_action")) {
                        player.performCommand("bw rejoin");
                    } else {
                        player.performCommand(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.rejoin.command"));
                    }
                }
                for (String extraItems : MapSelector.getPlugin().getMainConfig().getYml().getConfigurationSection("map_selector.menu.item").getKeys(false)) {
                    if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item." + extraItems + ".extra") &&
                            MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item." + extraItems + ".gui") == 1 &&
                            item.getItemMeta().getDisplayName().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item." + extraItems + ".name"))) {
                        if (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item." + extraItems + ".command").equals("default_action")) {
                            if (player.isOp() || player.hasPermission("bwselector.reload")) {
                                player.sendMessage("§4§l[Map Selector] §7You clicked on an extra item in one of the selector menu. §7You must edit the command or disable this item from the plugin's config §c(plugins/BedWarsProxy/Addons/MapSelector/config.yml)§7. §7To edit the item's command you have to modify the string §ccommand (it's under map_selector.menu.item." + extraItems + ")§7. §7To disable this item you have to set to §cfalse §7the boolean §cenabled (it's under map_selector.menu.item." + extraItems + ")§7.");
                            }
                        } else {
                            player.performCommand(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item." + extraItems + ".command"));
                        }
                    }
                }
            }
            event.setCancelled(true);

            //second gui
            if (event.getView().getTitle().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.selector.title"))) {
                if (item.getItemMeta().getDisplayName().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map.name").replace("{map_name}", tag4 == null ? "" : tag4))) {
                    if (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map.command").equals("default_action")) {
                        if (event.isRightClick()) {
                            MapSelector.getPlugin().getYamlConfig().setFavorite(player, tag1);
                            MapSelectorMenuProxy.openSecondGui(player, tag5, SelectorUtil.getStartMaps(player), SelectorUtil.getCurrentPage(player));
                        } else {
                            if (SelectorUtil.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.unlimited_message"))) {
                                SelectorUtil.joinArena(player, tag1, tag2, true);
                                player.closeInventory();
                            } else if (!SelectorUtil.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.unlimited_message"))) {
                                if (MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId()) < Integer.parseInt(SelectorUtil.getSelectionsType(player))) {
                                    SelectorUtil.joinArena(player, tag1, tag2, false);
                                    player.closeInventory();
                                } else {
                                    player.closeInventory();
                                    player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.limit_reached"));
                                }
                            }
                        }
                    } else {
                        player.performCommand(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map.command"));
                    }
                } else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_favorite.name").replace("{map_name}", tag4 == null ? "" : tag4))) {
                    if (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_favorite.command").equals("default_action")) {
                        if (event.isRightClick()) {
                            MapSelector.getPlugin().getYamlConfig().unsetFavorite(player, tag1);
                            MapSelectorMenuProxy.openSecondGui(player, tag5, SelectorUtil.getStartMaps(player), SelectorUtil.getCurrentPage(player));
                        } else {
                            if (SelectorUtil.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.unlimited_message"))) {
                                SelectorUtil.joinArena(player, tag1, tag2, true);
                                player.closeInventory();
                            } else if (!SelectorUtil.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.unlimited_message"))) {
                                if (MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId()) < Integer.parseInt(SelectorUtil.getSelectionsType(player))) {
                                    SelectorUtil.joinArena(player, tag1, tag2, false);
                                    player.closeInventory();
                                } else {
                                    player.closeInventory();
                                    player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.limit_reached"));
                                }
                            }
                        }
                    } else {
                        player.performCommand(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_favorite.command"));
                    }
                } else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_no_permissions_no_uses.name").replace("{map_name}", tag4 == null ? "" : tag4))) {
                    if (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_no_permissions_no_uses.command").equals("default_action")) {
                        player.closeInventory();
                        player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.limit_reached"));
                    } else {
                        player.performCommand(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.map_no_permissions_no_uses.command"));
                    }
                } else if (item.getItemMeta().getDisplayName().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.random_map.name").replace("{group_name}", tag2 == null ? "" : tag2))) {
                    if (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.random_map.command").equals("default_action")) {
                        List<String> groups = new ArrayList<>();

                        if (tag2 != null) groups = Arrays.asList(tag2.split(","));

                        SelectorUtil.joinRandomGroup(player, groups.get(new Random().nextInt(groups.size())), false, false);
                        player.closeInventory();
                    } else {
                        player.performCommand(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.random_map.command"));
                    }
                } else if (item.getItemMeta().getDisplayName().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.random_favourite.name").replace("{group_name}", tag2 == null ? "" : tag2))) {
                    if (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.random_favourite.command").equals("default_action")) {
                        if (SelectorUtil.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.unlimited_message"))) {
                            SelectorUtil.joinRandomGroup(player, tag2, true, true);
                        } else if (!SelectorUtil.getSelectionsType(player).equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.selection.unlimited_message"))) {
                            if (MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId()) < Integer.parseInt(SelectorUtil.getSelectionsType(player))) {
                                List<String> groups = new ArrayList<>();

                                if (tag2 != null) groups = Arrays.asList(tag2.split(","));

                                SelectorUtil.joinRandomGroup(player, groups.get(new Random().nextInt(groups.size())), false, true);
                                player.closeInventory();
                            } else {
                                player.closeInventory();
                                player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.limit_reached"));
                            }
                        }
                    } else {
                        player.performCommand(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.random_favourite.command"));
                    }
                } else if (item.getItemMeta().getDisplayName().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.back.name"))) {
                    if (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.back.command").equals("default_action")) {
                        MapSelectorMenuProxy.openFirstGui(player, tag2);
                    } else {
                        player.performCommand(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.back.command"));
                    }
                } else if (item.getItemMeta().getDisplayName().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.next_page.name"))) {
                    if (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.next_page.command").equals("default_action")) {
                        MapSelectorMenuProxy.openSecondGui(player, tag5, SelectorUtil.getStartMaps(player) + MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.slots").split(",").length, SelectorUtil.getCurrentPage(player) + 1);
                    } else {
                        player.performCommand(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.next_page.command"));
                    }
                } else if (item.getItemMeta().getDisplayName().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.previous_page.name"))) {
                    if (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.previous_page.command").equals("default_action")) {
                        MapSelectorMenuProxy.openSecondGui(player, tag5, SelectorUtil.getStartMaps(player) - MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.slots").split(",").length, SelectorUtil.getCurrentPage(player) - 1);
                    } else {
                        player.performCommand(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item.previous_page.command"));
                    }
                }
                for (String extraItems : MapSelector.getPlugin().getMainConfig().getYml().getConfigurationSection("map_selector.menu.item").getKeys(false)) {
                    if (MapSelector.getPlugin().getMainConfig().getBoolean("map_selector.menu.item." + extraItems + ".extra") &&
                            MapSelector.getPlugin().getMainConfig().getInt("map_selector.menu.item." + extraItems + ".gui") == 2 &&
                            item.getItemMeta().getDisplayName().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item." + extraItems + ".name"))) {
                        if (MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item." + extraItems + ".command").equals("default_action")) {
                            if (player.isOp() || player.hasPermission("bwselector.reload")) {
                                player.sendMessage("§4§l[Map Selector] §7You clicked on an extra item in one of the selector menu. §7You must edit the command or disable this item from the plugin's config §c(plugins/BedWarsProxy/Addons/MapSelector/config.yml)§7. §7To edit the item's command you have to modify the string §ccommand (it's under map_selector.menu.item." + extraItems + ")§7. §7To disable this item you have to set to §cfalse §7the boolean §cenabled (it's under map_selector.menu.item." + extraItems + ")§7.");
                            }
                        } else {
                            player.performCommand(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.item." + extraItems + ".command"));
                        }
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void inventoryDropEvent(PlayerDropItemEvent event) {
        if (event.getPlayer().getOpenInventory().getTitle().equals(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.per_group_menu.title"))) {
            event.setCancelled(true);
        }
    }
}
