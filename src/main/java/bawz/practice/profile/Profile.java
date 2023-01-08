package bawz.practice.profile;

import java.util.UUID;

import bawz.practice.Main;
import bawz.practice.profile.cache.ProfileCache;
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
	
	public Profile(final UUID uuid) {
		this.uuid = uuid;
		this.profileState = ProfileState.FREE;
		this.profileCache = new ProfileCache();
		this.main.getProfiles().putIfAbsent(uuid, this);
	}
	
}
