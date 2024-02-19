package gym.practice.handler.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gym.practice.Main;
import gym.practice.profile.Profile;

public class ScoreboardCommand implements CommandExecutor {

	private Main main;
	
	public ScoreboardCommand(final Main main) { this.main = main; }
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		if (!this.main.isScoreboardEnable()) {
			sender.sendMessage(ChatColor.valueOf(this.main.getInventoryLoader().getDisableColor()) + "The scoreboard has disabled for now!");
			return false;
		}
		final Player player = (Player) sender;
		final Profile pm = this.main.getManagerHandler().getProfileManager().getProfiles().get(player.getUniqueId());
		pm.getProfileData().setScoreboard(pm.getProfileData().isScoreboard() ? false : true); 
		pm.getProfileCache().getSettingsCache().loadSettingsInventory(player.getUniqueId());
		return false;
	}

}
