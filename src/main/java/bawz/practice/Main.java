package bawz.practice;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import bawz.practice.arena.Arena;
import bawz.practice.handler.CommandHandler;
import bawz.practice.handler.ListenerHandler;
import bawz.practice.handler.ManagerHandler;
import bawz.practice.ladder.Ladder;
import bawz.practice.ladder.sub.LadderFile;
import bawz.practice.match.MatchEntry;
import bawz.practice.profile.Profile;
import bawz.practice.queue.QueueEntry;
import bawz.practice.utils.LocationSerializer;
import net.minecraft.util.com.google.common.collect.Maps;

public class Main extends JavaPlugin {
	
	private static Main instance;
	public static Main getInstance() { return instance; }
	
	private ListenerHandler listenerHandler;
	public ListenerHandler getListenerHandler() { return listenerHandler; }
	private ManagerHandler managerHandler;
	public ManagerHandler getManagerHandler() { return managerHandler; }
	private CommandHandler commandHandler;
	public CommandHandler getCommandHandler() { return commandHandler; }
	
	private List<Arena> arenas = Lists.newArrayList();
	public List<Arena> getArenas() { return arenas; }
	private List<Ladder> ladders = Lists.newArrayList();
	public List<Ladder> getLadders() { return ladders; }
	private ConcurrentMap<UUID, QueueEntry> queues = Maps.newConcurrentMap();
	public ConcurrentMap<UUID, QueueEntry> getQueues() { return queues; }
	private Map<UUID, MatchEntry> matchs = Maps.newHashMap();
	public Map<UUID, MatchEntry> getMatchs() { return matchs; }
	private Location spawnLocation;
	public Location getSpawnLocation() { return spawnLocation; }
	private Location editorLocation;
	public Location getEditorLocation() { return editorLocation; }
	
	private LadderFile ladderFile;
	public LadderFile getLadderFile() { return ladderFile; }
	
	public void onEnable() {
		instance = this;
		this.saveDefaultConfig();
		this.ladderFile = new LadderFile(this);
        try {
            URL url = new URL("http://bawz.eu/" + this.getConfig().getString("licence"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			if (connection.getResponseCode() != 200) {
				System.out.println("[BAWZ-SERVICES] Your product key is wrong. Please contact our support to solve this problem.");
				this.getPluginLoader().disablePlugin(this);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.loadLocations();
		this.listenerHandler = new ListenerHandler(this);
		this.managerHandler = new ManagerHandler();
		this.commandHandler = new CommandHandler(this);
		if (Bukkit.getOnlinePlayers().size() != 0) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				new Profile(players.getUniqueId());
			}
		}
	}
	
	private void loadLocations() {
		this.spawnLocation = getConfig().getString("locations.spawn") != null ? LocationSerializer.stringToLocation(getConfig().getString("locations.spawn")).toBukkitLocation() : Bukkit.getWorld("world").getSpawnLocation();
		if (getConfig().getString("editor") != null) {
			this.editorLocation = LocationSerializer.stringToLocation(getConfig().getString("editor")).toBukkitLocation();
		}
	}

	public void onDisable() {
		if (this.managerHandler.getProfileManager().getProfileData().size() != 0) {
			for (UUID uuid : this.managerHandler.getProfileManager().getProfileData().keySet()) {
				File file = new File(getDataFolder() + "/players/" + uuid.toString() + ".yml");
				if (!file.exists()) {
					try {
		                file.getParentFile().mkdirs();
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				YamlConfiguration configFile = YamlConfiguration.loadConfiguration(file);
				configFile.createSection("elos");
				Integer[] elos = this.getManagerHandler().getProfileManager().getProfileData().get(uuid).getElos();
				List<Integer> eloList = Arrays.asList(elos);
				configFile.set("elos", eloList);
				try {
					configFile.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
			System.out.println("Data Player > Saved!");
		}
		if (Bukkit.getOnlinePlayers().size() != 0) {
			this.managerHandler.getProfileManager().getProfiles().clear();
		}
		try {
			this.getLadderFile().getConfig().save(this.getLadderFile().getFile());
			this.managerHandler.getArenaManager().saveArenas();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
