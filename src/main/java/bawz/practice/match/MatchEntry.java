package bawz.practice.match;

import java.util.List;
import java.util.UUID;

import bawz.practice.Main;
import bawz.practice.ladder.Ladder;
import bawz.practice.queue.QueueType;

public class MatchEntry {
	
	private final Main main = Main.getInstance();
	
	private UUID matchID;
	public UUID getMatchID() { return matchID; }
	private List<UUID> firstList;
	public List<UUID> getFirstList() { return firstList; }
	private List<UUID> secondList;
	public List<UUID> getSecondList() { return secondList; }
	private Ladder ladder;
	public Ladder getLadder() { return ladder; }
	private QueueType queueType;
	public QueueType getQueueType() { return queueType; }
	private MatchState matchState;
	public MatchState getMatchState() { return matchState; }
	public void setMatchState(MatchState matchState) { this.matchState = matchState; }
	
	public MatchEntry(final List<UUID> firstList, final List<UUID> secondList, final Ladder ladder, final QueueType queueType) {
		this.matchID = UUID.randomUUID();
		this.firstList = firstList;
		this.secondList = secondList;
		this.ladder = ladder;
		this.queueType = queueType;
		this.main.getMatchs().putIfAbsent(getMatchID(), this);
		this.main.getManagerHandler().getMatchManager().startMatch(firstList, secondList, this.getMatchID());
	}
}
