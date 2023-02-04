package bawz.practice.handler.managers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import bawz.practice.Main;
import bawz.practice.arena.Arena;
import bawz.practice.arena.ArenaType;
import bawz.practice.utils.LocationSerializer;
import bawz.practice.utils.config.Config;

public class ArenaManager {
	
	private Main main;
	public ArenaManager(final Main main) { this.main = main; }
	
	private HashMap<String, Arena> arenas = new HashMap<>();
	private final Config config = new Config("arenas", (JavaPlugin)this.main);
	
	public ArenaManager() { loadArenas(); }

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
		this.arenas.forEach((arenaName, arena) -> {
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
		this.arenas.clear();
		loadArenas();
	}

    public Arena getRandomArena(ArenaType arenaType) {
        List<Arena> availableArena = this.main.getArenas().stream().filter(arenaManager -> arenaManager.getArenaType() == arenaType).collect(Collectors.toList());
        Collections.shuffle(availableArena);
        return availableArena.get(0);
    }

	public void createArena(String name) {
		this.arenas.put(name, new Arena(name));
	}

	public Arena getArena(String name) {
		return this.arenas.get(name);
	}
}