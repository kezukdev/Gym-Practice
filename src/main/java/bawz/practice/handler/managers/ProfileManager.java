package bawz.practice.handler.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import bawz.practice.Main;
import bawz.practice.profile.Profile;
import bawz.practice.profile.data.ProfileData;
import bawz.practice.utils.config.Config;
import bawz.practice.utils.config.ConfigFile;
import net.minecraft.util.com.google.common.collect.Maps;

public class ProfileManager {

	private final Main main = Main.getInstance();
	
	private final HashMap<UUID, ProfileData> profileData = Maps.newHashMap();
	public HashMap<UUID, ProfileData> getProfileData() { return profileData; }
	private final Map<UUID, Profile> profiles = Maps.newHashMap();
	public Map<UUID, Profile> getProfiles() { return profiles; }
	
	private Config config;
	private ConfigFile configFile;
	
	public void dataManagement(final UUID uuid, final boolean disconnecting) {
		if (disconnecting && !this.profileData.containsKey(uuid)) {
			this.profileData.put(uuid, this.profiles.get(uuid).getProfileCache().getProfileData());
		}
		if (!disconnecting && this.profileData.containsKey(uuid)) {
			
			this.profileData.remove(uuid);
		}
	}
	
	public void writtingFiles(final UUID uuid) {
		if (this.profiles.get(uuid) == null) new Profile(uuid);
		if (!Boolean.valueOf(this.main.getConfig().getString("database.sql.enable")) && !Boolean.valueOf(this.main.getConfig().getString("database.mongo.enable"))) {
			config = new Config("players/" + uuid.toString(), main);	
			this.profileCache.elos = new Integer[this.main.getLadders().size()];
			for(int i = 0; i <= this.profileCache.elos.length-1; i++) this.profileCache.elos[i] = this.main.getConfig().getInt("default-elos");
			this.getConfig().getConfig().createSection("elos");
			this.getConfig().getConfig().set("elos", this.profileCache.elos);
			this.profileCache.elos = this.getConfig().getConfig().getIntegerList("elos").toArray(new Integer[this.main.getLadders().size()]);	
		}	
		if (Bukkit.getPlayer(uuid) == null) this.profiles.remove(uuid);
	}
}
