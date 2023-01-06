package saturne.practice.handler.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import saturne.practice.Main;
import saturne.practice.ladder.LadderType;
import saturne.practice.utils.BukkitSerialization;

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
			fileConfig.createSection("ladders." + args[1] + ".type");
			fileConfig.set("ladders." + args[1] + ".type", args[2]);
			fileConfig.createSection("ladders." + args[1] + ".inventory");
			fileConfig.createSection("ladders." + args[1] + ".icon");
			fileConfig.createSection("ladders." + args[1] + ".displayname");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.created-ladder").replace("%ladderName%", args[1]).replace("%ladderType%", args[2])));
			return false;
		}
		final Player player = (Player) sender;
		if (args[0].equalsIgnoreCase("setinv")) {
			if (args.length < 2 || args.length > 2) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "/ladder setinv <name>");
				return false;
			}
			if (!fileConfig.contains("ladders." + args[1])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.ladder-doesnt-exist").replace("%ladderName%", args[1])));
				return false;
			}
			fileConfig.set("ladders." + args[1] + ".inventory", BukkitSerialization.itemStackArrayToBase64(player.getInventory().getContents()));
			sender.sendMessage(ChatColor.GREEN + "The inventory was correctly defined!");
		}
		if (args[0].equalsIgnoreCase("seticon")) {
			if (args.length < 2 || args.length > 2) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "/ladder seticon <name>");
				return false;
			}
			if (!fileConfig.contains("ladders." + args[1])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.ladder-doesnt-exist").replace("%ladderName%", args[1])));
				return false;
			}
			fileConfig.set("ladders." + args[1] + ".icon", player.getItemInHand().getType().toString());
			sender.sendMessage(ChatColor.GREEN + "The icon has been defined to " + player.getItemInHand().getType().toString());
		}
		if (args[0].equalsIgnoreCase("setdisplay")) {
			if (args.length < 3 || args.length > 3) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "/ladder setdisplay <name> <display>");
				return false;
			}
			if (!fileConfig.contains("ladders." + args[1])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.ladder-doesnt-exist").replace("%ladderName%", args[1])));
				return false;
			}
			fileConfig.set("ladders." + args[1] + ".displayName", args[2]);
			sender.sendMessage(ChatColor.GREEN + "The displayname has been edited to " + args[2] + "!");
		}
		return false;
	}

}
