package gym.practice.handler.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import gym.practice.Main;

public class MergeCommand implements CommandExecutor {

	private Main main;
	
	public MergeCommand(final Main main) { this.main = main; }
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		final Player player = (Player) sender;
		if (!this.main.getConfig().getBoolean("inventory.merge-casual-and-ranked")) return false;
		player.openInventory(this.main.getManagerHandler().getInventoryManager().getMerge()[0]);
		return false;
	}

}