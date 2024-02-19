package gym.practice.handler.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import gym.practice.Main;
import gym.practice.arena.Arena;
import gym.practice.ladder.Ladder;
import gym.practice.match.MatchEntry;
import gym.practice.match.MatchState;
import gym.practice.profile.Profile;
import gym.practice.profile.ProfileState;
import gym.practice.runnable.CountdownRunnable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class MatchManager {
	
	private Main main;
	
	public MatchManager(final Main main) { this.main = main; }
	
	private Map<UUID, MatchEntry> matchs = new HashMap<>();
	public Map<UUID, MatchEntry> getMatchs() { return matchs; }
	
	public void startMatch(final List<UUID> firstList, final List<UUID> secondList, final UUID matchID) {
		final List<UUID> players = Lists.newArrayList(firstList);
		players.addAll(secondList);
		final MatchEntry matchEntry = this.getMatchs().get(matchID);
		final Ladder ladder = matchEntry.getLadder();
		final Arena arena = this.main.getManagerHandler().getArenaManager().getRandomArena(ladder.getLadderType().getArenaType());
		matchEntry.setMatchState(MatchState.STARTING);
		players.forEach(uuid -> {
			this.hidingPlayers(uuid, matchID);
			final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(uuid);
			profile.getProfileCache().setMatchID(matchID);
			final Player player = Bukkit.getPlayer(uuid);
			player.sendMessage(this.main.getMessageLoader().getMatchFound().replace("%opponent%", Bukkit.getPlayer(this.getOpponent(uuid)).getName()));
			player.teleport(firstList.contains(uuid) ? arena.getLoc1().toBukkitLocation() : arena.getLoc2().toBukkitLocation());
			profile.setProfileState(ProfileState.FIGHT);
			player.getInventory().setArmorContents(ladder.getArmorContent());
			player.getInventory().setContents(ladder.getContent());
			this.main.getSpigotHook().applyKitKnockback(player, ladder);
		});
		new CountdownRunnable(matchEntry, players, main).runTaskTimerAsynchronously(main, 20L, 20L);
	}
	
	public void endMatch(final UUID winner, final UUID matchID, final boolean disconnecting) {
		final MatchEntry matchEntry = this.getMatchs().get(matchID);
		matchEntry.setMatchState(MatchState.ENDING);
		final List<UUID> players = Lists.newArrayList(matchEntry.getPlayersList().get(0));
		players.addAll(matchEntry.getPlayersList().get(1));
		TextComponent inventoriesMsg = new TextComponent(this.main.getMessageLoader().getInventoriesMessage().replace("%winner%", Bukkit.getPlayer(winner) != null ? Bukkit.getPlayer(winner).getName() : Bukkit.getOfflinePlayer(winner).getName()).replace("%loser%", Bukkit.getPlayer(this.getOpponent(winner)) != null ? Bukkit.getPlayer(this.getOpponent(winner)).getName() : Bukkit.getOfflinePlayer(this.getOpponent(winner)).getName()));
        if (Bukkit.getPlayer(this.getOpponent(winner)) == null || disconnecting) {
            players.remove(this.getOpponent(winner));
        }
        if (Bukkit.getPlayer(winner) == null) {
            players.remove(winner);
        }
		players.forEach(uuid -> {
			if (Bukkit.getPlayer(uuid) != null) {
				final Player player = Bukkit.getPlayer(uuid);
				player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------");
				player.spigot().sendMessage(inventoriesMsg);	
				player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------");
				if (!this.main.getMessageLoader().getWinnerMessage().equals("untoggle")) Bukkit.getPlayer(uuid).sendMessage(this.main.getMessageLoader().getWinnerMessage().replace("%winner%",  Bukkit.getPlayer(winner).getName()));
				player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
				player.extinguish();
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				player.updateInventory();	
			}
		});
		this.clearDrops(matchID);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				players.forEach(uuid -> {
					if (Bukkit.getPlayer(uuid) != null) {
						Bukkit.getPlayer(uuid).setAllowFlight(true);
						Bukkit.getPlayer(uuid).setFlying(true);	
					}
				});
			}
		}.runTaskLaterAsynchronously(main, 2L);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				players.forEach(uuid -> {
					if (Bukkit.getPlayer(uuid) != null) {
						showPlayers(uuid);
						main.getManagerHandler().getProfileManager().getProfiles().get(uuid).getProfileCache().setMatchID(null);
						Bukkit.getPlayer(uuid).teleport(main.getSpawnLocation() != null ? main.getSpawnLocation() : Bukkit.getPlayer(uuid).getWorld().getSpawnLocation());
						main.getManagerHandler().getProfileManager().getProfiles().get(uuid).setProfileState(ProfileState.FREE);
						main.getManagerHandler().getItemManager().giveItems(Bukkit.getPlayer(uuid), "spawn-items");
						resetPlayer(uuid);	
					}
				});
				matchs.remove(matchEntry.getMatchID());
			}
		}.runTaskLaterAsynchronously(this.main, this.main.getMessageLoader().getRespawnTime()*20L);
	}
	
    public UUID getOpponent(final UUID uuid) {
    	UUID opponentUUID = null;
		final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(uuid);
		final MatchEntry matchEntry = this.getMatchs().get(profile.getProfileCache().getMatchID());
    	if (matchEntry != null) {
    		for (UUID uuid1 : matchEntry.getPlayersList().get(matchEntry.getPlayersList().get(0).contains(uuid) ? 1 : 0)) {
    			opponentUUID = uuid1;
    		}
    	}
    	return opponentUUID;
    }

    public void hidingPlayers(final UUID uuid, final UUID matchID) {
    	for (Entry<UUID, MatchEntry> entry : this.getMatchs().entrySet()) {
    		if (entry.getKey() == matchID) return;
    		for (List<UUID> listUUID : entry.getValue().getPlayersList()) {
    			for (UUID uuids : listUUID) {
    				final Player player = Bukkit.getPlayer(uuid);
    				final Player extPlayer = Bukkit.getPlayer(uuids);
    				player.hidePlayer(extPlayer);
    				extPlayer.hidePlayer(player);
    			}
    		}
    	}
    }
    
    public void showPlayers(final UUID uuid) {
    	new BukkitRunnable() {		
			@Override
			public void run() {
		    	for (Entry<UUID, MatchEntry> entry : getMatchs().entrySet()) {
		    		for (List<UUID> listUUID : entry.getValue().getPlayersList()) {
		    			for (UUID uuids : listUUID) {
		    				final Player player = Bukkit.getPlayer(uuid);
		    				final Player extPlayer = Bukkit.getPlayer(uuids);
		    				if (player != null || extPlayer != null) {
			    				player.showPlayer(extPlayer);
			    				extPlayer.showPlayer(player);	
		    				}
		    			}
		    		}
		    	}
			}
		}.runTaskLater(this.main, this.main.getMessageLoader().getRespawnTime()*20L);
    }
    
	public void addDrops(Item item, final UUID uuid) {
		this.main.getManagerHandler().getMatchManager().getMatchs().get(this.main.getManagerHandler().getProfileManager().getProfiles().get(uuid).getProfileCache().getMatchID()).getDropped().add(item.getUniqueId());
	}
	
	public void removeDrops(Item item, final UUID uuid) {
		this.main.getManagerHandler().getMatchManager().getMatchs().get(this.main.getManagerHandler().getProfileManager().getProfiles().get(uuid).getProfileCache().getMatchID()).getDropped().remove(item.getUniqueId());
	}
	
	public boolean containDrops(Item item, final UUID uuid) {
		return this.main.getManagerHandler().getMatchManager().getMatchs().get(this.main.getManagerHandler().getProfileManager().getProfiles().get(uuid).getProfileCache().getMatchID()).getDropped().contains(item.getUniqueId());
	}
	
	public void clearDrops(final UUID uuid) {
		if (this.main.getManagerHandler().getMatchManager().getMatchs().get(uuid).getDropped().isEmpty()) return;
		final World world = Bukkit.getWorld("world");
		for (Entity entities : world.getEntities()) {
			if (entities == null || !(entities instanceof Item) && !this.main.getManagerHandler().getMatchManager().getMatchs().get(uuid).getDropped().contains(entities.getUniqueId())) continue;
			entities.remove();
		}
	}
    
    private void resetPlayer(final UUID uuid) {
    	final Player player = Bukkit.getPlayer(uuid);
    	player.setHealth(player.getMaxHealth());
    	player.setFoodLevel(20);
    	player.setAllowFlight(false);
    	player.setFlying(false);
    }
}
