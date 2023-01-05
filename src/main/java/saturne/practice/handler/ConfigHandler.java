package saturne.practice.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;
import saturne.practice.Main;

public class ConfigHandler {
	
	@Getter
	private File itemsFile;
	@Getter
	private FileConfiguration itemsConfig;
	@Getter
	private ArrayList<File> files;
	@Getter
	private ArrayList<FileConfiguration> filesConfig;
	
	public ConfigHandler(final Main main) {
		main.saveDefaultConfig();
		itemsFile = new File(main.getDataFolder(), "items.yml");
        if (!itemsFile.exists()) {
            itemsFile.getParentFile().mkdirs();
            main.saveResource("items.yml", false);
        }
        Arrays.asList(itemsConfig);
        for (FileConfiguration fileConfig : filesConfig) {
        	fileConfig = new YamlConfiguration();
        }
        Arrays.asList(itemsFile);
        for (File file : files) {
        	YamlConfiguration.loadConfiguration(file);	
        	System.out.println(file.getName() + " has been loaded");
        }
	}

}
