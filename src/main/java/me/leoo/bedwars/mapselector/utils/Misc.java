/*
 *
 */

package me.leoo.bedwars.mapselector.utils;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.proxy.BedWarsProxy;
import com.andrei1058.bedwars.proxy.api.ArenaStatus;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.database.Yaml;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class Misc {

    public static String getSelectionsType(Player player) {
        String type = String.valueOf(0);
        for (String s : MapSelector.getPlugin().getMainConfig().getYml().getConfigurationSection("map-selector.selections.selections").getKeys(false)) {
            if (player.hasPermission(MapSelector.getPlugin().getMainConfig().getString("map-selector.selections.selections." + s + ".permission"))) {
                if (MapSelector.getPlugin().getMainConfig().getBoolean("map-selector.selections.selections." + s + ".unlimited")) {
                    type = MapSelector.getPlugin().getMainConfig().getString("map-selector.selections.unlimited-message");
                } else {
                    type = String.valueOf(MapSelector.getPlugin().getMainConfig().getInt("map-selector.selections.selections." + s + ".daily-uses"));
                }
            }
        }
        return type;
    }

    public static void joinRandomGroup(Player player, String group, boolean unlimited, boolean favorite) {
        if (MapSelector.getPlugin().getBedwarsMode().equals(BedwarsMode.PROXY)) {
            List<CachedArena> arenas = new ArrayList<>();
            List<CachedArena> arenas1;

            String noMapsMessage;

            if (favorite) {
                arenas1 = new ArrayList<>(Yaml.getFavoritesBungee(player, group));
                noMapsMessage = MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.no-favorites-maps");
            } else {
                arenas1 = new ArrayList<>(ArenaManager.getArenas());
                noMapsMessage = MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.no-maps");
            }

            for (CachedArena cachedArena : arenas1) {
                if (Arrays.asList(group.split(",")).contains(cachedArena.getArenaGroup())
                        && (cachedArena.getStatus().equals(ArenaStatus.WAITING) || cachedArena.getStatus().equals(ArenaStatus.STARTING))
                        && cachedArena.getCurrentPlayers() < cachedArena.getMaxPlayers()) {
                    arenas.add(cachedArena);
                }
            }

            if (arenas.isEmpty()) {
                player.sendMessage(noMapsMessage);
            } else {
                Collections.shuffle(arenas);
                arenas.sort((a1, a2) -> Integer.compare(a2.getCurrentPlayers(), a1.getCurrentPlayers()));

                joinArena(player, arenas.get(0).getArenaName(), arenas.get(0).getArenaGroup(), unlimited);
            }
        } else {
            List<IArena> arenas = new ArrayList<>();
            List<IArena> arenas1;

            String noMapsMessage;

            if (favorite) {
                arenas1 = new ArrayList<>(Yaml.getFavorites(player, group));
                noMapsMessage = MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.no-favorites-maps");
            } else {
                arenas1 = new ArrayList<>(Arena.getArenas());
                noMapsMessage = MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.no-maps");
            }

            for (IArena iArena : arenas1) {
                if (Arrays.asList(group.split(",")).contains(iArena.getGroup())
                        && (iArena.getStatus().equals(GameState.waiting) || iArena.getStatus().equals(GameState.starting))
                        && iArena.getPlayers().size() < iArena.getMaxPlayers()) {
                    arenas.add(iArena);
                }
            }

            if (arenas.isEmpty()) {
                player.sendMessage(noMapsMessage);
            } else {
                Collections.shuffle(arenas);
                arenas.sort((a1, a2) -> Integer.compare(a2.getPlayers().size(), a1.getPlayers().size()));

                joinArena(player, arenas.get(0).getArenaName(), arenas.get(0).getGroup(), unlimited);
            }
        }
    }

    public static void joinArena(Player player, String name, String group, boolean unlimited) {
        if (MapSelector.getPlugin().getBedwarsMode().equals(BedwarsMode.PROXY)) {
            List<CachedArena> arenas = new ArrayList<>();

            for (CachedArena cachedArena : ArenaManager.getArenas()) {
                if (cachedArena.getArenaGroup().equals(group)
                        && cachedArena.getArenaName().equals(name)
                        && (cachedArena.getStatus().equals(ArenaStatus.WAITING) || cachedArena.getStatus().equals(ArenaStatus.STARTING))
                        && cachedArena.getCurrentPlayers() < cachedArena.getMaxPlayers()) {
                    arenas.add(cachedArena);
                }
            }

            if (arenas.isEmpty()) {
                player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.no-maps"));
                return;
            }

            if (BedWarsProxy.getParty().hasParty(player.getUniqueId()) && BedWarsProxy.getParty().getMembers(player.getUniqueId()).size() > 1) {
                if (!BedWarsProxy.getParty().isOwner(player.getUniqueId())) {
                    player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.not-party-leader"));
                    return;
                }
                for (UUID uuid : BedWarsProxy.getParty().getMembers(player.getUniqueId())) {
                    Player player1 = Bukkit.getPlayer(uuid);
                    arenas.get(0).addPlayer(player1, player1.getName());
                }
            } else {
                arenas.get(0).addPlayer(player, player.getName());
            }
        } else {
            List<IArena> arenas = new ArrayList<>();

            for (IArena iArena : Arena.getArenas()) {
                if (iArena.getGroup().equals(group)
                        && iArena.getArenaName().equals(name)
                        && (iArena.getStatus().equals(GameState.waiting) || iArena.getStatus().equals(GameState.starting))
                        && iArena.getPlayers().size() < iArena.getMaxPlayers()) {
                    arenas.add(iArena);
                }
            }

            if (arenas.isEmpty()) {
                player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.no-maps"));
                return;
            }

            if (BedWars.getParty().hasParty(player) && BedWars.getParty().getMembers(player).size() > 1) {
                if (!BedWars.getParty().isOwner(player)) {
                    player.sendMessage(MapSelector.getPlugin().getMainConfig().getString("map-selector.messages.not-party-leader"));
                    return;
                }
                for (Player player1 : BedWars.getParty().getMembers(player)) {
                    arenas.get(0).addPlayer(player1, false);
                }
            } else {
                arenas.get(0).addPlayer(player, false);
            }
        }

        Yaml.addMapJoin(player, name);

        if (!unlimited) {
            MapSelector.getPlugin().getMapSelectorDatabase().setPlayerUses(player.getUniqueId(), MapSelector.getPlugin().getMapSelectorDatabase().getPlayerUses(player.getUniqueId()) + 1);
        }
    }

    public static void checkDate() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (day != MapSelector.getPlugin().getMainConfig().getInt("map-selector.last-date")) {
            MapSelector.getPlugin().getMapSelectorDatabase().setAllPlayersUses(0);
            MapSelector.getPlugin().getMainConfig().set("map-selector.last-date", day);
        }
    }

    public static ItemStack item(String material, String headUrl, int data, String displayName, List<String> lore, boolean enchanted, String n1, String n2, String n3, String n4, String n5) {
        if (material.equals("WOOL")) {
            data = new Random().nextInt(16);
        }

        ItemStack itemStack;
        if (MapSelector.getPlugin().getBedwarsMode().equals(BedwarsMode.BEDWARS)) {
            itemStack = BedWars.nms.createItemStack(material, 1, (short) data);
            if(itemStack == null){
                itemStack = BedWars.nms.createItemStack("STONE", 1, (short) data);
            }
        } else {
            itemStack = BedWarsProxy.getItemAdapter().createItem(material, 1, (byte) data);
            if(itemStack == null){
                itemStack = BedWarsProxy.getItemAdapter().createItem("STONE", 1, (byte) data);
            }
        }

        if(itemStack != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(displayName);
            itemMeta.setLore(lore);
            if (enchanted) itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
            itemStack.setItemMeta(itemMeta);

            if (material.equals("SKULL_ITEM") && headUrl != null) {
                SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
                GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                profile.getProperties().put("textures", new Property("textures", headUrl));
                try {
                    Field field = skullMeta.getClass().getDeclaredField("profile");
                    field.setAccessible(true);
                    field.set(skullMeta, profile);
                } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException exception) {
                    exception.printStackTrace();
                    return null;
                }
                itemStack.setItemMeta(skullMeta);
            }

            if (MapSelector.getPlugin().getBedwarsMode().equals(BedwarsMode.BEDWARS)) {
                itemStack = BedWars.nms.setTag(itemStack, "n1", n1 == null ? "" : n1);
                itemStack = BedWars.nms.setTag(itemStack, "n2", n2 == null ? "" : n2);
                itemStack = BedWars.nms.setTag(itemStack, "n3", n3 == null ? "" : n3);
                itemStack = BedWars.nms.setTag(itemStack, "n4", n4 == null ? "" : n4);
                itemStack = BedWars.nms.setTag(itemStack, "n5", n5 == null ? "" : n5);
            } else {
                itemStack = BedWarsProxy.getItemAdapter().addTag(itemStack, "n1", n1 == null ? "" : n1);
                itemStack = BedWarsProxy.getItemAdapter().addTag(itemStack, "n2", n2 == null ? "" : n2);
                itemStack = BedWarsProxy.getItemAdapter().addTag(itemStack, "n3", n3 == null ? "" : n3);
                itemStack = BedWarsProxy.getItemAdapter().addTag(itemStack, "n4", n4 == null ? "" : n4);
                itemStack = BedWarsProxy.getItemAdapter().addTag(itemStack, "n5", n5 == null ? "" : n5);
            }
        }

        return itemStack;
    }
}
