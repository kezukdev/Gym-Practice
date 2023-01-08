package bawz.practice.handler.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.google.common.collect.Lists;

import bawz.practice.Main;
import bawz.practice.ladder.Ladder;
import bawz.practice.queue.QueueType;
import net.md_5.bungee.api.ChatColor;

public class InventoryListener implements Listener {
	
	private final Main main = Main.getInstance();
	
	@EventHandler(priority=EventPriority.LOW)
	public void inventoryInteraction(final InventoryClickEvent event) {
		event.setCancelled(true);
		final String casualInventoryName = ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("inventory.casual.name"));
		final String rankedInventoryName = ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("inventory.ranked.name"));
		if (event.getClickedInventory().getName().equalsIgnoreCase(casualInventoryName) || event.getClickedInventory().getName().equalsIgnoreCase(rankedInventoryName)) {
			event.getWhoClicked().closeInventory();
			this.main.getManagerHandler().getQueueManager().addPlayerToQueue(Lists.newArrayList(event.getWhoClicked().getUniqueId()), Ladder.getLadderBySlots(event.getSlot()), event.getClickedInventory().getName().equalsIgnoreCase(rankedInventoryName) ? QueueType.RANKED : QueueType.CASUAL);
		}
	}

}
