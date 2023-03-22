package me.leoo.bedwars.mapselector.commands;

import me.leoo.bedwars.mapselector.MapSelector;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ReloadCommand extends BukkitCommand {

    public ReloadCommand(String name) {
        super(name);
        setPermission("bwselector.reload");
        setAliases(Collections.singletonList("bwselector"));
    }

    @Override
    public boolean execute(CommandSender sender, String string, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = (Player) sender;

        if (player.hasPermission("bwselector.reload")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    MapSelector.getPlugin().getMainConfig().reload();
                    MapSelector.getPlugin().getCacheConfig().reload();
                    player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.reload.success"));
                }
            } else {
                player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map_selector.menu.reload.missing"));
            }
        }
        return false;
    }
}
