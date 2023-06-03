package bawz.practice.ladder.sub;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import bawz.practice.Main;
import bawz.practice.ladder.Ladder;
import bawz.practice.ladder.LadderType;
import bawz.practice.utils.BukkitSerialization;
import lombok.Getter;

public class LadderFile {
	
	@Getter
	private Main main;
	private YamlConfiguration config;
	public YamlConfiguration getConfig() { return config; }
	private File file;
	public File getFile() { return file; }
	
	public LadderFile(final Main main) {
		this.main = main;
		this.generate();
	}

	private void generate() {
		file = new File(this.main.getDataFolder(), "ladders.yml");
		if (!file.exists()) {            
			this.main.saveResource("ladders.yml", false);
		}
		config = YamlConfiguration.loadConfiguration(file);
		if (config.getKeys(true).size() > 2) {
			for (String str : getConfig().getConfigurationSection("ladders").getKeys(false)) {
				try {
					new Ladder(str, new ItemStack(Material.valueOf(getConfig().getConfigurationSection("ladders").getString(str + ".icon")), 1, (byte) getConfig().getConfigurationSection("ladders").getInt(str + ".icon-id")), BukkitSerialization.itemStackArrayFromBase64(getConfig().getConfigurationSection("ladders").getString(str + ".content")), BukkitSerialization.itemStackArrayFromBase64(getConfig().getConfigurationSection("ladders").getString(str + ".armorContent")), getConfig().getConfigurationSection("ladders").getString(str + ".displayname"), LadderType.valueOf(getConfig().getConfigurationSection("ladders").getString(str + ".type")), Integer.valueOf(getConfig().getConfigurationSection("ladders").getInt(str + ".slots")), Boolean.valueOf(getConfig().getConfigurationSection("ladders").getString(str + ".editable")), Boolean.valueOf(getConfig().getConfigurationSection("ladders").getString("ranked")), Boolean.valueOf(getConfig().getConfigurationSection("ladders").getBoolean("cooldownPearl")), Boolean.valueOf(getConfig().getConfigurationSection("ladders").getString("knockbackProfile")), getConfig().getConfigurationSection("ladders").getString("knockbackTypeProfile"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}
		System.out.println("[OGPOTS] Ladders > Loaded");
	}

	public void save() {
		try {
			getConfig().save(getFile()); 
		} 
		catch (IOException e) { e.printStackTrace(); }
	}
}
