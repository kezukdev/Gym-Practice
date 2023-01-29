package bawz.practice.handler.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import bawz.practice.Main;
import bawz.practice.ladder.Ladder;
import bawz.practice.queue.QueueType;

public class InventoryListener implements Listener {
	
	private final Main main = Main.getInstance();
	
	@EventHandler(priority=EventPriority.LOW)
	public void inventoryInteraction(final InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getClickedInventory().getName().equalsIgnoreCase(this.main.getManagerHandler().getInventoryManager().getQueue()[0].getName()) || event.getClickedInventory().getName().equalsIgnoreCase(this.main.getManagerHandler().getInventoryManager().getQueue()[1].getName())) {
			if (event.getCurrentItem() == null || event.getCurrentItem().equals(new ItemStack(Material.AIR))) return;
			event.getWhoClicked().closeInventory();
			this.main.getManagerHandler().getQueueManager().addPlayerToQueue(Lists.newArrayList(event.getWhoClicked().getUniqueId()), Ladder.getLadderBySlots(event.getSlot()), event.getClickedInventory().getName().equalsIgnoreCase(this.main.getManagerHandler().getInventoryManager().getQueue()[1].getName()) ? QueueType.RANKED : QueueType.CASUAL);
			return;
		}
	}

}
