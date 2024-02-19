package gym.practice.handler.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

import gym.practice.Main;

public class SettingsCommand implements CommandExecutor {

	private Main main;
	
	public SettingsCommand(final Main main) { this.main = main; }
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		final Player player = (Player) sender;
		player.openInventory(this.main.getManagerHandler().getProfileManager().getProfiles().get(player.getUniqueId()).getProfileCache().getSettingsCache().getInventory()[0]);
		return false;
	}

}
