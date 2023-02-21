package bawz.practice.utils.hook.event;

import org.bukkit.entity.*;

import bawz.practice.ladder.Ladder;
import bawz.practice.utils.hook.KnockbackType;

public class Default implements KnockbackType
{
    @Override
    public void applyKnockback(final Player player, final String string) {
    }
    
    @Override
    public void applyDefaultKnockback(final Player player) {
    }
    
    @Override
    public void applyKitKnockback(final Player player, final Ladder kit) {
    }
}
