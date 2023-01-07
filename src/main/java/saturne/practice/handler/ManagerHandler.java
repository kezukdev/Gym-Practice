package saturne.practice.handler;

import saturne.practice.handler.managers.ArenaManager;
import saturne.practice.handler.managers.InventoryManager;
import saturne.practice.handler.managers.ItemManager;
import saturne.practice.handler.managers.MatchManager;
import saturne.practice.handler.managers.QueueManager;

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
	
	public ManagerHandler() {
		this.itemManager = new ItemManager();
		this.inventoryManager = new InventoryManager();
		this.arenaManager = new ArenaManager();
		this.queueManager = new QueueManager();
		this.matchManager = new MatchManager();
	}
}
