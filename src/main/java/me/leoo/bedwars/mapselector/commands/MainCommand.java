/*
 *
 */

package me.leoo.bedwars.mapselector.commands;

import me.leoo.bedwars.mapselector.MapSelector;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainCommand extends BukkitCommand {

    public MainCommand(String name) {
        super(name);
        setAliases(Collections.singletonList("bwselector"));
    }

    @Override
    public boolean execute(CommandSender sender, String string, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = (Player) sender;


        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (player.hasPermission("bwselector.reload")) {
                    MapSelector.getPlugin().getMainConfig().reload();
                    MapSelector.getPlugin().getCacheConfig().reload();
                    player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.reload.success"));
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("resetuses")) {
                if (player.hasPermission("bwselector.reset")) {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == null) {
                        player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.reset-uses.not-found"));
                    } else {
                        MapSelector.getPlugin().getMapSelectorDatabase().setPlayerUses(target.getUniqueId(), 0);
                        player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.reset-uses.success").replace("{player}", target.getName()));
                    }
                }
            } else {
                player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.reset-uses.missing"));
            }
        }else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setuses")) {
                if (player.hasPermission("bwselector.set")) {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == null) {
                        player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.set-uses.not-found"));
                    } else {
                        int uses = 0;
                        try{
                            uses = Integer.parseInt(args[2]);
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                        }

                        MapSelector.getPlugin().getMapSelectorDatabase().setPlayerUses(target.getUniqueId(), uses);
                        player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.set-uses.success")
                                .replace("{player}", target.getName())
                                .replace("{uses}", String.valueOf(uses)));
                    }
                }
            } else {
                player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.set-uses.missing"));
            }
        }

        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> tab = new ArrayList<>();

        if (args.length == 1) {
            tab.add("reload");
            tab.add("resetuses");
            tab.add("setuses");
        }

        return tab;
    }
}
