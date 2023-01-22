package bawz.practice.profile.cache;

import java.util.UUID;

public class ProfileCache {
	
	private UUID matchID;
	public UUID getMatchID() { return matchID; }
	public void setMatchID(UUID matchID) { this.matchID = matchID; }
	public Integer[] elos;
	public Integer[] getElos() { return elos; }

}
