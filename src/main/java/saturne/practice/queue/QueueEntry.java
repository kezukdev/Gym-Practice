package saturne.practice.queue;

import java.util.List;
import java.util.UUID;

import saturne.practice.ladder.Ladder;

public class QueueEntry {
	
	private final List<UUID> uuid;
	private final Ladder ladder;
	private final QueueType queueType;
	
	public QueueEntry(final List<UUID> uuid, final Ladder ladder, final QueueType queueType) {
		this.uuid = uuid;
		this.ladder = ladder;
		this.queueType = queueType;
	}

	public Ladder getLadder() {
		return ladder;
	}
	
	public QueueType getQueueType() {
		return queueType;
	}
	
	public List<UUID> getUuid() {
		return uuid;
	}
}
