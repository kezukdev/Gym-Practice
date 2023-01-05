package saturne.practice.handler;

import saturne.practice.handler.managers.ItemManager;

public class ManagerHandler {
	
	private final ItemManager itemManager;
	
	public ManagerHandler() {
		this.itemManager = new ItemManager();
	}
	
	public ItemManager getItemManager() {
		return itemManager;
	}

}
