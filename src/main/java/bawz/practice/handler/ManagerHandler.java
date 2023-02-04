package bawz.practice.handler;

import bawz.practice.Main;
import bawz.practice.handler.managers.ArenaManager;
import bawz.practice.handler.managers.InventoryManager;
import bawz.practice.handler.managers.ItemManager;
import bawz.practice.handler.managers.MatchManager;
import bawz.practice.handler.managers.ProfileManager;
import bawz.practice.handler.managers.QueueManager;

public class ManagerHandler {
	
	private final ItemManager itemManager;
	public ItemManager getItemManager() { return itemManager; }
	private final InventoryManager inventoryManager;
	public InventoryManager getInventoryManager() { return inventoryManager; }
	private final ArenaManager arenaManager;
	public ArenaManager getArenaManager() { return arenaManager; }
	private final QueueManager queueManager;
	public QueueManager getQueueManager() { return queueManager; }
	private final MatchManager matchManager;
	public MatchManager getMatchManager() { return matchManager; }
	private final ProfileManager profileManager;
	public ProfileManager getProfileManager() { return profileManager; }
	
	public ManagerHandler(final Main main) {
		this.itemManager = new ItemManager(main);
		this.itemManager.loadItems("spawn-items");
		this.itemManager.loadItems("queue-items");
		this.inventoryManager = new InventoryManager(main);
		this.arenaManager = new ArenaManager(main);
		this.queueManager = new QueueManager(main);
		this.matchManager = new MatchManager(main);
		this.profileManager = new ProfileManager();
	}
}
