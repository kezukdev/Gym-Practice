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
	
	private final Main main = Main.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		final Profile profile = this.main.getProfiles().get(Bukkit.getPlayer(sender.getName()).getUniqueId());
		if (profile.getProfileState() != ProfileState.QUEUE) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.not-in-queue")));
			return false;
		}
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.leave-queue").replace("%ladderName%", ChatColor.stripColor(this.main.getQueues().get(Bukkit.getPlayer(sender.getName()).getUniqueId()).getLadder().getDisplayName()))).replace("%queueType%", this.main.getQueues().get(Bukkit.getPlayer(sender.getName()).getUniqueId()).getQueueType().toString().toLowerCase()));
		profile.setProfileState(ProfileState.FREE);
		this.main.getManagerHandler().getItemManager().giveItems(Bukkit.getPlayer(sender.getName()), "spawn-items");
		return false;
	}

}
