package bawz.practice.handler.managers;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import bawz.practice.Main;
import bawz.practice.profile.Profile;
import bawz.practice.profile.data.ProfileData;

public class ProfileManager {
	
	private Main main;
	
	private final HashMap<UUID, ProfileData> profileData = new HashMap<>();
	public HashMap<UUID, ProfileData> getProfileData() { return profileData; }
	private final Map<UUID, Profile> profiles = new HashMap<>();
	public Map<UUID, Profile> getProfiles() { return profiles; }
	
	public ProfileManager(final Main main) {
		this.main = main;
		final File dir = new File(this.main.getDataFolder() + "/players/");
		File[] files = dir.listFiles();
		if (dir.exists()) {
			for (File file : files) {
				YamlConfiguration configFile = YamlConfiguration.loadConfiguration(file);
				final String str = file.getName().replace(".yml", "");
				List<Integer> elos = configFile.getIntegerList("elos");
				Integer[] elosArray = new Integer[this.main.getLadders().size()];
				elos.toArray(elosArray);
				final boolean scoreboard = configFile.getBoolean("scoreboard");
				this.profileData.putIfAbsent(UUID.fromString(str), new ProfileData(elosArray, scoreboard));
			}	
			System.out.println("[GYM] Data Player > Loaded!");
		}
	}
	
	public void dataManagement(final UUID uuid, final boolean disconnecting) {
		if (disconnecting && !this.profileData.containsKey(uuid)) {
			this.profileData.put(uuid, this.profiles.get(uuid).getProfileData());
		}
		if (!disconnecting && this.profileData.containsKey(uuid)) {
			this.profileData.remove(uuid);
		}
	}
}
