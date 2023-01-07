package saturne.practice.handler.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
	
	@EventHandler(priority=EventPriority.LOW)
	public void inventoryInteraction(final InventoryClickEvent event) {
		event.setCancelled(true);
	}

}
