package saturne.practice.profile;

import java.util.UUID;

import lombok.Getter;
import saturne.practice.Main;
import saturne.practice.profile.cache.ProfileCache;

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
