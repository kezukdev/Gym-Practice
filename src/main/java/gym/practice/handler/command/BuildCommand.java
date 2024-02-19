package gym.practice.handler.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gym.practice.Main;
import gym.practice.profile.Profile;
import gym.practice.profile.ProfileState;

public class BuildCommand implements CommandExecutor {
	
	private Main main;
	
	public BuildCommand(final Main main) { this.main = main; }

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		if (!sender.hasPermission(this.main.getMessageLoader().getBuildPermissions())) {
			sender.sendMessage(this.main.getMessageLoader().getNoPermission());
			return false;
		}
		final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(Bukkit.getPlayer(sender.getName()).getUniqueId());
		if (profile.getProfileState().equals(ProfileState.FIGHT) || profile.getProfileState().equals(ProfileState.QUEUE)) {
			sender.sendMessage(ChatColor.RED + "You cannot do this right now!");
			return false;
		}
		Bukkit.getPlayer(sender.getName()).sendMessage(profile.getProfileState().equals(ProfileState.BUILD) ? this.main.getMessageLoader().getBuildOff() : this.main.getMessageLoader().getBuildOn());
		Bukkit.getPlayer(sender.getName()).setGameMode(profile.getProfileState().equals(ProfileState.BUILD) ? GameMode.SURVIVAL : GameMode.CREATIVE);
		if (profile.getProfileState().equals(ProfileState.FREE)) Bukkit.getPlayer(sender.getName()).getInventory().clear();
		if (profile.getProfileState().equals(ProfileState.BUILD)) this.main.getManagerHandler().getItemManager().giveItems(Bukkit.getPlayer(sender.getName()), "spawn-items");
		profile.setProfileState(profile.getProfileState().equals(ProfileState.BUILD) ? ProfileState.FREE : ProfileState.BUILD);
		return false;
	}

}
