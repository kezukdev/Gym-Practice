package gym.practice.match.sub;

import java.util.UUID;

import gym.practice.Main;
import gym.practice.match.MatchEntry;

public class MatchStatistics {
	
	private Main main;
	
	private Long enderpearlCooldown;
	private Long nextHitTick;
	private MatchEntry matchEntry;
	
	public MatchStatistics(final UUID player, final UUID matchID, final Main main) {
		this.main = main;
		this.matchEntry = this.main.getManagerHandler().getMatchManager().getMatchs().get(matchID);
        this.enderpearlCooldown = 0L;
        this.nextHitTick = 0L;
		matchEntry.getMatchStatistics().putIfAbsent(player, this);
	}
	
	public boolean isEnderPearlCooldownActive() {
		return this.enderpearlCooldown > System.currentTimeMillis();
	}

	public long getEnderPearlCooldown() {
		return Math.max(0L, this.enderpearlCooldown - System.currentTimeMillis());
	}

	public void applyEnderPearlCooldown() {
		this.enderpearlCooldown = Long.valueOf(System.currentTimeMillis() + 16 * 1000);
	}

	public void removeEnderPearlCooldown() {
		this.enderpearlCooldown = 0L;
	}
	
    public Long getNextHitTick() {
        return this.nextHitTick;
    }
    
    public void updateNextHitTick() {
        this.nextHitTick = System.currentTimeMillis() + 500L;
    }

}
