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

	private Main main;
	
	public LocationsCommand(final Main main) { this.main = main; }
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		if (!sender.hasPermission(this.main.getConfig().getString("permissions.locations"))) {
			sender.sendMessage(this.main.getMessageLoader().getNoPermission());
			return false;
		}
		if (args.length > 1 || args.length < 1) {
			sender.sendMessage(ChatColor.RED + "/location set<spawn/editor>");
			return false;
		}
		if (args[0].contains("spawn") || args[0].contains("editor")) {
			this.main.getConfig().set((args[0].contains("spawn") ? "locations.spawn" : "locations.editor"), LocationSerializer.locationToString(LocationSerializer.fromBukkitLocation(Bukkit.getPlayer(sender.getName()).getLocation())));
			sender.sendMessage(ChatColor.GREEN + "The location " + ChatColor.WHITE + (args[0].contains("spawn") ? "spawn" : "editor") + ChatColor.GREEN + " has been set!");
		}
		return false;
	}

}
