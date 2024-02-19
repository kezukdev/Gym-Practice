package gym.practice.settings;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import gym.practice.Main;

public class Settings {
	
	private Inventory[] inventory = new Inventory[1];
	public Inventory[] getInventory() { return inventory; }
	private Main main;
	
	public Settings(final Main main, final UUID uuid) {
		this.main = main;
		this.inventory[0] = Bukkit.createInventory(null, main.getInventoryLoader().getSettingsSize(), main.getInventoryLoader().getSettingsName());
		this.loadSettingsInventory(uuid);
	}

	public void loadSettingsInventory(final UUID uuid) {
		for (Entry<Integer, ItemStack> item : main.getManagerHandler().getItemManager().getInventory().get("inventory.settings.items").entrySet()) {
			if (item.getValue().getType().equals(Material.AIR)) return;
			item.getValue().getItemMeta().setDisplayName(item.getValue().getItemMeta().getDisplayName().replace("%sbStatus%", this.main.getManagerHandler().getProfileManager().getProfiles().get(uuid).getProfileData().isScoreboard() ? ChatColor.valueOf(main.getInventoryLoader().getEnableColor()) + main.getInventoryLoader().getDisplayerEnable() : ChatColor.valueOf(main.getInventoryLoader().getDisableColor()) + main.getInventoryLoader().getDisplayerDisable()));
			inventory[0].setItem(item.getKey(), item.getValue());
		}
	}
}
