package bawz.practice.handler.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import bawz.practice.Main;
import bawz.practice.ladder.Ladder;

public class InventoryManager {
	
	private final Main main;
	private Inventory[] 
	private Inventory[] queue = new Inventory[2];
	public Inventory[] getQueue() { return queue; }
	
	public InventoryManager(final Main main) {
		this.main = main;
		this.queue[0] = Bukkit.createInventory(null, Boolean.valueOf(this.main.getConfig().getString("inventory.automatic-calcul-size")) ? this.calculateSize(this.main.getLadders().size()) : this.main.getConfig().getInt("inventory.casual.size"), ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("inventory.casual.name")));
		this.queue[1] = Bukkit.createInventory(null, Boolean.valueOf(this.main.getConfig().getString("inventory.automatic-calcul-size")) ? this.calculateSize(this.main.getLadders().size()) : this.main.getConfig().getInt("inventory.ranked.size"), ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("inventory.ranked.name")));
		this.refreshInventory();
	}
	
	public void refreshInventory() {
		for (Ladder ladder : this.main.getLadders()) {
			final ItemStack item = new ItemStack(ladder.getIcon());
			final ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ladder.getDisplayName()));
			item.setItemMeta(meta);
			for (Inventory inventory : queue) {
				if (inventory == queue[1] && !ladder.isRanked()) return;
				inventory.setItem(ladder.getSlots(), item);
			}
		}
	}
	
	private int calculateSize(int size) {
		int sizeNeeded = size / 9;
		if ((size % 9) != 0) {
			sizeNeeded += 1;
		}
		return (sizeNeeded == 0 ? 9 : sizeNeeded * 9);
	}

}
