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
import bawz.practice.match.MatchEntry;
import bawz.practice.match.MatchState;
import bawz.practice.profile.Profile;
import bawz.practice.profile.ProfileState;
import bawz.practice.utils.StringUtils;

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
        	if (pm.getProfileState().equals(ProfileState.FREE) || (this.plugin.getScoreboardFile().isQueueInLobby() && pm.getProfileState().equals(ProfileState.QUEUE))) {
        		return getLobbyBoard(player);
        	}
        	if (!this.plugin.getScoreboardFile().isQueueInLobby() && pm.getProfileState().equals(ProfileState.QUEUE)) {
        		return getQueueBoard(player);
        	}
        	if (pm.getProfileState().equals(ProfileState.FIGHT)) {
        		return getMatchBoard(player);
        	}	
        }
		return null;
    }
    
	private List<String> getLobbyBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        final Profile pm = this.plugin.getManagerHandler().getProfileManager().getProfiles().get(player.getUniqueId());
        for (String str : this.plugin.getScoreboardFile().getAdaptaters().get(0)) {
        	board.add(StringUtils.translate(str.replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size())).replace("%inQueue%", String.valueOf(this.plugin.getManagerHandler().getQueueManager().getQueues().size()))).replace("%inFight%", String.valueOf(this.plugin.getManagerHandler().getMatchManager().getMatchs().size())));
        }
        if (this.plugin.getScoreboardFile().isQueueInLobby() && pm.getProfileState().equals(ProfileState.QUEUE)) {
            for (String str : this.plugin.getScoreboardFile().getAdaptaters().get(1)) {
            	board.add(StringUtils.translate(str.replace("%ladderName%", ChatColor.stripColor(this.plugin.getManagerHandler().getQueueManager().getQueues().get(player.getUniqueId()).getLadder().getDisplayName())).replace("%queueType%", ChatColor.stripColor(this.plugin.getManagerHandler().getQueueManager().getQueues().get(player.getUniqueId()).getQueueType().toString()))));
            }
        }
        return board;
    }
	
	private List<String> getQueueBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        for (String str : this.plugin.getScoreboardFile().getAdaptaters().get(1)) {
        	board.add(StringUtils.translate(str.replace("%ladderName%", ChatColor.stripColor(this.plugin.getManagerHandler().getQueueManager().getQueues().get(player.getUniqueId()).getLadder().getDisplayName())).replace("%queueType%", ChatColor.stripColor(this.plugin.getManagerHandler().getQueueManager().getQueues().get(player.getUniqueId()).getQueueType().toString()))));
        }
        return board;
    }
	
	private List<String> getMatchBoard(final Player player) {
        final List<String> board = new LinkedList<String>();
        final Profile pm = this.plugin.getManagerHandler().getProfileManager().getProfiles().get(player.getUniqueId());
        final MatchEntry matchEntry = this.plugin.getManagerHandler().getMatchManager().getMatchs().get(pm.getProfileCache().getMatchID());
        final String opponent = Bukkit.getPlayer(this.plugin.getManagerHandler().getMatchManager().getOpponent(player.getUniqueId())) != null ? Bukkit.getPlayer(this.plugin.getManagerHandler().getMatchManager().getOpponent(player.getUniqueId())).getName() : Bukkit.getOfflinePlayer(this.plugin.getManagerHandler().getMatchManager().getOpponent(player.getUniqueId())).getName();
        final String type = matchEntry.getQueueType().toString();
        final Integer opponentElo = Bukkit.getPlayer(this.plugin.getManagerHandler().getMatchManager().getOpponent(player.getUniqueId())) != null ? this.plugin.getManagerHandler().getProfileManager().getProfiles().get(this.plugin.getManagerHandler().getMatchManager().getOpponent(player.getUniqueId())).getProfileData().getElos()[matchEntry.getLadder().getId()] : this.plugin.getManagerHandler().getProfileManager().getProfileData().get(this.plugin.getManagerHandler().getMatchManager().getOpponent(player.getUniqueId())).getElos()[matchEntry.getLadder().getId()];
        Integer index = matchEntry.getMatchState().equals(MatchState.STARTING) ? 2 : 3;
        if (matchEntry.getMatchState().equals(MatchState.ENDING)) index = 4;
        for (String str : this.plugin.getScoreboardFile().getAdaptaters().get(index)) {
        	board.add(StringUtils.translate(str.replace("%opponent%", opponent).replace("%type%", type).replace("%opponentElo%", String.valueOf(opponentElo))));
        }
        return board;
    }
}