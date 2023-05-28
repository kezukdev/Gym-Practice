package bawz.practice.handler.managers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import bawz.practice.Main;
import bawz.practice.arena.Arena;
import bawz.practice.arena.ArenaType;
import bawz.practice.utils.LocationSerializer;
import bawz.practice.utils.config.Config;

public class ArenaManager {
	
	private Main main;
	private final Config config;
	
	public ArenaManager(final Main main) { 
		this.main = main;
		this.config = new Config("arenas", this.main);
		this.loadArenas();
	}

	private void loadArenas() {
		FileConfiguration fileConfig = this.config.getConfig();
		ConfigurationSection arenaSection = fileConfig.getConfigurationSection("arenas");
		if (arenaSection == null) return;
		arenaSection.getKeys(false).forEach(name -> {
			String first = arenaSection.getString(name + ".first");
			String second = arenaSection.getString(name + ".second");
			ArenaType arenaType = ArenaType.valueOf(arenaSection.getString(name + ".type"));
			LocationSerializer firstLocation = LocationSerializer.stringToLocation(first);
			LocationSerializer secondLocation = LocationSerializer.stringToLocation(second);
			new Arena(name, firstLocation, secondLocation, arenaType);
		});
	}

	public void saveArenas() {
		FileConfiguration fileConfig = this.config.getConfig();
		fileConfig.set("arenas", null);
		this.main.getArenasMap().forEach((arenaName, arena) -> {
			String first = LocationSerializer.locationToString(arena.getLoc1());
			String second = LocationSerializer.locationToString(arena.getLoc2());
			String type = arena.getArenaType().toString();
			String arenaRoot = "arenas." + arenaName;
			fileConfig.set(arenaRoot + ".first", first);
			fileConfig.set(arenaRoot + ".second", second);
			fileConfig.set(arenaRoot + ".type", type);
		});
		this.config.save();
	}

	public void reloadArenas() {
		saveArenas();
		this.main.getArenasMap().clear();
		loadArenas();
	}

    public Arena getRandomArena(ArenaType arenaType) {
        List<Arena> availableArena = this.main.getArenas().stream().filter(arenaManager -> arenaManager.getArenaType() == arenaType).collect(Collectors.toList());
        Collections.shuffle(availableArena);
        return availableArena.get(0);
    }

	public Arena getArena(String name) {
		return this.main.getArenasMap().get(name);
	}
}