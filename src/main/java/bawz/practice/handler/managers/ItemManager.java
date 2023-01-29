package bawz.practice.handler.managers;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import bawz.practice.Main;
import net.minecraft.util.com.google.common.collect.Maps;

public class ItemManager {
	
	private Main main = Main.getInstance();
	
	private Map<String, Map<Integer, ItemStack>> inventory = Maps.newHashMap();
	
	public void giveItems(final Player player, final String type) {
		player.getInventory().clear();
		for (Entry<Integer, ItemStack> item : inventory.get(type.equals("spawn-items") ? "spawn-items" : "queue-items").entrySet()) {
			player.getInventory().setItem(item.getKey(), item.getValue());
		}
		player.updateInventory();
	}
	
	private ItemStack configToItem(final String type, final String number) {
		ItemStack item = new ItemStack(Material.valueOf(this.main.getConfig().getString(type + "." + number + ".material")));
		if (item.getType() != Material.AIR) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString(type + "." + number + ".displayname")));
			item.setItemMeta(meta);	
		}
		return item;
	}
	
	public void loadItems(final String type) {
		for (int i = 0; i < 9; i++) {
			Map<Integer, ItemStack> items = Maps.newHashMap();
			items.put(i, configToItem(type, String.valueOf(i)));
			inventory.put(type, items);
		}
		System.out.println("Items > " + type + " loaded!");
	}
}
