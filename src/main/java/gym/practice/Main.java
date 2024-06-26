package gym.practice;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.bizarrealex.aether.Aether;
import com.google.common.collect.Lists;

import gym.practice.arena.Arena;
import gym.practice.board.PracticeBoard;
import gym.practice.board.sub.ScoreboardFile;
import gym.practice.handler.CommandHandler;
import gym.practice.handler.ManagerHandler;
import gym.practice.handler.listeners.EntityListener;
import gym.practice.handler.listeners.InventoryListener;
import gym.practice.handler.listeners.PlayerListener;
import gym.practice.handler.listeners.ServerListener;
import gym.practice.ladder.Ladder;
import gym.practice.ladder.sub.LadderFile;
import gym.practice.loader.InventoryLoader;
import gym.practice.loader.StringLoader;
import gym.practice.profile.Profile;
import gym.practice.utils.LocationSerializer;
import gym.practice.utils.hook.SpigotHook;

public class Main extends JavaPlugin {
	
	private static Main instance;
	public static Main getInstance() { return instance; }
	
	private ManagerHandler managerHandler;
	public ManagerHandler getManagerHandler() { return managerHandler; }
	private CommandHandler commandHandler;
	public CommandHandler getCommandHandler() { return commandHandler; }
	
	private List<Arena> arenas;
	public List<Arena> getArenas() { return arenas; }
	private HashMap<String, Arena> arenasMap;
	public HashMap<String, Arena> getArenasMap() { return arenasMap; }
	private List<Ladder> ladders;
	public List<Ladder> getLadders() { return ladders; }
	
	private Location spawnLocation;
	public Location getSpawnLocation() { return spawnLocation; }
	private Location editorLocation;
	public Location getEditorLocation() { return editorLocation; }
	private Integer elosDefault;
	public Integer getElosDefault() { return elosDefault; }
	
	
	private LadderFile ladderFile;
	public LadderFile getLadderFile() { return ladderFile; }
	private ScoreboardFile scoreboardFile;
	public ScoreboardFile getScoreboardFile() { return scoreboardFile; }
	private StringLoader messageLoader;
	public StringLoader getMessageLoader() { return messageLoader; }
	private InventoryLoader inventoryLoader;
	public InventoryLoader getInventoryLoader() { return inventoryLoader; }
	
	private boolean checked;
	public boolean isChecked() { return checked; }
	
	private SpigotHook spigotHook;
	public SpigotHook getSpigotHook() { return spigotHook; }
	private boolean scoreboardEnable;
	public boolean isScoreboardEnable() { return scoreboardEnable; }
	
	public Main() {
		this.arenas = Lists.newArrayList();
		this.arenasMap = new HashMap<>();
		this.ladders = Lists.newArrayList();
	}
	
	public void onEnable() {
		instance = this;
		this.saveDefaultConfig();
		this.messageLoader = new StringLoader(this);
		this.inventoryLoader = new InventoryLoader(this);
		this.elosDefault = this.getConfig().getInt("default-elos");
		this.ladderFile = new LadderFile(this);
		this.scoreboardFile = new ScoreboardFile(this);
        this.loadLocations();
        for (Listener listener : Arrays.asList(new EntityListener(this), new InventoryListener(this), new ServerListener(this), new PlayerListener(this))) {
        	this.getServer().getPluginManager().registerEvents(listener, this);
        }
		this.managerHandler = new ManagerHandler(this);
		this.commandHandler = new CommandHandler(this);
		this.scoreboardEnable = Boolean.valueOf(this.scoreboardFile.getConfig().getString("configuration.enabled"));
		if (scoreboardEnable) 	new Aether(this, new PracticeBoard(this));
		if (Bukkit.getOnlinePlayers().size() != 0) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				new Profile(players.getUniqueId());
			}
		}
		this.spigotHook = new SpigotHook();
		if (!this.getConfig().getString("licence").equals("rimk")) {
			try {
	            URL url = new URL("http://bawz.eu/" + this.getConfig().getString("licence") + ".html");
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				if (connection.getResponseCode() != 200) {
					System.out.println("[GYM] Licence key is wrong, Need help? Contact Theo on discord.");
					this.checked = false;
					this.getPluginLoader().disablePlugin(this);
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}	
		}
        this.checked = true;	
	}
	
	private void loadLocations() {
		this.spawnLocation = Bukkit.getWorld("world").getSpawnLocation();
		if (getConfig().getString("locations.spawn") != null) {
			this.spawnLocation = LocationSerializer.stringToLocation(getConfig().getString("locations.spawn")).toBukkitLocation();
		}
		if (getConfig().getString("editor") != null) {
			this.editorLocation = LocationSerializer.stringToLocation(getConfig().getString("editor")).toBukkitLocation();
		}
	}

	public void onDisable() {
		if (this.checked) {
			this.ladders.clear();
			this.managerHandler.getItemManager().getInventory().clear();
			this.managerHandler.getItemManager().getCommands().clear();
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
					configFile.createSection("scoreboard");
					Integer[] elos = this.getManagerHandler().getProfileManager().getProfileData().get(uuid).getElos();
					List<Integer> eloList = Arrays.asList(elos);
					configFile.set("elos", eloList);
					configFile.set("scoreboard", String.valueOf(this.getManagerHandler().getProfileManager().getProfileData().get(uuid).isScoreboard()));
					try {
						configFile.save(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}	
				System.out.println("[GYM] Data Player > Saved!");
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
}
