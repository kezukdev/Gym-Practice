package saturne.practice;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import net.minecraft.util.com.google.common.collect.Maps;
import saturne.practice.arena.Arena;
import saturne.practice.handler.CommandHandler;
import saturne.practice.handler.ListenerHandler;
import saturne.practice.handler.ManagerHandler;
import saturne.practice.ladder.Ladder;
import saturne.practice.ladder.sub.LadderFile;
import saturne.practice.profile.Profile;

public class Main extends JavaPlugin {
	
	private static Main instance;
	public static Main getInstance() { return instance; }
	
	private ListenerHandler listenerHandler;
	public ListenerHandler getListenerHandler() { return listenerHandler; }
	private ManagerHandler managerHandler;
	public ManagerHandler getManagerHandler() { return managerHandler; }
	private CommandHandler commandHandler;
	public CommandHandler getCommandHandler() { return commandHandler; }
	
	private HashMap<UUID, Profile> profiles = Maps.newHashMap();
	public HashMap<UUID, Profile> getProfiles() { return profiles; }
	private List<Arena> arenas = Lists.newArrayList();
	public List<Arena> getArenas() { return arenas; }
	private List<Ladder> ladders = Lists.newArrayList();
	public List<Ladder> getLadders() { return ladders; }
	
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
				System.out.println("[SATURNE-SERVICES] Your product key is wrong. Please contact our support to solve this problem.");
				this.getPluginLoader().disablePlugin(this);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		this.listenerHandler = new ListenerHandler(this);
		this.managerHandler = new ManagerHandler();
		this.commandHandler = new CommandHandler(this);
		if (Bukkit.getOnlinePlayers().size() != 0) {
			for (Player players : Bukkit.getOnlinePlayers()) {
				new Profile(players.getUniqueId());
			}
		}
	}
	
	public void onDisable() {
		if (Bukkit.getOnlinePlayers().size() != 0) {
			this.profiles.clear();
		}
		try {
			this.getLadderFile().getConfig().save(this.getLadderFile().getFile());
			this.managerHandler.getArenaManager().saveArenas();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
