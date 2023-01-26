package bawz.practice.handler.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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

import com.google.common.collect.Lists;

import org.bukkit.event.entity.PlayerDeathEvent;

import bawz.practice.Main;
import bawz.practice.ladder.LadderType;
import bawz.practice.match.MatchEntry;
import bawz.practice.match.MatchState;
import bawz.practice.profile.Profile;
import bawz.practice.profile.ProfileState;

public class PlayerListener implements Listener {
	
	private Main main = Main.getInstance();
	
	@EventHandler(priority=EventPriority.HIGH)
	public void PlayerJoin(final PlayerJoinEvent event) {
		event.setJoinMessage(null);
		if (Boolean.valueOf(this.main.getConfig().getString("join-message.enabled"))) {
			String message = this.main.getConfig().getString("join-message.message");
			event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%currentlyOnline%", String.valueOf(Bukkit.getOnlinePlayers().size())).replace("%maxSlots%", String.valueOf(Bukkit.getMaxPlayers())).replace("%player%", event.getPlayer().getDisplayName())));
		}
		event.getPlayer().teleport(main.getSpawnLocation() != null ? main.getSpawnLocation() : event.getPlayer().getWorld().getSpawnLocation());
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
		this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getPlayer().getUniqueId()).exit();
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void PlayerInteract(final PlayerInteractEvent event) {
		final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getPlayer().getUniqueId());
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.getItem().getType() != Material.AIR) {
				event.getPlayer().chat(this.main.getConfig().getString((profile.getProfileState().equals(ProfileState.FREE) ? "spawn" : "queue") + "-items." + String.valueOf(event.getPlayer().getInventory().getHeldItemSlot()) + ".command"));	
			}	
		}
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void PlayerFoodChange(final FoodLevelChangeEvent event) {
		final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getEntity().getUniqueId());
		if (profile.getProfileState().equals(ProfileState.FIGHT)) {
			return;
		}
		event.setCancelled(true);
	}

	@EventHandler(priority=EventPriority.LOW)
	public void PlayerPlaceBlockEvent(final BlockPlaceEvent event) {
		final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getPlayer().getUniqueId());
		final LadderType type = this.main.getMatchs().get(profile.getProfileCache().getMatchID()).getLadder().getLadderType();
		if (profile.getProfileState().equals(ProfileState.FIGHT) && (type.equals(LadderType.BRIDGES) || type.equals(LadderType.UHC))) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void PlayerBreakBlockEvent(final BlockBreakEvent event) {
		final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getPlayer().getUniqueId());
		final LadderType type = this.main.getMatchs().get(profile.getProfileCache().getMatchID()).getLadder().getLadderType();
		if (profile.getProfileState().equals(ProfileState.FIGHT) && (type.equals(LadderType.BRIDGES) || type.equals(LadderType.UHC)|| type.equals(LadderType.SPLEEF))) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void PlayerDropEvent(final PlayerDropItemEvent event) {
		final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getPlayer().getUniqueId());
		if (profile.getProfileState().equals(ProfileState.FIGHT)) {
			return;
		}
		event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void PlayerDeathEvent(final PlayerDeathEvent event) {
		final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getEntity().getUniqueId());
		if (profile.getProfileState().equals(ProfileState.FIGHT) && this.main.getMatchs().get(profile.getProfileCache().getMatchID()).getMatchState().equals(MatchState.PLAYING)) {
			final MatchEntry matchEntry = this.main.getMatchs().get(profile.getProfileCache().getMatchID());
			Integer teamID = matchEntry.getFirstList().contains(event.getEntity().getUniqueId()) ? 0 : 1;
			matchEntry.getAlives().get(teamID).remove(event.getEntity().getUniqueId());
			List<UUID> players = Lists.newArrayList(matchEntry.getFirstList());
			players.addAll(matchEntry.getSecondList());
			for (UUID uuids : players) {
				final Player player = Bukkit.getPlayer(uuids);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.kill-message").replace("%killer%", event.getEntity().getKiller().getName()).replace("%killed%", event.getEntity().getName())));
			}
			if (matchEntry.getAlives().get(teamID).size() == 0) {
				this.main.getManagerHandler().getMatchManager().endMatch(event.getEntity().getKiller().getUniqueId(), profile.getProfileCache().getMatchID());
			}
		}
	}
}
