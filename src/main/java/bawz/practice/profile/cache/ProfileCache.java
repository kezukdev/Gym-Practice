package bawz.practice.profile.cache;

import java.util.UUID;

public class ProfileCache {
	
	private UUID matchID;
	public UUID getMatchID() { return matchID; }
	public void setMatchID(UUID matchID) { this.matchID = matchID; }
	private SettingsCache settingsCache;
	public SettingsCache getSettingsCache() { return settingsCache; }
	public void setSettingsCache(SettingsCache settingsCache) { this.settingsCache = settingsCache; }

}
