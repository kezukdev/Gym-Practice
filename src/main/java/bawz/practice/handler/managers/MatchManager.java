package bawz.practice.handler.managers;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
	
	private final Main main = Main.getInstance();
	
	public void startMatch(final List<UUID> firstList, final List<UUID> secondList, final UUID matchID) {
		final List<UUID> players = Lists.newArrayList(firstList);
		players.addAll(secondList);
		final MatchEntry matchEntry = this.main.getMatchs().get(matchID);
		final Ladder ladder = matchEntry.getLadder();
		final Arena arena = this.main.getManagerHandler().getArenaManager().getRandomArena(ladder.getLadderType().getArenaType());
		matchEntry.setMatchState(MatchState.STARTING);
		for (UUID uuid : players) {
			final Profile profile = this.main.getProfiles().get(uuid);
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
				if (counter == 0) {
					for (UUID uuid : players) {
						Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.match-started")));
					}
					matchEntry.setMatchState(MatchState.PLAYING);
					this.cancel();
				}
				counter =- 1;
				for (UUID uuid : players) {
					Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.countdown-message").replace("%countdown%", String.valueOf(counter))));
				}
			}
		}.runTaskTimerAsynchronously(main, 20L, 20L);
	}
	
	public void endMatch(final UUID winner, final UUID matchID) {
		final MatchEntry matchEntry = this.main.getMatchs().get(matchID);
		matchEntry.setMatchState(MatchState.ENDING);
		final List<UUID> players = Lists.newArrayList(matchEntry.getFirstList());
		players.addAll(matchEntry.getSecondList());
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
					final Profile profile = main.getProfiles().get(uuid);
					player.teleport(main.getSpawnLocation() != null ? main.getSpawnLocation() : player.getWorld().getSpawnLocation());
					profile.getProfileCache().setMatchID(null);
				}
			}
		}.runTaskLaterAsynchronously(main, 20*this.main.getConfig().getInt("respawn-after-match-time"));
	}
	
	public UUID getOpponent(final UUID uuid) {
		final Profile profile = this.main.getProfiles().get(uuid);
		final MatchEntry matchEntry = this.main.getMatchs().get(profile.getProfileCache().getMatchID());
		if (matchEntry.getFirstList().contains(uuid)) {
			for (UUID uuids : matchEntry.getSecondList()) {
				return uuids;
			}
		}
		for (UUID uuids : matchEntry.getSecondList()) {
			return uuids;
		}
		return null;
	}

}