package saturne.practice.handler.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

import saturne.practice.Main;

public class InventoryManager {
	
	private final Main main = Main.getInstance();
	private Inventory casualInventory;
	private Inventory rankedInventory;
	
	public InventoryManager() {
		this.casualInventory = Bukkit.createInventory(null, Boolean.valueOf(this.main.getConfig().getString("inventory.automatic-calcul-size")) ? this.calculateSize(this.main.getLadders().size()) : this.main.getConfig().getInt("inventory.casual.size"), ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("inventory.casual.name")));
		this.rankedInventory = Bukkit.createInventory(null, Boolean.valueOf(this.main.getConfig().getString("inventory.automatic-calcul-size")) ? this.calculateSize(this.main.getLadders().size()) : this.main.getConfig().getInt("inventory.ranked.size"), ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("inventory.ranked.name")));
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
