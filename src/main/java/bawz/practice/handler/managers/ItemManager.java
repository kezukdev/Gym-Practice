package bawz.practice.handler.managers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import bawz.practice.Main;

public class ItemManager {
	
	private Main main = Main.getInstance();
	
	public void giveItems(final Player player, final String type) {
		player.getInventory().clear();
		for (int i = 0; i < 9; i++) {
			player.getInventory().setItem(i, this.configToItem(type, String.valueOf(i)));
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

}
