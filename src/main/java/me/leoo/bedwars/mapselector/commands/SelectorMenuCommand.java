package me.leoo.bedwars.mapselector.commands;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.menu.MapSelectorMenu;
import me.leoo.bedwars.mapselector.menu.MapSelectorMenuProxy;
import me.leoo.bedwars.mapselector.utils.BedwarsMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectorMenuCommand extends BukkitCommand {

    public SelectorMenuCommand(String name) {
        super(name);
        setAliases(Collections.singletonList("bwmap"));
    }

    @Override
    public boolean execute(CommandSender sender, String string, String[] args) {
        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;

        if (args.length == 1) {
            String group = args[0];

            if (MapSelector.getPlugin().getBedwarsMode().equals(BedwarsMode.BEDWARSPROXY)) {
                List<String> groups = new ArrayList<>();

                for (CachedArena arena : ArenaManager.getArenas()) {
                    if (!groups.contains(arena.getArenaGroup())) {
                        groups.add(arena.getArenaGroup());
                    }
                }

                for (String group1 : group.split(",")) {
                    if (groups.contains(group1)) {
                        MapSelectorMenuProxy.openSecondGui(player, group1, 0, 0);
                    } else {
                        player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.open.group_doesnt_exist"));
                    }
                }
            } else {
                for (String group1 : group.split(",")) {
                    if (BedWars.config.getList("arenaGroups").contains(group1)) {
                        MapSelectorMenu.openSecondGui(player, group1, 0, 0);
                    } else {
                        player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.open.group_doesnt_exist"));
                    }
                }
            }
        } else {
            player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.open.missing"));
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            if (MapSelector.getPlugin().getBedwarsMode().equals(BedwarsMode.BEDWARSPROXY)) {
                List<String> groups = new ArrayList<>();

                for (CachedArena arena : ArenaManager.getArenas()) {
                    if (!groups.contains(arena.getArenaGroup())) {
                        groups.add(arena.getArenaGroup());
                    }
                }

                return groups;
            } else {
                return BedWars.config.getList("arenaGroups");
            }
        }
        return Collections.emptyList();
    }
}
