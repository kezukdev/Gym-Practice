package bawz.practice.utils.hook;

import org.bukkit.entity.*;

import bawz.practice.Main;
import bawz.practice.ladder.Ladder;

public class SpigotHook
{
    private Types spigotType;
    
    public SpigotHook() {
    	this.spigotType = Types.get();
    	Main.getInstance().getLogger().warning(spigotType.equals(Types.Default) ? "No hooked spigot" : spigotType.getName() + " has hooked!");
    }
    
    public void resetKnockback(final Player player) {
        this.spigotType.getKnockbackType().applyDefaultKnockback(player);
    }
    
    public void applyKitKnockback(final Player player, final Ladder kit) {
        this.spigotType.getKnockbackType().applyKitKnockback(player, kit);
    }
    
    public void applyKnockback(final Player player, final String name) {
        this.spigotType.getKnockbackType().applyKnockback(player, name);
    }
    
    public Types getSpigotType() {
        return this.spigotType;
    }
}
