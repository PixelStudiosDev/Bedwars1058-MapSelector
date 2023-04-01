package me.leoo.bedwars.mapselector.commands;

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

public class FirstGuiCommand extends BukkitCommand {

    public FirstGuiCommand(String name) {
        super(name);
        setAliases(Collections.singletonList("bwmenu"));
    }

    @Override
    public boolean execute(CommandSender sender, String string, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = (Player) sender;

        if (args.length == 1) {
            String group = args[0];
            List<String> groups = new ArrayList<>();

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

                SelectorMenuProxy.openFirstGui(player, group);
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

                SelectorMenu.openFirstGui(player, group);
            }
        } else {
            player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.open.missing"));
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            List<String> groups = new ArrayList<>();

            if (MapSelector.getPlugin().getBedwarsMode().equals(BedwarsMode.BEDWARSPROXY)) {
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
    }
}
