package saturne.practice;

import org.bukkit.plugin.java.JavaPlugin;

import saturne.practice.handler.ListenerHandler;
import saturne.practice.handler.ManagerHandler;

public class Main extends JavaPlugin {
	
	private static Main instance;
	public static Main getInstance() { return instance; }
	
	private ListenerHandler listenerHandler;
	private ManagerHandler managerHandler;
	
	public void onEnable() {
		instance = this;
		this.saveDefaultConfig();
		this.listenerHandler = new ListenerHandler(this);
		this.managerHandler = new ManagerHandler();
	}
	
	public ListenerHandler getListenerHandler() {
		return listenerHandler;
	}
	
	public ManagerHandler getManagerHandler() {
		return managerHandler;
	}
}
