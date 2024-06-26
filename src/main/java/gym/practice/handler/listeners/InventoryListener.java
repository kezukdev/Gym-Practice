package gym.practice.handler.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import com.google.common.collect.Lists;

import gym.practice.Main;
import gym.practice.ladder.Ladder;
import gym.practice.profile.ProfileState;
import gym.practice.queue.QueueType;

public class InventoryListener implements Listener {
	
	private Main main;
	
	public InventoryListener(final Main main) { this.main = main; }
	
	@EventHandler(priority=EventPriority.LOW)
	public void inventoryInteraction(final InventoryClickEvent event) {
		if (event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) return;
		if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
		if (this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getWhoClicked().getUniqueId()).getProfileState().equals(ProfileState.FIGHT)) return;
		event.setCancelled(true);
		if (event.getClickedInventory().getName().equalsIgnoreCase(this.main.getManagerHandler().getInventoryManager().getQueue()[0].getName()) || event.getClickedInventory().getName().equalsIgnoreCase(this.main.getManagerHandler().getInventoryManager().getQueue()[1].getName())) {
			event.getWhoClicked().closeInventory();
			this.main.getManagerHandler().getQueueManager().addPlayerToQueue(Lists.newArrayList(event.getWhoClicked().getUniqueId()), Ladder.getLadderBySlots(event.getSlot()), event.getClickedInventory().getName().equalsIgnoreCase(this.main.getManagerHandler().getInventoryManager().getQueue()[1].getName()) ? QueueType.Ranked : QueueType.Casual);
			return;
		}
		if (event.getClickedInventory().getName().equalsIgnoreCase(this.main.getManagerHandler().getInventoryManager().getMerge()[0].getName())) {
			if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
			if (event.getCurrentItem().getType() != Material.AIR) {
				final Player player = (Player) event.getWhoClicked();
				player.chat(this.main.getManagerHandler().getItemManager().getCommands().get("inventory.merge.items").get(event.getSlot()));	
			}
		}
	}
	
	

}
