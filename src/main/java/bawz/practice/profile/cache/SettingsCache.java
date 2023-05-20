package bawz.practice.profile.cache;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import bawz.practice.Main;

public class SettingsCache {
	
	private Inventory[] inventory = new Inventory[1];
	public Inventory[] getInventory() { return inventory; }
	private boolean scoreboard;
	
	public SettingsCache(final Main main, boolean scoreboard) {
		this.inventory[0] = Bukkit.createInventory(null, main.getInventoryLoader().getSettingsSize(), main.getInventoryLoader().getSettingsName());
		this.scoreboard = scoreboard;
		this.loadSettingsInventory(main);
	}

	private void loadSettingsInventory(final Main main) {
		for (Entry<Integer, ItemStack> item : main.getManagerHandler().getItemManager().getInventory().get("inventory.settings.items").entrySet()) {
			inventory[0].setItem(item.getKey(), item.getValue());
		}
	}

}
