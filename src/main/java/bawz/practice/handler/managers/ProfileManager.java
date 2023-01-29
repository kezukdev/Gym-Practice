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
import net.minecraft.util.com.google.common.collect.Maps;

public class ProfileManager {
	
	private final HashMap<UUID, ProfileData> profileData = Maps.newHashMap();
	public HashMap<UUID, ProfileData> getProfileData() { return profileData; }
	private final Map<UUID, Profile> profiles = Maps.newHashMap();
	public Map<UUID, Profile> getProfiles() { return profiles; }
	
	public ProfileManager() {
		final File dir = new File(Main.getInstance().getDataFolder() + "/players/");
		File[] files = dir.listFiles();
		if (dir.exists()) {
			for (File file : files) {
				YamlConfiguration configFile = YamlConfiguration.loadConfiguration(file);
				final String str = file.getName().replace(".yml", "");
				List<Integer> elos = configFile.getIntegerList("elos");
				Integer[] elosArray = new Integer[Main.getInstance().getLadders().size()];
				elos.toArray(elosArray);
				this.profileData.putIfAbsent(UUID.fromString(str), new ProfileData(elosArray));
			}	
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
