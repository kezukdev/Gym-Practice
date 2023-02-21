package bawz.practice.loader;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import bawz.practice.Main;
import bawz.practice.utils.StringUtils;

public class InventoryLoader {
	
	private String previewName;
	public String getPreviewName() { return previewName; }
	private ItemStack playerInfosItem;
	public ItemStack getPlayerInfosItem() { return playerInfosItem; }
	private String playerInfosName;
	public String getPlayerInfosName() { return playerInfosName; }
	private List<String> playerInfosLore;
	public List<String> getPlayerInfosLore() { return playerInfosLore; }
	
	public InventoryLoader(final Main main) {
		this.previewName = StringUtils.translate(main.getConfig().getString("inventory.preview.name"));
		this.playerInfosName = StringUtils.translate(main.getConfig().getString("inventory.preview.items.playerinformation.name"));
		this.playerInfosLore = main.getConfig().getStringList("inventory.preview.items.playerinformation.lore");
		this.playerInfosItem = new ItemStack(Material.valueOf(main.getConfig().getString("inventory.preview.items.playerinformation.material")), 1, (short) main.getConfig().getInt("inventory.preview.player-infos.data"));
		main.getLogger().warning("Inventory > Loaded");
	}

}
