package saturne.practice.handler.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import saturne.practice.Main;
import saturne.practice.utils.ConfigTranslator;

public class PlayerListener implements Listener {
	
	private Main main = Main.getInstance();
	
	@EventHandler(priority=EventPriority.HIGH)
	public void PlayerJoin(final PlayerJoinEvent event) {
		event.setJoinMessage(null);
		if (Boolean.valueOf(this.main.getConfig().getString("join-message.enabled"))) {
			String message = this.main.getConfig().getString("join-message.message");
			event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', message.replace(message, ConfigTranslator.translate(message)).replace("%player%", event.getPlayer().getDisplayName())));
		}
		this.main.getManagerHandler().getItemManager().giveItems(event.getPlayer(), "spawn-items");
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void PlayerLeave(final PlayerQuitEvent event) {
		event.setQuitMessage(null);
		if (Boolean.valueOf(this.main.getConfig().getString("leave-message.enabled"))) {
			String message = this.main.getConfig().getString("leave-message.message");
			event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', message.replace(message, ConfigTranslator.translate(message)).replace("%player%", event.getPlayer().getDisplayName())));
		}
	}

}
