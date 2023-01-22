package bawz.practice.profile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import bawz.practice.Main;
import bawz.practice.profile.cache.ProfileCache;
import bawz.practice.utils.config.Config;
import bawz.practice.utils.config.ConfigFile;
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
	private Config config;
	public Config getConfig() { return config; }
	private ConfigFile file;
	public ConfigFile getFile() { return file; }
	
	public Profile(final UUID uuid) {
		this.uuid = uuid;
		this.profileState = ProfileState.FREE;
		this.profileCache = new ProfileCache();
		this.load();
		this.main.getProfiles().putIfAbsent(uuid, this);
	}
	
	private void load() {
		if (!Boolean.valueOf(this.main.getConfig().getString("database.sql.enable")) && !Boolean.valueOf(this.main.getConfig().getString("database.mongo.enable"))) {
			config = new Config("players/" + this.uuid.toString(), main);
			this.profileCache.elos = new Integer[this.main.getLadders().size()];
			for(int i = 0; i <= this.profileCache.elos.length-1; i++) this.profileCache.elos[i] = this.main.getConfig().getInt("default-elos");
			this.getConfig().getConfig().createSection("elos");
			this.getConfig().getConfig().set("elos", this.profileCache.elos);
			this.profileCache.elos = this.getConfig().getConfig().getIntegerList("elos").toArray(new Integer[this.main.getLadders().size()]);	
		}
	}
	
	public void exit() {
		if (!Boolean.valueOf(this.main.getConfig().getString("database.sql.enable")) && !Boolean.valueOf(this.main.getConfig().getString("database.mongo.enable"))) {
			this.getConfig().getConfig().set("elos", this.profileCache.elos);
			this.getConfig().save();
		}
		this.main.getProfiles().remove(this.uuid);
	}
	
}
