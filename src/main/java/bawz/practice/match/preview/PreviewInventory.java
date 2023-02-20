package bawz.practice.match.preview;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class PreviewInventory {
	
	private Inventory inventory;
	public Inventory getInventory() { return inventory; }

	public PreviewInventory(final UUID uuid) {
		this.inventory = Bukkit.createInventory(null, 54, null);
	}
}
