package me.leoo.bedwars.mapselector.commands;

import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.utils.bukkit.commands.Command;
import me.leoo.utils.bukkit.commands.CommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainCommand extends Command {

    public MainCommand() {
        super("bedwarsselector");

        setMainCommand(new CommandBuilder("bedwarsselector")
                .setAliases("bwselector")
                .setPlayersOnly(true)
                .setOperatorsOnly(true)
                .setPlayerExecutor((player, args) -> {
                    switch (args.length) {
                        case 1:
                            if (args[0].equalsIgnoreCase("reload")) {
                                if (player.hasPermission("bwselector.reload")) {
                                    MapSelector.get().getMainConfig().reload();
                                    MapSelector.get().getCacheConfig().reload();

                                    player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.reload.success"));
                                }
                            }
                            break;
                        case 2:
                            if (args[0].equalsIgnoreCase("resetuses")) {
                                if (player.hasPermission("bwselector.reset")) {
                                    Player target = Bukkit.getPlayerExact(args[1]);
                                    if (target == null) {
                                        player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.reset-uses.not-found"));
                                    } else {
                                        MapSelector.get().getDatabaseManager().setPlayerUses(target.getUniqueId(), 0);
                                        player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.reset-uses.success").replace("{player}", target.getName()));
                                    }
                                }
                            } else {
                                player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.reset-uses.missing"));
                            }
                            break;
                        case 3:
                            if (args[0].equalsIgnoreCase("setuses")) {
                                if (player.hasPermission("bwselector.set")) {
                                    Player target = Bukkit.getPlayerExact(args[1]);
                                    if (target == null) {
                                        player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.set-uses.not-found"));
                                    } else {
                                        int uses = 0;
                                        try {
                                            uses = Integer.parseInt(args[2]);
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }

                                        MapSelector.get().getDatabaseManager().setPlayerUses(target.getUniqueId(), uses);
                                        player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.set-uses.success")
                                                .replace("{player}", target.getName())
                                                .replace("{uses}", String.valueOf(uses)));
                                    }
                                }
                            } else {
                                player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.set-uses.missing"));
                            }
                            break;
                    }
                })
                .setTabComplete((sender, args) -> {
                    List<String> tab = new ArrayList<>();

                    if (args.length == 1) {
                        tab.addAll(Arrays.asList("reload", "resetuses", "setuses"));
                    }

                    return tab;
                })
        );
    }

    @Override
    public String getNoPermissionMessage() {
        return null;
    }

}
