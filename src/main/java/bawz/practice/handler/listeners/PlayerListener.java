package bawz.practice.handler.listeners;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
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
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import org.bukkit.event.entity.PlayerDeathEvent;

import bawz.practice.Main;
import bawz.practice.ladder.LadderType;
import bawz.practice.match.MatchEntry;
import bawz.practice.match.MatchState;
import bawz.practice.match.sub.MatchStatistics;
import bawz.practice.profile.Profile;
import bawz.practice.profile.ProfileState;
import bawz.practice.utils.StringUtils;

public class PlayerListener implements Listener {
	
	private Main main;
	
	public PlayerListener(final Main main) { this.main = main; }
	
	@EventHandler(priority=EventPriority.HIGH)
	public void PlayerJoin(final PlayerJoinEvent event) {
		event.setJoinMessage(null);
		event.getPlayer().getInventory().setArmorContents(null);
		if (this.main.getMessageLoader().isJoinMessageEnabled()) {
			String message = this.main.getMessageLoader().getJoinMessage();
			event.setJoinMessage(message.replace("%currentlyOnline%", String.valueOf(Bukkit.getOnlinePlayers().size())).replace("%maxSlots%", String.valueOf(Bukkit.getMaxPlayers())).replace("%player%", event.getPlayer().getDisplayName()));
		}
		event.getPlayer().teleport(main.getSpawnLocation() != null ? main.getSpawnLocation() : event.getPlayer().getWorld().getSpawnLocation());
		new Profile(event.getPlayer().getUniqueId());
		event.getPlayer().setFoodLevel(20);
		event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
		for (PotionEffect effect : event.getPlayer().getActivePotionEffects()) {
			event.getPlayer().removePotionEffect(effect.getType());
		}
		this.main.getManagerHandler().getItemManager().giveItems(event.getPlayer(), "spawn-items");
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void PlayerLeave(final PlayerQuitEvent event) {
		event.setQuitMessage(null);
		if (this.main.getMessageLoader().isLeaveMessageEnabled()) {
			String message = this.main.getMessageLoader().getLeaveMessage();
			event.setQuitMessage(message.replace("%currentlyOnline%", String.valueOf(Bukkit.getOnlinePlayers().size())).replace("%maxSlots%", String.valueOf(Bukkit.getMaxPlayers())).replace("%player%", event.getPlayer().getDisplayName()));
		}
		this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getPlayer().getUniqueId()).exit();
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void PlayerInteract(final PlayerInteractEvent event) {
		final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getPlayer().getUniqueId());
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.getItem().getType() != Material.AIR && (profile.getProfileState().equals(ProfileState.FREE) || profile.getProfileState().equals(ProfileState.QUEUE))) {
				event.getPlayer().chat(this.main.getManagerHandler().getItemManager().getCommands().get(profile.getProfileState().equals(ProfileState.FREE) ? "spawn-items" : "queue-items").get(event.getPlayer().getInventory().getHeldItemSlot()));	
			}	
			if (profile.getProfileState().equals(ProfileState.FIGHT)) {
				final ItemStack item = event.getItem();
				final MatchEntry matchEntry = this.main.getManagerHandler().getMatchManager().getMatchs().get(profile.getProfileCache().getMatchID());
				if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && item.getType() == Material.ENDER_PEARL && event.getPlayer().getGameMode() != GameMode.CREATIVE && matchEntry.getLadder().isCooldownPearl()) {
					if (matchEntry.getMatchState() != MatchState.PLAYING) {
						event.setUseItemInHand(Result.DENY);
						event.getPlayer().sendMessage(StringUtils.translate(null));
						event.getPlayer().updateInventory();
						return;
					}
					MatchStatistics matchStats = matchEntry.getMatchStatistics().get(event.getPlayer().getUniqueId());
					if (!matchStats.isEnderPearlCooldownActive()) {
						matchStats.applyEnderPearlCooldown();
						return;
					}
					event.setUseItemInHand(Result.DENY);
					final double time = matchStats.getEnderPearlCooldown() / 1000.0D;
					final DecimalFormat df = new DecimalFormat("#.#");
					event.getPlayer().sendMessage(this.main.getMessageLoader().getCooldownEnderpearl().replace("%cooldown%", df.format(time)));
					event.getPlayer().updateInventory();
					return;
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onTeleport(PlayerTeleportEvent event) {
		if (event.getCause() == TeleportCause.ENDER_PEARL) {
			final Player player = event.getPlayer();
			final Profile pm = this.main.getManagerHandler().getProfileManager().getProfiles().get(player.getUniqueId());
			if (pm.getProfileState() != ProfileState.FIGHT && (this.main.getManagerHandler().getMatchManager().getMatchs().get(pm.getProfileCache().getMatchID()) != null && this.main.getManagerHandler().getMatchManager().getMatchs().get(pm.getProfileCache().getMatchID()).getMatchState() != MatchState.PLAYING)) event.setCancelled(true);
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
		if (this.main.getManagerHandler().getMatchManager().getMatchs().get(profile.getProfileCache().getMatchID()) != null) {
			final LadderType type = this.main.getManagerHandler().getMatchManager().getMatchs().get(profile.getProfileCache().getMatchID()).getLadder().getLadderType();
			if (profile.getProfileState().equals(ProfileState.FIGHT) && (type.equals(LadderType.BRIDGES) || type.equals(LadderType.UHC))) {
				return;
			}	
		}
		event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void PlayerBreakBlockEvent(final BlockBreakEvent event) {
		final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getPlayer().getUniqueId());
		if (this.main.getManagerHandler().getMatchManager().getMatchs().get(profile.getProfileCache().getMatchID()) != null) {
			final LadderType type = this.main.getManagerHandler().getMatchManager().getMatchs().get(profile.getProfileCache().getMatchID()).getLadder().getLadderType();
			if (profile.getProfileState().equals(ProfileState.FIGHT) && (type.equals(LadderType.BRIDGES) || type.equals(LadderType.UHC)|| type.equals(LadderType.SPLEEF))) {
				return;
			}	
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
		event.setDeathMessage(null);
		event.getDrops().clear();
		final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getEntity().getUniqueId());
		if (profile.getProfileState().equals(ProfileState.FIGHT) && this.main.getManagerHandler().getMatchManager().getMatchs().get(profile.getProfileCache().getMatchID()).getMatchState().equals(MatchState.PLAYING)) {
            new BukkitRunnable() {
                public void run() {
                    try {
                        final Object nmsPlayer = event.getEntity().getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(event.getEntity(), new Object[0]);
                        final Object con = nmsPlayer.getClass().getDeclaredField("playerConnection").get(nmsPlayer);
                        final Class<?> EntityPlayer = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".EntityPlayer");
                        final Field minecraftServer = con.getClass().getDeclaredField("minecraftServer");
                        minecraftServer.setAccessible(true);
                        final Object mcserver = minecraftServer.get(con);
                        final Object playerlist = mcserver.getClass().getDeclaredMethod("getPlayerList", (Class<?>[])new Class[0]).invoke(mcserver, new Object[0]);
                        final Method moveToWorld = playerlist.getClass().getMethod("moveToWorld", EntityPlayer, Integer.TYPE, Boolean.TYPE);
                        moveToWorld.invoke(playerlist, nmsPlayer, 0, false);
                    }
                    catch (Exception ex) {
                    	event.getEntity().spigot().respawn();
                        ex.printStackTrace();
                    }
                }
            }.runTaskLater(this.main, 2L);
			final MatchEntry matchEntry = this.main.getManagerHandler().getMatchManager().getMatchs().get(profile.getProfileCache().getMatchID());
			Integer teamID = matchEntry.getPlayersList().get(0).contains(event.getEntity().getUniqueId()) ? 0 : 1;
			matchEntry.getAlives().get(teamID).remove(event.getEntity().getUniqueId());
			List<UUID> players = Lists.newArrayList(matchEntry.getPlayersList().get(0));
			players.addAll(matchEntry.getPlayersList().get(1));
			for (UUID uuids : players) {
				final Player player = Bukkit.getPlayer(uuids);
				player.sendMessage(this.main.getMessageLoader().getKillMessage().replace("%killer%", event.getEntity().getKiller().getName()).replace("%killed%", event.getEntity().getName()));
			}
			if (matchEntry.getAlives().get(teamID).size() == 0) {
				this.main.getManagerHandler().getMatchManager().endMatch(event.getEntity().getKiller().getUniqueId(), profile.getProfileCache().getMatchID());
			}
		}
	}
}
