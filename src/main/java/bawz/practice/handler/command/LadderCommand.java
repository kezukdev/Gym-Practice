package bawz.practice.handler.command;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import bawz.practice.Main;
import bawz.practice.ladder.Ladder;
import bawz.practice.ladder.LadderType;
import bawz.practice.utils.BukkitSerialization;
import net.md_5.bungee.api.ChatColor;

public class LadderCommand implements CommandExecutor {
	
	private final Main main = Main.getInstance();
	final FileConfiguration fileConfig = this.main.getLadderFile().getConfig();
	private final String[] help = this.main.getConfig().getConfigurationSection("messages").getStringList("ladder-help").toArray(new String[this.main.getConfig().getConfigurationSection("messages").getStringList("ladder-help").size()]);

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		final Player player = (Player) sender;
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
			final int slots = this.main.getLadders().isEmpty() ? 0 : this.main.getLadders().size();
			new Ladder(args[1], player.getItemInHand(), player.getInventory().getContents(), player.getInventory().getArmorContents(), "&a" + args[1], LadderType.valueOf(args[2]), slots, true);
			fileConfig.createSection("ladders." + args[1]);
			fileConfig.createSection("ladders." + args[1] + ".type");
			fileConfig.set("ladders." + args[1] + ".type", args[2]);
			fileConfig.createSection("ladders." + args[1] + ".content");
			fileConfig.set("ladders." + args[1] + ".content", BukkitSerialization.itemStackArrayToBase64(player.getInventory().getContents()));
			fileConfig.createSection("ladders." + args[1] + ".armorContent");
			fileConfig.set("ladders." + args[1] + ".armorContent", BukkitSerialization.itemStackArrayToBase64(player.getInventory().getArmorContents()));
			fileConfig.createSection("ladders." + args[1] + ".icon");
			fileConfig.set("ladders." + args[1] + ".icon", player.getItemInHand().getType().toString());
			fileConfig.createSection("ladders." + args[1] + ".slots");
			fileConfig.set("ladders." + args[1] + ".slots", slots);
			fileConfig.createSection("ladders." + args[1] + ".editable");
			fileConfig.set("ladders." + args[1] + ".editable", "true");
			fileConfig.createSection("ladders." + args[1] + ".displayname");
			fileConfig.set("ladders." + args[1] + ".displayname", "&a" + args[1]);
			this.save();
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.created-ladder").replace("%ladderName%", args[1]).replace("%ladderType%", args[2])));
			return false;
		}
		if (args[0].equalsIgnoreCase("setinv")) {
			if (args.length < 2 || args.length > 2) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "/ladder setinv <name>");
				return false;
			}
			if (!fileConfig.contains("ladders." + args[1])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.ladder-doesnt-exist").replace("%ladderName%", args[1])));
				return false;
			}
			fileConfig.set("ladders." + args[1] + ".content", BukkitSerialization.itemStackArrayToBase64(player.getInventory().getContents()));
			fileConfig.set("ladders." + args[1] + ".armorContent", BukkitSerialization.itemStackArrayToBase64(player.getInventory().getArmorContents()));
			this.save();
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
			this.save();
			sender.sendMessage(ChatColor.GREEN + "The icon has been defined to " + player.getItemInHand().getType().toString());
		}
		if (args[0].equalsIgnoreCase("setslots")) {
			if (args.length < 3 || args.length > 3) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "/ladder setslots <name> <number>");
				return false;
			}
			if (!fileConfig.contains("ladders." + args[1])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.ladder-doesnt-exist").replace("%ladderName%", args[1])));
				return false;
			}
			if (Integer.valueOf(args[2]) == null) {
				sender.sendMessage(ChatColor.RED + "Please provide a correct format!");
				return false;
			}
			fileConfig.set("ladders." + args[1] + ".slots", Integer.valueOf(args[2]));
			this.save();
			sender.sendMessage(ChatColor.GREEN + "The slots of " + args[1] + " has been defined to " + args[2]);
		}
		if (args[0].equalsIgnoreCase("seteditable")) {
			if (args.length < 3 || args.length > 3) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "/ladder seteditable <name> <true/false>");
				return false;
			}
			if (!fileConfig.contains("ladders." + args[1])) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.ladder-doesnt-exist").replace("%ladderName%", args[1])));
				return false;
			}
			if (Boolean.valueOf(args[2]) == null) {
				sender.sendMessage(ChatColor.RED + "Please provide is true or false!");
				return false;
			}
			fileConfig.set("ladders." + args[1] + ".editable", args[2]);
			sender.sendMessage(ChatColor.GREEN + "The alterable of " + args[1] + " has been defined to " + args[2]);
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
			fileConfig.set("ladders." + args[1] + ".displayname", args[2]);
			sender.sendMessage(ChatColor.GREEN + "The displayname has been edited to " + args[2] + "!");
		}
		return false;
	}

	private void save() {
		try {
			fileConfig.save(this.main.getLadderFile().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}