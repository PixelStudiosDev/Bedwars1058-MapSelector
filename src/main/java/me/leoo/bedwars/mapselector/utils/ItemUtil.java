package me.leoo.bedwars.mapselector.utils;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.proxy.BedWarsProxy;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.leoo.bedwars.mapselector.MapSelector;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ItemUtil {

    public static ItemStack item(Material material, String headUrl, int data, String displayName, List<String> lore, boolean enchanted, String n1, String n2, String n3, String n4, String n5) {
        if (material == null) material = Material.STONE;
        if (material.equals(Material.WOOL)) {
            data = new Random().nextInt(16);
        }

        ItemStack itemStack;
        if (MapSelector.getPlugin().getBedwarsMode().equals(BedwarsMode.BEDWARS)) {
            itemStack = BedWars.nms.createItemStack(String.valueOf(material), 1, (short) data);
        } else {
            itemStack = BedWarsProxy.getItemAdapter().createItem(material, 1, (byte) data);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        if (enchanted) itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);

        if (material.equals(Material.SKULL_ITEM) && headUrl != null) {
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

        return itemStack;
    }
}
