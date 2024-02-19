package gym.practice.runnable;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import gym.practice.Main;
import gym.practice.match.MatchEntry;
import gym.practice.match.MatchState;

public class CountdownRunnable extends BukkitRunnable {
	
	private final Main main;
	private MatchEntry matchEntry;
	private List<UUID> players;
	private Integer counter;
	
	public CountdownRunnable(final MatchEntry matchEntry, final List<UUID> players, final Main main) {
		this.main = main;
		this.matchEntry = matchEntry;
		this.players = players;
		this.counter = 6;
		this.run();
	}

	@Override
	public void run() {
		if (matchEntry.getMatchState().equals(MatchState.ENDING) || matchEntry.getMatchState().equals(MatchState.PLAYING)) {
			this.cancel();
			return;
		}
		counter -= 1;
		if (counter <= 0) {
			players.forEach(uuid -> {
				if (Bukkit.getPlayer(uuid) != null) {
					if (Bukkit.getPlayer(uuid).getInventory().contains(Material.SUGAR_CANE)) {
						Bukkit.getPlayer(uuid).getInventory().setArmorContents(matchEntry.getLadder().getArmorContent());
						Bukkit.getPlayer(uuid).getInventory().setContents(matchEntry.getLadder().getContent());
					}
					Bukkit.getPlayer(uuid).sendMessage(main.getMessageLoader().getMatchStarted());	
				}
			});
			matchEntry.setMatchState(MatchState.PLAYING);
			this.cancel();
		}
		if (counter > 0) {
			if (players != null) {
				players.forEach(uuid -> {
					if (Bukkit.getPlayer(uuid) != null) {
						Bukkit.getPlayer(uuid).sendMessage(main.getMessageLoader().getMatchCountdown().replace("%countdown%", String.valueOf(counter)));	
					}
				});	
			}
		}
	}

}
