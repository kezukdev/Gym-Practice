package bawz.practice.handler.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import bawz.practice.Main;
import bawz.practice.arena.Arena;
import bawz.practice.ladder.Ladder;
import bawz.practice.match.MatchEntry;
import bawz.practice.match.MatchState;
import bawz.practice.profile.Profile;
import bawz.practice.profile.ProfileState;
import net.md_5.bungee.api.ChatColor;

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
		for (UUID uuid : players) {
			final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(uuid);
			profile.getProfileCache().setMatchID(matchID);
			final Player player = Bukkit.getPlayer(uuid);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.match-found").replace("%opponent%", Bukkit.getPlayer(this.getOpponent(uuid)).getName())));
			player.teleport(firstList.contains(uuid) ? arena.getLoc1().toBukkitLocation() : arena.getLoc2().toBukkitLocation());
			profile.setProfileState(ProfileState.FIGHT);
			player.getInventory().setArmorContents(ladder.getArmorContent());
			player.getInventory().setContents(ladder.getContent());
		}
		new BukkitRunnable() {
			Integer counter = 5;
			@Override
			public void run() {
				if (matchEntry.getMatchState().equals(MatchState.ENDING) || matchEntry.getMatchState().equals(MatchState.PLAYING)) {
					this.cancel();
				}
				counter -= 1;
				if (counter <= 0) {
					for (UUID uuid : players) {
						Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.match-started")));
					}
					matchEntry.setMatchState(MatchState.PLAYING);
					this.cancel();
				}
				if (counter > 0) {
					for (UUID uuid : players) {
						Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.countdown-message").replace("%countdown%", String.valueOf(counter))));
					}	
				}
			}
		}.runTaskTimerAsynchronously(main, 20L, 20L);
	}
	
	public void endMatch(final UUID winner, final UUID matchID) {
		final MatchEntry matchEntry = this.getMatchs().get(matchID);
		matchEntry.setMatchState(MatchState.ENDING);
		final List<UUID> players = Lists.newArrayList(matchEntry.getPlayersList().get(0));
		players.addAll(matchEntry.getPlayersList().get(1));
		for (UUID uuid : players) {
			final Player player = Bukkit.getPlayer(uuid);
			for (String str : this.main.getConfig().getStringList("messages.inventories-message")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', str.replace("%winner%", Bukkit.getPlayer(winner).getName())));
			}
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				for (UUID uuid : players) {
					final Player player = Bukkit.getPlayer(uuid);
					final Profile profile = main.getManagerHandler().getProfileManager().getProfiles().get(uuid);
					player.teleport(main.getSpawnLocation() != null ? main.getSpawnLocation() : player.getWorld().getSpawnLocation());
					profile.getProfileCache().setMatchID(null);
					profile.setProfileState(ProfileState.FREE);
					player.getInventory().setArmorContents(null);
					for (PotionEffect effect : player.getActivePotionEffects()) {
						player.removePotionEffect(effect.getType());
					}
					player.updateInventory();
					main.getManagerHandler().getItemManager().giveItems(player, "spawn-items");
				}
			}
		}.runTaskLaterAsynchronously(main, 20*this.main.getConfig().getInt("respawn-after-match-time"));
	}
	
    public UUID getOpponent(final UUID uuid) {
    	UUID opponentUUID = null;
		final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(uuid);
		final MatchEntry matchEntry = this.getMatchs().get(profile.getProfileCache().getMatchID());
		Integer teamID = matchEntry.getPlayersList().get(0).contains(uuid) ? 1 : 0;
    	if (matchEntry != null) {
    		for (UUID uuid1 : matchEntry.getPlayersList().get(teamID)) {
    			opponentUUID = uuid1;
    		}
    	}
    	return opponentUUID;
    }

}
