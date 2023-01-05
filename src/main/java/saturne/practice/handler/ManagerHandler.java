package saturne.practice.handler;

import saturne.practice.handler.managers.InventoryManager;
import saturne.practice.handler.managers.ItemManager;

public class ManagerHandler {
	
	private final ItemManager itemManager;
	private final InventoryManager inventoryManager;
	
	public ManagerHandler() {
		this.itemManager = new ItemManager();
		this.inventoryManager = new InventoryManager();
	}
	
	public ItemManager getItemManager() {
		return itemManager;
	}
	
	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}

}
