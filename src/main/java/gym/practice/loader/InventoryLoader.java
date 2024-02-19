package gym.practice.loader;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import gym.practice.Main;
import gym.practice.utils.StringUtils;

public class InventoryLoader {
	
	private String previewName;
	public String getPreviewName() { return previewName; }
	private ItemStack playerInfosItem;
	public ItemStack getPlayerInfosItem() { return playerInfosItem; }
	private String playerInfosName;
	public String getPlayerInfosName() { return playerInfosName; }
	private List<String> playerInfosLore;
	public List<String> getPlayerInfosLore() { return playerInfosLore; }
	private String settingsName;
	public String getSettingsName() { return settingsName; }
	private Integer settingsSize;
	public Integer getSettingsSize() { return settingsSize; }
	private String enableColor;
	public String getEnableColor() { return enableColor; }
	private String disableColor;
	public String getDisableColor() { return disableColor; }
	private String displayerEnable;
	public String getDisplayerEnable() { return displayerEnable; }
	private String displayerDisable;
	public String getDisplayerDisable() { return displayerDisable; }
	
	public InventoryLoader(final Main main) {
		this.previewName = StringUtils.translate(main.getConfig().getString("inventory.preview.name"));
		this.playerInfosName = StringUtils.translate(main.getConfig().getString("inventory.preview.items.playerinformation.name"));
		this.playerInfosLore = main.getConfig().getStringList("inventory.preview.items.playerinformation.lore");
		this.playerInfosItem = new ItemStack(Material.valueOf(main.getConfig().getString("inventory.preview.items.playerinformation.material")), 1, (short) main.getConfig().getInt("inventory.preview.player-infos.data"));
		this.settingsName = StringUtils.translate(main.getConfig().getString("inventory.settings.name"));
		this.settingsSize = main.getConfig().getInt("inventory.settings.size");
		this.enableColor = main.getConfig().getString("color.enable");
		this.disableColor = main.getConfig().getString("color.disable");
		this.displayerEnable = main.getConfig().getString("displayer.enable");
		this.displayerDisable = main.getConfig().getString("displayer.disable");
		main.getLogger().warning("Inventory > Loaded");
	}
	
}
