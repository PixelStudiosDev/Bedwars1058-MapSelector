package me.leoo.bedwars.mapselector.utils;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.server.VersionSupport;
import com.andrei1058.bedwars.proxy.BedWarsProxy;
import com.andrei1058.bedwars.proxy.libs.ItemStackSupport;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.leoo.bedwars.mapselector.Main;
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

		ItemStack item;
		if (Main.getMode().equals(BedwarsMode.BEDWARS)) {
			item = BedWars.nms.createItemStack(String.valueOf(material), 1, (short) data);
		} else {
			item = BedWarsProxy.getItemAdapter().createItem(material, 1, (byte) data);
		}

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		if (enchanted) meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);

		if (material.equals(Material.SKULL_ITEM)) {
			if (headUrl != null) {
				SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
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
				item.setItemMeta(skullMeta);
			}
		}

		if (Main.getMode().equals(BedwarsMode.BEDWARS)) {
			VersionSupport nms = BedWars.nms;
			item = nms.setTag(item, "n1", n1 == null ? "" : n1);
			item = nms.setTag(item, "n2", n2 == null ? "" : n2);
			item = nms.setTag(item, "n3", n3 == null ? "" : n3);
			item = nms.setTag(item, "n4", n4 == null ? "" : n4);
			item = nms.setTag(item, "n5", n5 == null ? "" : n5);
		} else {
			ItemStackSupport itemAdapter = BedWarsProxy.getItemAdapter();
			item = itemAdapter.addTag(item, "n1", n1 == null ? "" : n1);
			item = itemAdapter.addTag(item, "n2", n2 == null ? "" : n2);
			item = itemAdapter.addTag(item, "n3", n3 == null ? "" : n3);
			item = itemAdapter.addTag(item, "n4", n4 == null ? "" : n4);
			item = itemAdapter.addTag(item, "n5", n5 == null ? "" : n5);
		}

		return item;
	}
}
