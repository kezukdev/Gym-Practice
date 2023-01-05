package saturne.practice.handler.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import net.md_5.bungee.api.ChatColor;
import saturne.practice.Main;

public class ServerListener implements Listener {
	
	private final Main main = Main.getInstance();
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPing(final ServerListPingEvent event) {
		event.setMaxPlayers(this.main.getConfig().getInt("maxslots"));
		if (Bukkit.hasWhitelist()) {
			event.setMotd(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("motd.whitelist.1") + "\n" + this.main.getConfig().getString("motd.whitelist.2")));
			return;
		}
		event.setMotd(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("motd.unwhitelist.1") + "\n" + this.main.getConfig().getString("motd.unwhitelist.2")));
	}

}
