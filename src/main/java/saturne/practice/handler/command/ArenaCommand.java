package saturne.practice.handler.command;

import org.bukkit.command.*;
import org.bukkit.entity.*;

import net.md_5.bungee.api.ChatColor;
import saturne.practice.Main;
import saturne.practice.arena.Arena;
import saturne.practice.arena.ArenaType;
import saturne.practice.utils.LocationSerializer;
import org.bukkit.*;

public class ArenaCommand implements CommandExecutor {
	
	private final Main main = Main.getInstance();
	private final String[] help = this.main.getConfig().getConfigurationSection("messages").getStringList("arena-help").toArray(new String[this.main.getConfig().getConfigurationSection("messages").getStringList("ladder-help").size()]);
	
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) return false;
        if (!sender.hasPermission(this.main.getConfig().getString("permissions.arena"))) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.no-permissions")));
            return false;
        }
        if (args.length == 0) {
        	sender.sendMessage(this.help);
        	return false;
        }
        final Arena arena = this.main.getManagerHandler().getArenaManager().getArena(args[1]);
        if (args[0].equalsIgnoreCase("create")) {
        	if (args.length > 3 || args.length < 3) {
        		sender.sendMessage(ChatColor.RED + "/arena create <name> <type>");
        		return false;
        	}
        	if (arena != null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.arena-already-exist")));
                return false;
        	}
        	if (ArenaType.valueOf(args[2]) == null) {
        		sender.sendMessage(ChatColor.RED + "Please provide a good type!");
        		return false;
        	}
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.arena-created").replace("%arenaName%", args[1]).replace("%arenaType%", args[2])));
            this.main.getManagerHandler().getArenaManager().createArena(args[1]);
            final Location location = Bukkit.getServer().getPlayer(sender.getName()).getLocation();
            this.main.getManagerHandler().getArenaManager().getArena(args[1]).setLoc1(LocationSerializer.fromBukkitLocation(location));
            this.main.getManagerHandler().getArenaManager().getArena(args[1]).setLoc2(LocationSerializer.fromBukkitLocation(location));
            this.main.getManagerHandler().getArenaManager().getArena(args[1]).setArenaType(ArenaType.valueOf(args[2]));
            return false;
        }
        if (args[0].contains("setloc")) {
        	if (args.length > 2 || args.length < 2) {
        		sender.sendMessage(ChatColor.RED + "/arena setloc<1/2> <name>");
        		return false;
        	}
        	if (arena == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.arena-not-exist")));
                return false;
        	}
            final Location location = Bukkit.getServer().getPlayer(sender.getName()).getLocation();
        	if (args[0].contains("1")) {
                arena.setLoc1(LocationSerializer.fromBukkitLocation(location));
        	}
        	if (args[0].contains("2")) {
                arena.setLoc2(LocationSerializer.fromBukkitLocation(location));
        	}
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.arena-location").replace("%locationSet%", args[0].contains("1") ? "first" : "second")));
        }
        else {
        	sender.sendMessage(this.help);
        }
        return false;
    }
}