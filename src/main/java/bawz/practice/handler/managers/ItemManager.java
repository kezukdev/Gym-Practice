package bawz.practice.handler.managers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import bawz.practice.Main;

public class ItemManager {
	
	private Main main = Main.getInstance();
	
	private PlayerInventory spawnInventory;
	private PlayerInventory queueInventory;
	
	public void giveItems(final Player player, final String type) {
		player.getInventory().setContents(type.equalsIgnoreCase("spawn-items") ? spawnInventory.getContents() : queueInventory.getContents());
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
		final PlayerInventory inventory = type.equalsIgnoreCase("spawn-items") ? this.spawnInventory : this.queueInventory;
		inventory.clear();
		for (int i = 0; i < 9; i++) {
			inventory.setItem(i, this.configToItem(type, String.valueOf(i)));
		}
	}
}
