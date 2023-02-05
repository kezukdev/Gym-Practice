package bawz.practice.handler.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import bawz.practice.Main;

public class ItemManager {
	
	private Main main;
	
	private Map<String, Map<Integer, ItemStack>> inventory = new HashMap<>();
	
	public ItemManager(final Main main) { this.main = main; }
	
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
		Map<Integer, ItemStack> items = new HashMap<>();
		for (int i = 0; i < 9; i++) {
			items.put(i, configToItem(type, String.valueOf(i)));
			inventory.put(type, items);
		}
	}
}
