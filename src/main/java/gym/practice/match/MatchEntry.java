package gym.practice.match;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import gym.practice.Main;
import gym.practice.ladder.Ladder;
import gym.practice.match.sub.MatchStatistics;
import gym.practice.queue.QueueType;

public class MatchEntry {
	
	private final Main main;
	
	private UUID matchID;
	public UUID getMatchID() { return matchID; }
	private List<List<UUID>> playersList;
	public List<List<UUID>> getPlayersList() { return playersList; }
	private Ladder ladder;
	public Ladder getLadder() { return ladder; }
	private QueueType queueType;
	public QueueType getQueueType() { return queueType; }
	private MatchState matchState;
	public MatchState getMatchState() { return matchState; }
	public void setMatchState(MatchState matchState) { this.matchState = matchState; }
	private List<List<UUID>> alives;
	public List<List<UUID>> getAlives() { return alives; }
	private List<UUID> spectator;
	public List<UUID> getSpectator() { return spectator; }
	private Set<UUID> dropped;
	public Set<UUID> getDropped() { return dropped; }
	private HashMap<UUID, MatchStatistics> matchStatistics;
	public HashMap<UUID, MatchStatistics> getMatchStatistics() { return matchStatistics; }
	
	public MatchEntry(final List<List<UUID>> players, final Ladder ladder, final QueueType queueType, final Main main) {
		this.main = main;
		this.matchID = UUID.randomUUID();
		this.playersList = players;
		this.ladder = ladder;
		this.queueType = queueType;
		this.alives = Lists.newArrayList();
		this.alives.addAll(players);
		this.spectator = Lists.newArrayList();
		this.matchStatistics = new HashMap<>();
		this.dropped = Sets.newHashSet();
		this.main.getManagerHandler().getMatchManager().getMatchs().putIfAbsent(getMatchID(), this);
		for (List<UUID> listUUID : players) {
			for (UUID uuid : listUUID) {
				new MatchStatistics(uuid, this.matchID, this.main);
			}
		}
		this.main.getManagerHandler().getMatchManager().startMatch(players.get(0), players.get(1), this.getMatchID());
	}
}
