package saturne.practice.utils;

import org.bukkit.Bukkit;

public class ConfigTranslator {
	
	public static String translate(final String message) {
		if (message.contains("%currentlyOnline%")) {
			message.replace("%currentlyOnline%", String.valueOf(Bukkit.getOnlinePlayers().size()));
		}
		if (message.contains("%maxSlots%")) {
			message.replace("%maxSlots%", String.valueOf(Bukkit.getServer().getMaxPlayers()));
		}
		return message;
	}

}
