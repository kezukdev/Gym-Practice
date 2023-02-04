package bawz.practice.board.sub;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import bawz.practice.Main;
import bawz.practice.board.Adaptater;
import lombok.Getter;

public class ScoreboardFile {
	
	@Getter
	private Main main;
	private YamlConfiguration config;
	public YamlConfiguration getConfig() { return config; }
	private File file;
	public File getFile() { return file; }
	private Adaptater adaptater;
	public Adaptater getAdaptater() { return adaptater; }
	
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
			for (String str : getConfig().getConfigurationSection("scoreboard").getKeys(false)) {
				this.adaptater = new Adaptater(this.getConfig().getString("global-name"), str,this.getConfig().getBoolean("queueboard-in-lobbyboard"), this.getConfig().getConfigurationSection(str).getStringList(str));
			}
		}
	}

	public void save() {
		try {
			getConfig().save(getFile()); 
		} 
		catch (IOException e) { e.printStackTrace(); }
	}
}
