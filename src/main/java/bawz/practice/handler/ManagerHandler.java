package bawz.practice.handler;

import bawz.practice.handler.managers.ArenaManager;
import bawz.practice.handler.managers.InventoryManager;
import bawz.practice.handler.managers.ItemManager;
import bawz.practice.handler.managers.MatchManager;
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
	
	public ManagerHandler() {
		this.itemManager = new ItemManager();
		this.inventoryManager = new InventoryManager();
		this.arenaManager = new ArenaManager();
		this.queueManager = new QueueManager();
		this.matchManager = new MatchManager();
	}
}