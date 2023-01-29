package bawz.practice.handler.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bawz.practice.Main;
import bawz.practice.utils.LocationSerializer;
import net.md_5.bungee.api.ChatColor;

public class LocationsCommand implements CommandExecutor {

	private final Main main = Main.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		if (!sender.hasPermission(this.main.getConfig().getString("permissions.locations"))) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.no-permissions")));
			return false;
		}
		if (args.length > 1 || args.length < 1) {
			sender.sendMessage(ChatColor.RED + "/location set<spawn/editor>");
			return false;
		}
		if (args[0].contains("spawn") || args[0].contains("editor")) {
			LocationSerializer location = LocationSerializer.fromBukkitLocation(Bukkit.getPlayer(sender.getName()).getLocation());
			String loc = LocationSerializer.locationToString(location);
			String bool = args[0].contains("spawn") ? "locations.spawn" : "locations.editor";
			this.main.getConfig().set(bool, loc);
			sender.sendMessage(ChatColor.GREEN + "The location " + ChatColor.WHITE + (args[0].contains("spawn") ? "spawn" : "editor") + ChatColor.GREEN + " has been set!");
		}
		return false;
	}

}
