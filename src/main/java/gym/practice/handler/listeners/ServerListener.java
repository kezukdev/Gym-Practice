package gym.practice.handler.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import gym.practice.Main;

public class ServerListener implements Listener {
	
	private Main main;
	
	public ServerListener(final Main main) { this.main = main; }
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPing(final ServerListPingEvent event) {
		event.setMaxPlayers(this.main.getMessageLoader().getMaxSlots());
		if (Bukkit.hasWhitelist()) {
			event.setMotd(this.main.getMessageLoader().getMotdWhitelistOne() + "\n" + this.main.getMessageLoader().getMotdWhitelistTwo());
			return;
		}
		event.setMotd(this.main.getMessageLoader().getMotdUnwhitelistOne() + "\n" + this.main.getMessageLoader().getMotdUnwhitelistTwo());
	}

}
