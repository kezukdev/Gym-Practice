package saturne.practice.ladder;

import saturne.practice.arena.ArenaType;

public enum LadderType {
	
	NORMAL(ArenaType.NORMAL),
	UHC(ArenaType.UHC),
	SPLEEF(ArenaType.SPLEEF),
	BRIDGES(ArenaType.BRIDGES),
	HCF(ArenaType.HCF),
	SUMO(ArenaType.SUMO),
	COMBO(ArenaType.NORMAL);
	
	final ArenaType arenaType;
	
	LadderType(final ArenaType arenaType) {
		this.arenaType = arenaType;
	}
	
	public ArenaType getArenaType() {
		return arenaType;
	}

}
