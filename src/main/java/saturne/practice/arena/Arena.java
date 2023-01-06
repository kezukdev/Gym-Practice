package saturne.practice.arena;

import org.bukkit.Location;

import saturne.practice.Main;

public class Arena {
	
	private final Main main = Main.getInstance();
	
	private String name;
	private Location firstCorner;
	private Location secondCorner;
	private Location locA;
	private Location locB;
	
	private ArenaType type;
	
	public Arena(final String name, final Location firstCorner, final Location secondCorner, final Location locA, final Location locB, final ArenaType type) {
		this.name = name;
		this.firstCorner = firstCorner;
		this.secondCorner = secondCorner;
		this.locA = locA;
		this.locB = locB;
		this.type = type;
		this.main.getArenas().add(this);
	}

	public String getName() {
		return name;
	}
	
	public Location getFirstCorner() {
		return firstCorner;
	}
	
	public Location getSecondCorner() {
		return secondCorner;
	}
	
	public Location getLocA() {
		return locA;
	}
	
	public Location getLocB() {
		return locB;
	}
	
	public ArenaType getType() {
		return type;
	}
}
