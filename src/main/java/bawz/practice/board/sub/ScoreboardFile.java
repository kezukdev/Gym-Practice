package bawz.practice.board.sub;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;

import bawz.practice.Main;
import lombok.Getter;

public class ScoreboardFile {
	
	@Getter
	private Main main;
	private YamlConfiguration config;
	public YamlConfiguration getConfig() { return config; }
	private File file;
	public File getFile() { return file; }
	private List<List<String>> adaptaters = Lists.newArrayList();
	public List<List<String>> getAdaptaters() { return adaptaters; }
	private String name;
	public String getName() { return name; }
	private boolean queueInLobby;
	public boolean isQueueInLobby() { return queueInLobby; }
	
	public ScoreboardFile(final Main main) {
		this.main = main;
		this.generate();
	}

	private void generate() {
		file = new File(this.main.getDataFolder(), "scoreboard.yml");
		if (!file.exists()) {            
			this.main.saveResource("scoreboard.yml", false);
		}
		config = YamlConfiguration.loadConfiguration(file);
		if (config.getKeys(true).size() > 2) {
			this.name = this.getConfig().getString("configuration.global-name");
			this.queueInLobby = Boolean.valueOf(this.getConfig().getString("configuration.queueboard-in-lobbyboard"));
			for (String str : getConfig().getConfigurationSection("scoreboard").getKeys(false)) {
				this.adaptaters.add(getConfig().getConfigurationSection("scoreboard").getStringList(str));
			}
		}
		System.out.println(adaptaters.size());
		System.out.println("[GYM] Scoreboards > Loaded");
	}

	public void save() {
		try {
			getConfig().save(getFile()); 
		} 
		catch (IOException e) { e.printStackTrace(); }
	}
}
