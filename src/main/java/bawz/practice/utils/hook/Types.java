package bawz.practice.utils.hook;

import java.util.*;
import bawz.practice.utils.hook.event.*;

public enum Types
{
    Default("None", "none", (KnockbackType)new Default()), 
	BawzPaper("BawzPaper", "kezuk.bspigot.utils.DiscordWebhook", (KnockbackType)new BawzPaper());
    
    private final String name;
    private final String packageName;
    private final KnockbackType knockbackType;
    
    public static Types get() {
        return Arrays.stream(values()).filter(spigotType -> !spigotType.equals(Types.Default) && check(spigotType.getPackageName())).findFirst().orElse(Types.Default);
    }
    
    public static boolean check(final String string) {
        try {
            Class.forName(string);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getPackageName() {
        return this.packageName;
    }
    
    public KnockbackType getKnockbackType() {
        return this.knockbackType;
    }
    
    private Types(final String name, final String packageName, final KnockbackType knockbackType) {
        this.name = name;
        this.packageName = packageName;
        this.knockbackType = knockbackType;
    }
}
