package gym.practice.utils.hook.event;

import org.bukkit.entity.Player;

import gym.practice.ladder.Ladder;
import gym.practice.utils.hook.KnockbackType;
import kezuk.bspigot.knockback.KnockbackModule;
import kezuk.bspigot.knockback.KnockbackProfile;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

public class BawzPaper implements KnockbackType{

    @Override
    public void applyKnockback(final Player player, final String string) {
        final KnockbackProfile knockbackProfile = KnockbackModule.INSTANCE.profiles.get(string);
        ((CraftPlayer)player).getHandle().setKnockbackProfile(knockbackProfile);
        ((CraftLivingEntity)player).getHandle().setKnockbackProfile(knockbackProfile);
    }
    
    @Override
    public void applyDefaultKnockback(final Player player) {
        final KnockbackProfile knockbackProfile = KnockbackModule.getDefault();
        ((CraftPlayer)player).getHandle().setKnockbackProfile(knockbackProfile);
        ((CraftLivingEntity)player).getHandle().setKnockbackProfile(knockbackProfile);
    }
    
    @Override
    public void applyKitKnockback(final Player player, final Ladder kit) {
        KnockbackProfile knockbackProfile = KnockbackModule.getDefault();
        if (kit.getKnockbackTypeProfile() != null && KnockbackModule.INSTANCE.profiles.containsKey(kit.getKnockbackTypeProfile())) {
            knockbackProfile = KnockbackModule.INSTANCE.profiles.get(kit.getKnockbackTypeProfile());
        }
        ((CraftPlayer)player).getHandle().setKnockbackProfile(knockbackProfile);
        ((CraftLivingEntity)player).getHandle().setKnockbackProfile(knockbackProfile);
    }

}
