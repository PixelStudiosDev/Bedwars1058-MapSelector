package me.leoo.bedwars.mapselector.utils;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.proxy.BedWarsProxy;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ItemUtil {

	public static ItemStack item(Material material, String headUrl, int data, String displayName, List<String> lore, boolean enchanted, String nbt1, String nbt2, String nbt3, String nbt4, String nbt5){

		if(material == null) material = Material.STONE;

		List<Integer> datas = new ArrayList<>();
		if(material.equals(Material.WOOL)){
			for(int k = 0; k <= 15; k++){
				datas.add(k);
			}
			data = datas.get(new Random().nextInt(datas.size()));
		}
		ItemStack i;
		if(Main.bungee){
			i = BedWarsProxy.getItemAdapter().createItem(material, 1, (byte) data);
		}else{
			i = BedWars.nms.createItemStack(String.valueOf(material), 1, (short) data);
		}
		ItemMeta itemMeta = i.getItemMeta();
		itemMeta.setDisplayName(displayName);
		itemMeta.setLore(lore);
		if(enchanted){
			itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
		}
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		i.setItemMeta(itemMeta);

		if(material.equals(Material.SKULL_ITEM)){
			if(headUrl != null){
				SkullMeta skullMeta = (SkullMeta) i.getItemMeta();
				GameProfile profile = new GameProfile(UUID.randomUUID(), null);
				profile.getProperties().put("textures", new Property("textures", headUrl));
				try {
					Field field = skullMeta.getClass().getDeclaredField("profile");
					field.setAccessible(true);
					field.set(skullMeta, profile);
				}catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException exception ){
					exception.printStackTrace();
					return null;
				}
				i.setItemMeta(skullMeta);
			}
		}

		if(Main.bungee){
			if(nbt1 == null){
				i = BedWarsProxy.getItemAdapter().addTag(i, "n1", "");
			}else{
				i = BedWarsProxy.getItemAdapter().addTag(i, "n1", nbt1);
			}
			if(nbt2 == null){
				i = BedWarsProxy.getItemAdapter().addTag(i, "n2", "");
			}else{
				i = BedWarsProxy.getItemAdapter().addTag(i, "n2", nbt2);
			}
			if(nbt3 == null){
				i = BedWarsProxy.getItemAdapter().addTag(i, "n3", "");
			}else{
				i = BedWarsProxy.getItemAdapter().addTag(i, "n3", nbt3);
			}
			if(nbt4 == null){
				i = BedWarsProxy.getItemAdapter().addTag(i, "n4", "");
			}else{
				i = BedWarsProxy.getItemAdapter().addTag(i, "n4", nbt4);
			}
			if(nbt5 == null){
				i = BedWarsProxy.getItemAdapter().addTag(i, "n5", "");
			}else{
				i = BedWarsProxy.getItemAdapter().addTag(i, "n5", nbt5);
			}
		}else{
			if(nbt1 == null){
				i = BedWars.nms.setTag(i, "n1", "");
			}else{
				i = BedWars.nms.setTag(i, "n1", nbt1);
			}
			if(nbt2 == null){
				i = BedWars.nms.setTag(i, "n2", "");
			}else{
				i = BedWars.nms.setTag(i, "n2", nbt2);
			}
			if(nbt3 == null){
				i = BedWars.nms.setTag(i, "n3", "");
			}else{
				i = BedWars.nms.setTag(i, "n3", nbt3);
			}
			if(nbt4 == null){
				i = BedWars.nms.setTag(i, "n4", "");
			}else{
				i = BedWars.nms.setTag(i, "n4", nbt4);
			}
			if(nbt5 == null){
				i = BedWars.nms.setTag(i, "n5", "");
			}else{
				i = BedWars.nms.setTag(i, "n5", nbt5);
			}
		}

		return i;
	}
}
