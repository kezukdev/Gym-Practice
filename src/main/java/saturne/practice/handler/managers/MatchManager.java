package saturne.practice.handler.managers;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import saturne.practice.Main;
import saturne.practice.arena.Arena;
import saturne.practice.ladder.Ladder;
import saturne.practice.match.MatchEntry;
import saturne.practice.match.MatchState;
import saturne.practice.profile.Profile;
import saturne.practice.profile.ProfileState;

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
			player.teleport(firstList.contains(uuid) ? arena.getLoc1().toBukkitLocation() : arena.getLoc2().toBukkitLocation());
			profile.setProfileState(ProfileState.FIGHT);
			player.getInventory().setArmorContents(ladder.getArmorContent());
			player.getInventory().setContents(ladder.getContent());
		}
	}
	
	public void endMatch() {
		
	}

}
