package bawz.practice.utils.hook.event;

import org.bukkit.entity.*;

import bawz.practice.ladder.Ladder;
import bawz.practice.utils.hook.KnockbackType;
import us.saturne.spigot.knockback.KnockbackProfile;

public class SaturneSpigot implements KnockbackType
{
    @Override
    public void applyKnockback(final Player player, final String string) {
        final KnockbackProfile knockbackProfile = us.saturne.spigot.SaturneSpigot.INSTANCE.getProfile(string);
        player.setKnockbackProfile(knockbackProfile);
    }
    
    @Override
    public void applyDefaultKnockback(final Player player) {
        final KnockbackProfile knockbackProfile = us.saturne.spigot.SaturneSpigot.INSTANCE.getKnockbackHandler().getActiveProfile();
        player.setKnockbackProfile(knockbackProfile);
    }
    
    @Override
    public void applyKitKnockback(final Player player, final Ladder kit) {
        KnockbackProfile knockbackProfile = us.saturne.spigot.SaturneSpigot.INSTANCE.getKnockbackHandler().getActiveProfile();
        if (kit.getKnockbackTypeProfile() != null && us.saturne.spigot.SaturneSpigot.INSTANCE.getKnockbackHandler().profileExists(kit.getKnockbackTypeProfile())) {
            knockbackProfile = us.saturne.spigot.SaturneSpigot.INSTANCE.getKnockbackHandler().getProfileByName(kit.getKnockbackTypeProfile()).get();
        }
        player.setKnockbackProfile(knockbackProfile);
    }
}
