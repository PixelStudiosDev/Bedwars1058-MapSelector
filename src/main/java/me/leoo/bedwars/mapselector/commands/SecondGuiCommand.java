package me.leoo.bedwars.mapselector.commands;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.menu.bedwars.ArenasMenu;
import me.leoo.bedwars.mapselector.menu.proxy.ArenasMenuProxy;
import me.leoo.bedwars.mapselector.utils.BedwarsMode;
import me.leoo.utils.bukkit.commands.Command;
import me.leoo.utils.bukkit.commands.CommandBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecondGuiCommand extends Command {

    public SecondGuiCommand() {
        super("bedwarsmap");

        setMainCommand(new CommandBuilder("bedwarsmap")
                .setAliases("bwmap")
                .setPlayersOnly(true)
                .setPlayerExecutor((player, args) -> {
                    List<String> groups = new ArrayList<>();

                    if (args.length != 1) {
                        player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.open.missing2"));
                    }

                    String group = args[0];

                    if (MapSelector.get().getBedwarsMode().equals(BedwarsMode.PROXY)) {

                        for (CachedArena arena : ArenaManager.getArenas()) {
                            if (!groups.contains(arena.getArenaGroup())) {
                                groups.add(arena.getArenaGroup());
                            }
                        }

                        for (String group1 : group.split(",")) {
                            if (!groups.contains(group1)) {
                                player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.open.group-doesnt-exists"));
                                return;
                            }
                        }

                        new ArenasMenuProxy(group).open(player);
                    } else {

                        for (IArena arena : Arena.getArenas()) {
                            if (!groups.contains(arena.getGroup())) {
                                groups.add(arena.getGroup());
                            }
                        }

                        for (String group1 : group.split(",")) {
                            if (!groups.contains(group1)) {
                                player.sendMessage(MapSelector.get().getMainConfig().getString("map-selector.messages.open.group-doesnt-exists"));
                                return;
                            }
                        }

                        new ArenasMenu(group).open(player);
                    }
                })
                .setTabComplete((commandSender, args) -> {
                    if (args.length == 1) {
                        List<String> groups = new ArrayList<>();

                        if (MapSelector.get().getBedwarsMode().equals(BedwarsMode.PROXY)) {
                            for (CachedArena arena : ArenaManager.getArenas()) {
                                if (!groups.contains(arena.getArenaGroup())) {
                                    groups.add(arena.getArenaGroup());
                                }
                            }
                        } else {
                            for (IArena arena : Arena.getArenas()) {
                                if (!groups.contains(arena.getGroup())) {
                                    groups.add(arena.getGroup());
                                }
                            }
                        }

                        return groups;
                    }

                    return Collections.emptyList();
                })
        );

    }

    @Override
    public String getNoPermissionMessage() {
        return null;
    }
}
