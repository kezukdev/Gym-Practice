package saturne.practice.handler;

import saturne.practice.handler.managers.ArenaManager;
import saturne.practice.handler.managers.InventoryManager;
import saturne.practice.handler.managers.ItemManager;

public class ManagerHandler {
	
	private final ItemManager itemManager;
	private final InventoryManager inventoryManager;
	private final ArenaManager arenaManager;
	
	public ManagerHandler() {
		this.itemManager = new ItemManager();
		this.inventoryManager = new InventoryManager();
		this.arenaManager = new ArenaManager();
	}
	
	public ItemManager getItemManager() {
		return itemManager;
	}
	
	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}
	
	public ArenaManager getArenaManager() {
		return arenaManager;
	}

}
