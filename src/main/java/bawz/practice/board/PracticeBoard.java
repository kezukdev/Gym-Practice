package bawz.practice.board;

import java.text.DateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;

import bawz.practice.Main;
import bawz.practice.ladder.Ladder;
import bawz.practice.profile.Profile;
import bawz.practice.profile.ProfileState;

public class PracticeBoard implements BoardAdapter {
	
    private final Main plugin;
    DateFormat shortDateFormat = DateFormat.getDateTimeInstance(
            DateFormat.SHORT,
            DateFormat.SHORT);
    
    public PracticeBoard(final Main main) {
        this.plugin = main;
    }
    
    @Override
    public String getTitle(final Player player) {
        return ChatColor.translateAlternateColorCodes('&', this.plugin.getScoreboardFile().getName());
    }
    
    @Override
    public List<String> getScoreboard(final Player player, final Board board, final Set<BoardCooldown> cooldowns) {
        final Profile pm = this.plugin.getManagerHandler().getProfileManager().getProfiles().get(player.getUniqueId());
        if (pm == null) {
            this.plugin.getLogger().warning(String.valueOf(player.getName()) + "'s profile data is null");
            return null;
        }
        if (pm.getProfileData().isScoreboard()) {
        	if (pm.getProfileState().equals(ProfileState.FREE) || pm.getProfileState().equals(ProfileState.QUEUE)) {
        		return getLobbyBoard(player);
        	}
        }
		return null;
    }
    
	private List<String> getLobbyBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        final Profile pm = this.plugin.getManagerHandler().getProfileManager().getProfiles().get(player.getUniqueId());
        for (String str : this.plugin.getScoreboardFile().getAdaptaters().get(0)) {
        	String string = str;
        	for (Ladder ladder : this.plugin.getLadders()) {
            	board.add(str.replace("%currentlyOnline%", String.valueOf(Bukkit.getOnlinePlayers().size())).replace("%laddersName%", ChatColor.stripColor(ladder.getDisplayName()).replace("%laddersElo%", String.valueOf(pm.getProfileData().getElos()[ladder.getId()]))));
        	}
        }
        if (this.plugin.getScoreboardFile().isQueueInLobby() && pm.getProfileState().equals(ProfileState.QUEUE)) {
            for (String str : this.plugin.getScoreboardFile().getAdaptaters().get(1)) {
            	board.add(str.replace("%ladderName%", ChatColor.stripColor(this.plugin.getManagerHandler().getQueueManager().getQueues().get(player.getUniqueId()).getLadder().getDisplayName())).replace("%queueType%", ChatColor.stripColor(this.plugin.getManagerHandler().getQueueManager().getQueues().get(player.getUniqueId()).getQueueType().toString())));
            }
        }
        return board;
    }
}