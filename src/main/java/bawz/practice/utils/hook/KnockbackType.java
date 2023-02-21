package bawz.practice.utils.hook;

import org.bukkit.entity.*;

import bawz.practice.ladder.Ladder;

public interface KnockbackType
{
    void applyKnockback(final Player p0, final String p1);
    
    void applyDefaultKnockback(final Player p0);
    
    void applyKitKnockback(final Player p0, final Ladder p1);
}
