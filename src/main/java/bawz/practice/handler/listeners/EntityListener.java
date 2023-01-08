package bawz.practice.handler.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import bawz.practice.Main;
import bawz.practice.profile.Profile;
import bawz.practice.profile.ProfileState;

public class EntityListener implements Listener {
	
	private final Main main = Main.getInstance();
	
	@EventHandler(priority=EventPriority.LOW)
	public void onDamage(final EntityDamageEvent event) {
		final Profile profile = this.main.getProfiles().get(event.getEntity().getUniqueId());
		if (event.getEntity() instanceof Player) {
			if (profile.getProfileState().equals(ProfileState.FIGHT)) {
				return;
			}	
		}
		event.setCancelled(true);
	}

	@EventHandler(priority=EventPriority.LOW)
	public void onDamageOnAnotherEntity(final EntityDamageByEntityEvent event) {
		final Profile profileDamaged = this.main.getProfiles().get(event.getEntity().getUniqueId());
		final Profile profileDamager = this.main.getProfiles().get(event.getDamager().getUniqueId());
		if (event.getEntity() instanceof Player) {
			if (profileDamaged.getProfileState().equals(ProfileState.FIGHT) && profileDamager.getProfileState().equals(ProfileState.FIGHT)) {
				return;
			}	
		}
		event.setCancelled(true);
	}
}
