package bawz.practice.profile.cache;

import java.util.UUID;

import bawz.practice.settings.Settings;

public class ProfileCache {
	
	private UUID matchID;
	public UUID getMatchID() { return matchID; }
	public void setMatchID(UUID matchID) { this.matchID = matchID; }
	public Settings settingsCache;
	public Settings getSettingsCache() { return settingsCache; }

}
