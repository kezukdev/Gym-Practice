package bawz.practice.runnable;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import bawz.practice.Main;
import bawz.practice.match.MatchEntry;
import bawz.practice.match.MatchState;

public class CountdownRunnable extends BukkitRunnable {
	
	private Main main;
	private MatchEntry matchEntry;
	private List<UUID> players;
	private Integer counter;
	
	public CountdownRunnable(final MatchEntry matchEntry, final List<UUID> players, final Main main) {
		this.main = main;
		this.matchEntry = matchEntry;
		this.players = players;
		this.counter = 5;
		this.run();
	}

	@Override
	public void run() {
		if (matchEntry.getMatchState().equals(MatchState.ENDING) || matchEntry.getMatchState().equals(MatchState.PLAYING)) {
			this.cancel();
		}
		counter -= 1;
		if (counter <= 0) {
			players.forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(main.getMessageLoader().getMatchStarted()));
			matchEntry.setMatchState(MatchState.PLAYING);
			this.cancel();
		}
		if (counter > 0) {
			players.forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(main.getMessageLoader().getMatchCountdown().replace("%countdown%", String.valueOf(counter))));
		}
	}

}
