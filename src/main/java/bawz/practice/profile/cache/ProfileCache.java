package bawz.practice.profile.cache;

import java.util.UUID;

public class ProfileCache {
	
	private UUID matchID;
	public UUID getMatchID() { return matchID; }
	public void setMatchID(UUID matchID) { this.matchID = matchID; }
	public SettingsCache settingsCache;
	public SettingsCache getSettingsCache() { return settingsCache; }

}
