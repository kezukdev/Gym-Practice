package gym.practice.match.preview;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import gym.practice.Main;

public class PreviewInventory {
	
	private Inventory inventory;
	public Inventory getInventory() { return inventory; }

	public PreviewInventory(final UUID uuid, final Main main) {
		this.inventory = Bukkit.createInventory(null, 54, main.getInventoryLoader().getPreviewName().replace("%playerName%", Bukkit.getPlayer(uuid) != null ? Bukkit.getPlayer(uuid).getName() : Bukkit.getOfflinePlayer(uuid).getName()));
		this.inventory.setContents(Bukkit.getPlayer(uuid) != null ? Bukkit.getPlayer(uuid).getInventory().getContents() : Bukkit.getOfflinePlayer(uuid).getPlayer().getInventory().getContents());
		this.inventory.setItem(36, Bukkit.getPlayer(uuid) != null ? Bukkit.getPlayer(uuid).getInventory().getArmorContents()[3] : Bukkit.getOfflinePlayer(uuid).getPlayer().getInventory().getArmorContents()[3]);
		this.inventory.setItem(37, Bukkit.getPlayer(uuid) != null ? Bukkit.getPlayer(uuid).getInventory().getArmorContents()[2] : Bukkit.getOfflinePlayer(uuid).getPlayer().getInventory().getArmorContents()[2]);
		this.inventory.setItem(38, Bukkit.getPlayer(uuid) != null ? Bukkit.getPlayer(uuid).getInventory().getArmorContents()[1] : Bukkit.getOfflinePlayer(uuid).getPlayer().getInventory().getArmorContents()[1]);
		this.inventory.setItem(39, Bukkit.getPlayer(uuid) != null ? Bukkit.getPlayer(uuid).getInventory().getArmorContents()[0] : Bukkit.getOfflinePlayer(uuid).getPlayer().getInventory().getArmorContents()[0]);
		final ItemStack playerInfos = main.getInventoryLoader().getPlayerInfosItem();
		final ItemMeta playerInfosMeta = playerInfos.getItemMeta();
		playerInfosMeta.setDisplayName(main.getInventoryLoader().getPlayerInfosName());
		playerInfosMeta.setLore(main.getInventoryLoader().getPlayerInfosLore());
		playerInfos.setItemMeta(playerInfosMeta);
		this.inventory.setItem(48, playerInfos);
	}
}
