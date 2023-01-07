package saturne.practice.handler.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import saturne.practice.Main;
import saturne.practice.ladder.Ladder;

public class InventoryManager {
	
	private final Main main = Main.getInstance();
	private Inventory casualInventory;
	private Inventory rankedInventory;
	
	public InventoryManager() {
		this.casualInventory = Bukkit.createInventory(null, Boolean.valueOf(this.main.getConfig().getString("inventory.automatic-calcul-size")) ? this.calculateSize(this.main.getLadders().size()) : this.main.getConfig().getInt("inventory.casual.size"), ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("inventory.casual.name")));
		this.rankedInventory = Bukkit.createInventory(null, Boolean.valueOf(this.main.getConfig().getString("inventory.automatic-calcul-size")) ? this.calculateSize(this.main.getLadders().size()) : this.main.getConfig().getInt("inventory.ranked.size"), ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("inventory.ranked.name")));
		for (Ladder ladder : this.main.getLadders()) {
			final ItemStack item = new ItemStack(ladder.getIcon());
			final ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ladder.getDisplayName()));
			item.setItemMeta(meta);
			this.casualInventory.setItem(ladder.getSlots(), item);
			this.rankedInventory.setItem(ladder.getSlots(), item);
		}
	}
	
	public Inventory getCasualInventory() {
		return casualInventory;
	}
	
	public Inventory getRankedInventory() {
		return rankedInventory;
	}
	
	private int calculateSize(int size) {
		int sizeNeeded = size / 9;
		if ((size % 9) != 0) {
			sizeNeeded += 1;
		}
		return (sizeNeeded * 9);
	}

}
