package bawz.practice.arena;

import bawz.practice.Main;
import bawz.practice.utils.LocationSerializer;

public class Arena {
	
	private final Main main = Main.getInstance();
	
    private String name;
    private LocationSerializer loc1;
    private LocationSerializer loc2;
    private ArenaType arenaType;
    
    public Arena(final String name, final LocationSerializer loc1, final LocationSerializer loc2, final ArenaType arenaType) {
        this.name = name;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.arenaType = arenaType;
        this.main.getArenas().add(this);
    }
    
    public Arena(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public LocationSerializer getLoc1() {
        return this.loc1;
    }
    
    public void setLoc1(final LocationSerializer loc1) {
        this.loc1 = loc1;
    }
    
    public LocationSerializer getLoc2() {
        return this.loc2;
    }
    
    public void setLoc2(final LocationSerializer loc2) {
        this.loc2 = loc2;
    }
    
    public ArenaType getArenaType() {
		return arenaType;
	}
    
    public void setArenaType(ArenaType arenaType) {
		this.arenaType = arenaType;
	}
}
