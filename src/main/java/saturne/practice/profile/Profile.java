package saturne.practice.profile;

import java.util.UUID;

import lombok.Getter;
import saturne.practice.Main;

public class Profile {
	
	private Main main = Main.getInstance();
	
	@Getter
	private UUID uuid;
	private ProfileState profileState;
	
	public Profile(final UUID uuid) {
		this.uuid = uuid;
		this.profileState = ProfileState.FREE;
		this.main.getProfiles().putIfAbsent(uuid, this);
	}

	public ProfileState getProfileState() {
		return profileState;
	}
	
	public void setProfileState(ProfileState profileState) {
		this.profileState = profileState;
	}
}
