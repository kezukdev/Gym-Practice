package bawz.practice.profile.cache;

import java.util.UUID;

import bawz.practice.profile.data.ProfileData;

public class ProfileCache {
	
	private UUID matchID;
	public UUID getMatchID() { return matchID; }
	public void setMatchID(UUID matchID) { this.matchID = matchID; }
	private ProfileData profileData = new ProfileData();
	public ProfileData getProfileData() { return profileData; }

}
