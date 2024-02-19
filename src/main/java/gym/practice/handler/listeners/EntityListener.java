package gym.practice.handler.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import gym.practice.Main;
import gym.practice.match.MatchState;
import gym.practice.profile.Profile;
import gym.practice.profile.ProfileState;

public class EntityListener implements Listener {
	
	private Main main;
	
	public EntityListener(final Main main) { this.main = main; }
	
	@EventHandler(priority=EventPriority.LOW)
	public void onDamage(final EntityDamageEvent event) {
		final Profile profile = this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getEntity().getUniqueId());
		if (event.getEntity() instanceof Player) {
			if (profile.getProfileState().equals(ProfileState.FIGHT)) {
				return;
			}	
		}
		event.setCancelled(true);
	}

	@EventHandler(priority=EventPriority.LOW)
	public void onDamageOnAnotherEntity(final EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) return;
		final Profile profileDamaged = this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getEntity().getUniqueId());
		final Profile profileDamager = this.main.getManagerHandler().getProfileManager().getProfiles().get(event.getDamager().getUniqueId());
		if (event.getEntity() instanceof Player) {
			if (profileDamaged.getProfileState().equals(ProfileState.FIGHT) && profileDamager.getProfileState().equals(ProfileState.FIGHT) && (this.main.getManagerHandler().getMatchManager().getMatchs().get(profileDamager.getProfileCache().getMatchID()) != null && this.main.getManagerHandler().getMatchManager().getMatchs().get(profileDamager.getProfileCache().getMatchID()).getMatchState().equals(MatchState.PLAYING))) return;	
		}
		event.setCancelled(true);
	}
}
