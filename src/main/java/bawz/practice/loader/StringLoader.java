package bawz.practice.loader;

import bawz.practice.Main;
import bawz.practice.utils.StringUtils;

public class StringLoader {
	
	private Main main;
	
	private String enterQueue;
	public String getEnterQueue() { return enterQueue; }
	private String leaveQueue;
	public String getLeaveQueue() { return leaveQueue; }
	private String notInQueue;
	public String getNotInQueue() { return notInQueue; }
	private String noPermission;
	public String getNoPermission() { return noPermission; }
	private String arenaAlreadyExist;
	public String getArenaAlreadyExist() { return arenaAlreadyExist; }
	private String arenaCreated;
	public String getArenaCreated() { return arenaCreated; }
	private String arenaNotExist;
	public String getArenaNotExist() { return arenaNotExist; }
	private String arenaLocation;
	public String getArenaLocation() { return arenaLocation; }
	private boolean joinMessageEnabled;
	public boolean isJoinMessageEnabled() { return joinMessageEnabled; }
	private String joinMessage;
	public String getJoinMessage() { return joinMessage; }
	private boolean leaveMessageEnabled;
	public boolean isLeaveMessageEnabled() { return leaveMessageEnabled; }
	private String leaveMessage;
	public String getLeaveMessage() { return leaveMessage; }
	private String cannotLaunchPearl;
	public String getCannotLaunchPearl() { return cannotLaunchPearl; }
	private String cooldownEnderpearl;
	public String getCooldownEnderpearl() { return cooldownEnderpearl; }
	private String killMessage;
	public String getKillMessage() { return killMessage; }
	private String motdWhitelistOne;
	public String getMotdWhitelistOne() { return motdWhitelistOne; }
	private String motdWhitelistTwo;
	public String getMotdWhitelistTwo() { return motdWhitelistTwo; }
	private String motdUnwhitelistOne;
	public String getMotdUnwhitelistOne() { return motdUnwhitelistOne; }
	private String motdUnwhitelistTwo;
	public String getMotdUnwhitelistTwo() { return motdUnwhitelistTwo; }
	private Integer maxSlots;
	public Integer getMaxSlots() { return maxSlots; }
	private Integer respawnTime;
	public Integer getRespawnTime() { return respawnTime; }
	private String winnerMessage;
	public String getWinnerMessage() { return winnerMessage; }
	private String inventoriesMessage;
	public String getInventoriesMessage() { return inventoriesMessage; }
	private String matchStarted;
	public String getMatchStarted() { return matchStarted; }
	private String matchCountdown;
	public String getMatchCountdown() { return matchCountdown; }
	private String matchFound;
	public String getMatchFound() { return matchFound; }
	
	public StringLoader(final Main main) {
		this.main = main;
		this.enterQueue = StringUtils.translate(this.main.getConfig().getString("messages.enter-in-queue"));
		this.leaveQueue = StringUtils.translate(this.main.getConfig().getString("messages.leave-queue"));
		this.notInQueue = StringUtils.translate(this.main.getConfig().getString("messages.not-in-queue"));
		this.noPermission = StringUtils.translate(this.main.getConfig().getString("messages.no-permissions"));
		this.arenaAlreadyExist = StringUtils.translate(this.main.getConfig().getString("messages.arena-already-exist"));
		this.arenaCreated = StringUtils.translate(this.main.getConfig().getString("messages.arena-created"));
		this.arenaNotExist = StringUtils.translate(this.main.getConfig().getString("messages.arena-not-exist"));
		this.arenaLocation = StringUtils.translate(this.main.getConfig().getString("messages.arena-location"));
		this.joinMessageEnabled = Boolean.valueOf(this.main.getConfig().getString("join-message.enabled"));
		if (joinMessageEnabled) this.joinMessage = StringUtils.translate(this.main.getConfig().getString("join-message.message"));
		this.leaveMessageEnabled = Boolean.valueOf(this.main.getConfig().getString("leave-message.enabled"));
		if (leaveMessageEnabled) this.leaveMessage = StringUtils.translate(this.main.getConfig().getString("leave-message.message"));
		this.cannotLaunchPearl = StringUtils.translate(this.main.getConfig().getString("messages.match.cannot-launch-enderpearl"));
		this.cooldownEnderpearl = StringUtils.translate(this.main.getConfig().getString("messages.match.cooldown-enderpearl"));
		this.killMessage = StringUtils.translate(this.main.getConfig().getString("messages.match.kill"));
		this.motdWhitelistOne = StringUtils.translate(this.main.getConfig().getString("motd.whitelist.1"));
		this.motdWhitelistTwo = StringUtils.translate(this.main.getConfig().getString("motd.whitelist.2"));
		this.motdUnwhitelistOne = StringUtils.translate(this.main.getConfig().getString("motd.unwhitelist.1"));
		this.motdUnwhitelistTwo = StringUtils.translate(this.main.getConfig().getString("motd.unwhitelist.2"));
		this.maxSlots = this.main.getConfig().getInt("maxslots");
		this.respawnTime = this.main.getConfig().getInt("respawn-after-match-time");
		this.winnerMessage = StringUtils.translate(this.main.getConfig().getString("messages.match.winner"));
		this.inventoriesMessage = StringUtils.translate(this.main.getConfig().getString("messages.match.inventories"));
		this.matchStarted = StringUtils.translate(main.getConfig().getString("messages.match.begin"));
		this.matchCountdown = StringUtils.translate(main.getConfig().getString("messages.match.countdown"));
		this.matchFound = StringUtils.translate(this.main.getConfig().getString("messages.match.found"));
		System.out.println("[GYM] Stringer > Loaded");
	}

}
