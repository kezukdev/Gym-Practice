package saturne.practice.handler.command;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import saturne.practice.Main;
import saturne.practice.ladder.LadderType;

public class LadderCommand implements CommandExecutor {
	
	private final Main main = Main.getInstance();
	private final String[] help = {"",
			ChatColor.DARK_PURPLE + "Ladders Command:",
			"",
			ChatColor.LIGHT_PURPLE + "/ladder create <name> <type>",
			ChatColor.LIGHT_PURPLE + "/ladder setinv <name>",
			ChatColor.LIGHT_PURPLE + "/ladder seticon <name>",
			ChatColor.LIGHT_PURPLE + "/ladder setdisplay <name> <displayName>"};
	final FileConfiguration fileConfig = this.main.getLadderFile().getConfig();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		if (!sender.hasPermission(this.main.getConfig().getString("permissions.ladder"))) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.no-permissions")));
			return false;
		}
		if (args.length == 0 || args.length == 1 || args.length > 3) {
			sender.sendMessage(this.help);
			return false;
		}
		if (args[0].equalsIgnoreCase("create")) {
			if (args.length < 3 || args.length > 3) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "/ladder create <name> <type>");
				return false;
			}
			if (fileConfig.contains("ladders." + args[1])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.ladder-already-exist")));
				return false;
			}
			if (LadderType.valueOf(args[2]) == null) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.ladder-type-doesnt-exist")));
				return false;
			}
			fileConfig.set("ladders", args[1]);
			fileConfig.set("ladders." + args[1], args[2]);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.created-ladder").replace("%ladderName%", args[1]).replace("%ladderType%", args[2])));
			return false;
		}
		return false;
	}

}
