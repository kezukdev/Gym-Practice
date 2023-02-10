package bawz.practice.utils;

import org.bukkit.ChatColor;

public class StringUtils {
	
	public static String translate(final String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

}
