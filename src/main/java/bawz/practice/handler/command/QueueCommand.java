package bawz.practice.handler.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bawz.practice.Main;
import bawz.practice.profile.Profile;
import bawz.practice.profile.ProfileState;
import net.md_5.bungee.api.ChatColor;

public class QueueCommand implements CommandExecutor {
	
	private Main main;
	
	public QueueCommand(final Main main) { this.main = main; }

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(Bukkit.getPlayer(sender.getName()).getUniqueId());
		if (profile.getProfileState() != ProfileState.QUEUE) {
			sender.sendMessage(this.main.getMessageLoader().getNotInQueue());
			return false;
		}
		sender.sendMessage(this.main.getMessageLoader().getLeaveQueue().replace("%ladderName%", ChatColor.stripColor(this.main.getManagerHandler().getQueueManager().getQueues().get(Bukkit.getPlayer(sender.getName()).getUniqueId()).getLadder().getName())).replace("%queueType%", this.main.getManagerHandler().getQueueManager().getQueues().get(Bukkit.getPlayer(sender.getName()).getUniqueId()).getQueueType().toString().toLowerCase()));
		profile.setProfileState(ProfileState.FREE);
		this.main.getManagerHandler().getQueueManager().getQueues().remove(Bukkit.getPlayer(sender.getName()).getUniqueId());
		this.main.getManagerHandler().getItemManager().giveItems(Bukkit.getPlayer(sender.getName()), "spawn-items");
		return false;
	}

}
