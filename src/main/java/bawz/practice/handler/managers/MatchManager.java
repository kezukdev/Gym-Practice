package bawz.practice.handler.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
import bawz.practice.runnable.CountdownRunnable;
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
		});
		new CountdownRunnable(matchEntry, players, main).runTaskTimerAsynchronously(main, 20L, 20L);
	}
	
	public void endMatch(final UUID winner, final UUID matchID) {
		final MatchEntry matchEntry = this.getMatchs().get(matchID);
		matchEntry.setMatchState(MatchState.ENDING);
		final List<UUID> players = Lists.newArrayList(matchEntry.getPlayersList().get(0));
		players.addAll(matchEntry.getPlayersList().get(1));
		TextComponent inventoriesMsg = new TextComponent(this.main.getMessageLoader().getInventoriesMessage());
		final List<StringBuilder> builder = Lists.newArrayList();
		for (int i = 0; i < 2; i++) {
			builder.add(new StringBuilder());
		}
        matchEntry.getPlayersList().get(0).stream().map(this.main.getServer()::getPlayer).filter(Objects::nonNull).forEach(member -> builder.get(0).append(ChatColor.GRAY).append((matchEntry.getPlayersList().get(0).contains(winner) ? ChatColor.GREEN : ChatColor.RED) + member.getName()).append(","));
        matchEntry.getPlayersList().get(1).stream().map(this.main.getServer()::getPlayer).filter(Objects::nonNull).forEach(member -> builder.get(1).append(ChatColor.GRAY).append((matchEntry.getPlayersList().get(1).contains(winner) ? ChatColor.GREEN : ChatColor.RED) + member.getName()).append(","));
		players.forEach(uuid -> {
			final TextComponent winnerComponent = new TextComponent(matchEntry.getPlayersList().get(0).contains(winner) ? builder.get(0).toString() : builder.get(1).toString());
			final TextComponent looserComponent = new TextComponent(matchEntry.getPlayersList().get(1).contains(winner) ? builder.get(1).toString() : builder.get(0).toString());
			inventoriesMsg.addExtra(winnerComponent);
			inventoriesMsg.addExtra(ChatColor.GRAY + ", ");
			inventoriesMsg.addExtra(looserComponent);
			Bukkit.getPlayer(uuid).spigot().sendMessage(inventoriesMsg);	
			Bukkit.getPlayer(uuid).sendMessage(this.main.getMessageLoader().getWinnerMessage().replace("%winner%",  Bukkit.getPlayer(winner).getName()));
		});
		CompletableFuture completable = CompletableFuture.runAsync(new Runnable() {
			@Override
			public void run() {
				players.forEach(uuid -> {
					showPlayers(uuid);
					final Player player = Bukkit.getPlayer(uuid);
					final Profile profile = main.getManagerHandler().getProfileManager().getProfiles().get(uuid);
					player.teleport(main.getSpawnLocation() != null ? main.getSpawnLocation() : player.getWorld().getSpawnLocation());
					profile.getProfileCache().setMatchID(null);
					profile.setProfileState(ProfileState.FREE);
					player.getInventory().setArmorContents(null);
					player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
					player.updateInventory();
					main.getManagerHandler().getItemManager().giveItems(player, "spawn-items");
				});
			}
		});
		try { completable.get(this.main.getMessageLoader().getRespawnTime(), TimeUnit.SECONDS); } catch (InterruptedException | ExecutionException | TimeoutException e) { e.printStackTrace(); }
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
    	for (Entry<UUID, MatchEntry> entry : this.getMatchs().entrySet()) {
    		for (List<UUID> listUUID : entry.getValue().getPlayersList()) {
    			for (UUID uuids : listUUID) {
    				final Player player = Bukkit.getPlayer(uuid);
    				final Player extPlayer = Bukkit.getPlayer(uuids);
    				player.showPlayer(extPlayer);
    				extPlayer.showPlayer(player);
    			}
    		}
    	}
    }
}
