package bawz.practice.profile;

import java.util.UUID;

import bawz.practice.Main;
import bawz.practice.profile.cache.ProfileCache;
import bawz.practice.profile.data.ProfileData;
import bawz.practice.settings.Settings;
import lombok.Getter;

public class Profile {
	
	private Main main = Main.getInstance();
	
	@Getter
	private UUID uuid;
	private ProfileState profileState;
	public ProfileState getProfileState() { return profileState; }
	public void setProfileState(ProfileState profileState) { this.profileState = profileState; }
	private ProfileCache profileCache;
	public ProfileCache getProfileCache() { return profileCache; }
	public ProfileData profileData;
	public ProfileData getProfileData() { return profileData; }
	
	public Profile(final UUID uuid) {
		this.uuid = uuid;
		this.profileState = ProfileState.FREE;
		this.profileCache = new ProfileCache();
		if (!this.main.getManagerHandler().getProfileManager().getProfileData().containsKey(uuid)) {
			Integer[] elos = new Integer[this.main.getLadders().size()];
	        for(int i = 0; i <= elos.length-1; i++) elos[i] = this.main.getElosDefault();
			this.profileData = new ProfileData(elos, true);	
		}
		this.main.getManagerHandler().getProfileManager().getProfiles().putIfAbsent(uuid, this);
		this.main.getManagerHandler().getProfileManager().dataManagement(uuid, false);
		this.profileCache.settingsCache = new Settings(this.main, this.profileData.isScoreboard());
	}
	
	public void exit() {
		this.main.getManagerHandler().getProfileManager().dataManagement(uuid, true);
	}
	
}
