package saturne.practice.utils.config;

import java.io.*;
import org.bukkit.configuration.file.*;
import org.bukkit.plugin.java.*;
import org.bukkit.*;
import java.util.*;

public class ConfigFile
{
    private File file;
    private YamlConfiguration configuration;
    
    public ConfigFile(final JavaPlugin plugin, final String name) {
        this.file = new File(plugin.getDataFolder(), name + ".yml");
        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdir();
        }
        plugin.saveResource(name + ".yml", false);
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }
    
    public double getDouble(final String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getDouble(path);
        }
        return 0.0;
    }
    
    public int getInt(final String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getInt(path);
        }
        return 0;
    }
    
    public boolean getBoolean(final String path) {
        return this.configuration.contains(path) && this.configuration.getBoolean(path);
    }
    
    public String getString(final String path) {
        if (this.configuration.contains(path)) {
            return ChatColor.translateAlternateColorCodes('&', this.configuration.getString(path));
        }
        return "ERROR: STRING NOT FOUND";
    }
    
    public String getString(final String path, final String callback, final boolean colorize) {
        if (!this.configuration.contains(path)) {
            return callback;
        }
        if (colorize) {
            return ChatColor.translateAlternateColorCodes('&', this.configuration.getString(path));
        }
        return this.configuration.getString(path);
    }
    
    public List<String> getReversedStringList(final String path) {
        final List<String> list = this.getStringList(path);
        if (list != null) {
            final int size = list.size();
            final List<String> toReturn = new ArrayList<String>();
            for (int i = size - 1; i >= 0; --i) {
                toReturn.add(list.get(i));
            }
            return toReturn;
        }
        return Arrays.asList("ERROR: STRING LIST NOT FOUND!");
    }
    
    public List<String> getStringList(final String path) {
        if (this.configuration.contains(path)) {
            final ArrayList<String> strings = new ArrayList<String>();
            for (final String string : this.configuration.getStringList(path)) {
                strings.add(ChatColor.translateAlternateColorCodes('&', string));
            }
            return strings;
        }
        return Arrays.asList("ERROR: STRING LIST NOT FOUND!");
    }
    
    public List<String> getStringListOrDefault(final String path, final List<String> toReturn) {
        if (this.configuration.contains(path)) {
            final ArrayList<String> strings = new ArrayList<String>();
            for (final String string : this.configuration.getStringList(path)) {
                strings.add(ChatColor.translateAlternateColorCodes('&', string));
            }
            return strings;
        }
        return toReturn;
    }
    
    public File getFile() {
        return this.file;
    }
    
    public YamlConfiguration getConfiguration() {
        return this.configuration;
    }
}
