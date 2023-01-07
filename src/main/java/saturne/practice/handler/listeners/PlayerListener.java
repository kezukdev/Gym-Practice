package saturne.practice.handler.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import saturne.practice.Main;
import saturne.practice.profile.Profile;
import saturne.practice.profile.ProfileState;

public class PlayerListener implements Listener {
	
	private Main main = Main.getInstance();
	
	@EventHandler(priority=EventPriority.HIGH)
	public void PlayerJoin(final PlayerJoinEvent event) {
		event.setJoinMessage(null);
		if (Boolean.valueOf(this.main.getConfig().getString("join-message.enabled"))) {
			String message = this.main.getConfig().getString("join-message.message");
			event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%currentlyOnline%", String.valueOf(Bukkit.getOnlinePlayers().size())).replace("%maxSlots%", String.valueOf(Bukkit.getMaxPlayers())).replace("%player%", event.getPlayer().getDisplayName())));
		}
		new Profile(event.getPlayer().getUniqueId());
		event.getPlayer().setFoodLevel(20);
		event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
		this.main.getManagerHandler().getItemManager().giveItems(event.getPlayer(), "spawn-items");
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void PlayerLeave(final PlayerQuitEvent event) {
		event.setQuitMessage(null);
		if (Boolean.valueOf(this.main.getConfig().getString("leave-message.enabled"))) {
			String message = this.main.getConfig().getString("leave-message.message");
			event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%currentlyOnline%", String.valueOf(Bukkit.getOnlinePlayers().size())).replace("%maxSlots%", String.valueOf(Bukkit.getMaxPlayers())).replace("%player%", event.getPlayer().getDisplayName())));
		}
		this.main.getProfiles().remove(event.getPlayer().getUniqueId());
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void PlayerInteract(final PlayerInteractEvent event) {
		final Profile profile = this.main.getProfiles().get(event.getPlayer().getUniqueId());
		if (profile.getProfileState().equals(ProfileState.FREE)) {
			if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if (event.getItem().getType() != Material.AIR) {
					event.getPlayer().chat(this.main.getConfig().getString("spawn-items." + String.valueOf(event.getPlayer().getInventory().getHeldItemSlot()) + ".command"));	
				}	
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void PlayerFoodChange(final FoodLevelChangeEvent event) {
		final Profile profile = this.main.getProfiles().get(event.getEntity().getUniqueId());
		if (profile.getProfileState().equals(ProfileState.FIGHT)) {
			return;
		}
		event.setCancelled(true);
	}

	@EventHandler(priority=EventPriority.LOW)
	public void PlayerPlaceBlockEvent(final BlockPlaceEvent event) {
		final Profile profile = this.main.getProfiles().get(event.getPlayer().getUniqueId());
		if (profile.getProfileState().equals(ProfileState.FIGHT)) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void PlayerBreakBlockEvent(final BlockBreakEvent event) {
		final Profile profile = this.main.getProfiles().get(event.getPlayer().getUniqueId());
		if (profile.getProfileState().equals(ProfileState.FIGHT)) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void PlayerDropEvent(final PlayerDropItemEvent event) {
		final Profile profile = this.main.getProfiles().get(event.getPlayer().getUniqueId());
		if (profile.getProfileState().equals(ProfileState.FIGHT)) {
			return;
		}
		event.setCancelled(true);
	}
}
