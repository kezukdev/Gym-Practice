package bawz.practice.handler.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import bawz.practice.Main;
import net.md_5.bungee.api.ChatColor;

public class ServerListener implements Listener {
	
	private Main main;
	
	public ServerListener(final Main main) { this.main = main; }
	
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
