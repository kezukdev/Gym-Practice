package bawz.practice.handler.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
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
			this.main.getSpigotHook().applyKitKnockback(player, ladder);
		});
		new CountdownRunnable(matchEntry, players, main).runTaskTimerAsynchronously(main, 20L, 20L);
	}
	
	public void endMatch(final UUID winner, final UUID matchID) {
		final MatchEntry matchEntry = this.getMatchs().get(matchID);
		matchEntry.setMatchState(MatchState.ENDING);
		final List<UUID> players = Lists.newArrayList(matchEntry.getPlayersList().get(0));
		players.addAll(matchEntry.getPlayersList().get(1));
		TextComponent inventoriesMsg = new TextComponent(this.main.getMessageLoader().getInventoriesMessage());
		final StringBuilder builderOne =  new StringBuilder();
		final StringBuilder builderTwo =  new StringBuilder();
        matchEntry.getPlayersList().get(0).stream().map(this.main.getServer()::getPlayer).filter(Objects::nonNull).forEach(member -> builderOne.append((matchEntry.getPlayersList().get(0).contains(winner) ? ChatColor.GREEN : ChatColor.RED) + member.getName()).append(ChatColor.GRAY + ","));
        matchEntry.getPlayersList().get(1).stream().map(this.main.getServer()::getPlayer).filter(Objects::nonNull).forEach(member -> builderTwo.append((matchEntry.getPlayersList().get(1).contains(winner) ? ChatColor.GREEN : ChatColor.RED) + member.getName()).append( ChatColor.GRAY + ","));
		final TextComponent winnerComponent = new TextComponent(matchEntry.getPlayersList().get(0).contains(winner) ? builderOne.toString() : builderTwo.toString());
		final TextComponent looserComponent = new TextComponent(matchEntry.getPlayersList().get(0).contains(winner) ? builderTwo.toString() : builderOne.toString());
		inventoriesMsg.addExtra(winnerComponent);
		inventoriesMsg.addExtra(ChatColor.GRAY + ", ");
		inventoriesMsg.addExtra(looserComponent);
		players.forEach(uuid -> {
			Bukkit.getPlayer(uuid).spigot().sendMessage(inventoriesMsg);	
			Bukkit.getPlayer(uuid).sendMessage(this.main.getMessageLoader().getWinnerMessage().replace("%winner%",  Bukkit.getPlayer(winner).getName()));
			final Player player = Bukkit.getPlayer(uuid);
			player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
			player.getInventory().setArmorContents(null);
			player.updateInventory();
		});
		new BukkitRunnable() {
			
			@Override
			public void run() {
				players.forEach(uuid -> {
					showPlayers(uuid);
					main.getManagerHandler().getProfileManager().getProfiles().get(uuid).getProfileCache().setMatchID(null);
					Bukkit.getPlayer(uuid).teleport(main.getSpawnLocation() != null ? main.getSpawnLocation() : Bukkit.getPlayer(uuid).getWorld().getSpawnLocation());
					main.getManagerHandler().getProfileManager().getProfiles().get(uuid).setProfileState(ProfileState.FREE);
					main.getManagerHandler().getItemManager().giveItems(Bukkit.getPlayer(uuid), "spawn-items");
				});
			}
		}.runTaskLaterAsynchronously(this.main, this.main.getMessageLoader().getRespawnTime()*20L);
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
    	new BukkitRunnable() {		
			@Override
			public void run() {
		    	for (Entry<UUID, MatchEntry> entry : getMatchs().entrySet()) {
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
		}.runTaskLater(this.main, this.main.getMessageLoader().getRespawnTime()*20L);
    }
}
