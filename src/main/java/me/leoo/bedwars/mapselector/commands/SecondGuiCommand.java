package me.leoo.bedwars.mapselector.commands;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.menu.SelectorMenu;
import me.leoo.bedwars.mapselector.menu.SelectorMenuProxy;
import me.leoo.bedwars.mapselector.utils.BedwarsMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecondGuiCommand extends BukkitCommand {

    public SecondGuiCommand(String name) {
        super(name);
        setAliases(Collections.singletonList("bwmap"));
    }

    @Override
    public boolean execute(CommandSender sender, String string, String[] args) {
        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;
        List<String> groups = new ArrayList<>();

        if (args.length == 1) {
            String group = args[0];

            if (MapSelector.getPlugin().getBedwarsMode().equals(BedwarsMode.BEDWARSPROXY)) {

                for (CachedArena arena : ArenaManager.getArenas()) {
                    if (!groups.contains(arena.getArenaGroup())) {
                        groups.add(arena.getArenaGroup());
                    }
                }

                for (String group1 : group.split(",")) {
                    if (!groups.contains(group1)) {
                        player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.open.group-doesnt-exists"));
                        return false;
                    }
                }

                SelectorMenuProxy.openSecondGui(player, group, 0);
            } else {

                for (IArena arena : Arena.getArenas()) {
                    if (!groups.contains(arena.getGroup())) {
                        groups.add(arena.getGroup());
                    }
                }

                for (String group1 : group.split(",")) {
                    if (!groups.contains(group1)) {
                        player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.open.group-doesnt-exists"));
                        return false;
                    }
                }

                SelectorMenu.openSecondGui(player, group, 0);
            }
        } else {
            player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.open.missing2"));
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
