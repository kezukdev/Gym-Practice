package saturne.practice;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import saturne.practice.handler.ConfigHandler;
import saturne.practice.handler.ListenerHandler;
import saturne.practice.handler.ManagerHandler;

public class Main extends JavaPlugin {
	
	private static Main instance;
	public static Main getInstance() { return instance; }
	
	@Getter
	private ConfigHandler configHandler;
	@Getter
	private ListenerHandler listenerHandler;
	@Getter
	private ManagerHandler managerHandler;
	
	public void onEnable() {
		instance = this;
		this.configHandler = new ConfigHandler(this);
		this.listenerHandler = new ListenerHandler(this);
		this.managerHandler = new ManagerHandler();
	}
}
