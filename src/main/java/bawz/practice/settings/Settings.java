package bawz.practice.settings;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import bawz.practice.Main;

public class Settings {
	
	private Inventory[] inventory = new Inventory[1];
	public Inventory[] getInventory() { return inventory; }
	private Main main;
	private boolean scoreboard;
	
	public Settings(final Main main, boolean scoreboard) {
		this.main = main;
		this.inventory[0] = Bukkit.createInventory(null, main.getInventoryLoader().getSettingsSize(), main.getInventoryLoader().getSettingsName());
		this.scoreboard = scoreboard;
		this.loadSettingsInventory();
	}

	private void loadSettingsInventory() {
		for (Entry<Integer, ItemStack> item : main.getManagerHandler().getItemManager().getInventory().get("inventory.settings.items").entrySet()) {
			if (item.getValue().getType().equals(Material.AIR)) return;
			item.getValue().getItemMeta().setDisplayName(item.getValue().getItemMeta().getDisplayName().replace("%sbStatus%", scoreboard ? ChatColor.valueOf(main.getInventoryLoader().getEnableColor()) + main.getInventoryLoader().getDisplayerEnable() : ChatColor.valueOf(main.getInventoryLoader().getDisableColor()) + main.getInventoryLoader().getDisplayerDisable()));
			inventory[0].setItem(item.getKey(), item.getValue());
		}
	}
}
