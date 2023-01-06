package saturne.practice.ladder.sub;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;
import saturne.practice.Main;

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
	}

	public void save() {
		try {
			getConfig().save(getFile()); 
		} 
		catch (IOException e) { e.printStackTrace(); }
	}
}
