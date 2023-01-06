package saturne.practice.ladder;

import saturne.practice.Main;

public class Ladder {

	private final Main main = Main.getInstance();
	
	private String displayName;
	private LadderType ladderType;
	private Integer slots;
	
	public Ladder(final String displayName, final LadderType ladderType, final Integer slots) {
		this.displayName = displayName;
		this.ladderType = ladderType;
		this.slots = slots;
		this.main.getLadders().add(this);
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public LadderType getLadderType() {
		return ladderType;
	}
	
	public Integer getSlots() {
		return slots;
	}
}
