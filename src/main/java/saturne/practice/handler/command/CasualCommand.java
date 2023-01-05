package saturne.practice.handler.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import saturne.practice.Main;

public class CasualCommand implements CommandExecutor {

	private final Main main = Main.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		final Player player = (Player) sender;
		player.openInventory(this.main.getManagerHandler().getInventoryManager().getCasualInventory());
		return false;
	}

}
