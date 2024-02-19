package gym.practice.handler.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import gym.practice.Main;
import net.minecraft.util.com.google.common.collect.Lists;

public class ItemManager {
	
	private Main main;
	
	private Map<String, Map<Integer, ItemStack>> inventory = new HashMap<>();
	public Map<String, Map<Integer, ItemStack>> getInventory() { return inventory; }
	private Map<String, Map<Integer, String>> commands = new HashMap<>();
	public Map<String, Map<Integer, String>> getCommands() { return commands; }
	
	public ItemManager(final Main main) { this.main = main; }
	
	public void giveItems(final Player player, final String type) {
		player.getInventory().clear();
		for (Entry<Integer, ItemStack> item : inventory.get(type.equals("spawn-items") ? "spawn-items" : "queue-items").entrySet()) {
			player.getInventory().setItem(item.getKey(), item.getValue());
		}
		player.updateInventory();
	}
	
	private ItemStack configToItem(final String type, final String number) {
		ItemStack item = new ItemStack(this.main.getConfig().get(type + "." + number) != null ? Material.valueOf(this.main.getConfig().getString(type + "." + number + ".material")) : Material.AIR, 1,(byte)(this.main.getConfig().get(type + "." + number) != null ? this.main.getConfig().getInt(type + "." + number + ".material-id") : 0));
		if (item.getType() != Material.AIR) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(this.main.getConfig().get(type + "." + number) != null ? ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString(type + "." + number + ".displayname")) : "");
			if (this.main.getConfig().get(type + "." + number) != null && this.main.getConfig().get(type + "." + number + ".lore") != null) {
				List<String> lores = Lists.newArrayList();
				 for (String lore : this.main.getConfig().getStringList(type + "." + number + ".lore")) {
					  lores.add(ChatColor.translateAlternateColorCodes('&', lore.toString()));
				}
				meta.setLore(lores);
			}
			item.setItemMeta(meta);	
		}
		return item;
	}
	
	public void loadItems(final String type) {
		Map<Integer, ItemStack> items = new HashMap<>();
		Map<Integer, String> command = new HashMap<>();
		for (int i = 0; i < (type == "settings" ? main.getInventoryLoader().getSettingsSize() : 9); i++) {
			items.put(i, configToItem(type, String.valueOf(i)));
			command.put(i, this.main.getConfig().getString(type + "." + i + ".command"));
			inventory.put(type, items);
			commands.put(type, command);
		}
	}
}
