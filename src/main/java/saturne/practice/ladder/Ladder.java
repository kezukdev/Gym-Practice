package saturne.practice.ladder;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import com.google.common.collect.Lists;

import saturne.practice.Main;

public class Ladder {

	private final Main main = Main.getInstance();
	
	private String displayName;
	private String name;
	private LadderType ladderType;
	private Integer slots;
	private ItemStack icon;
	private List<ItemStack> inventory = Lists.newArrayList();
	private boolean editable;
	
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
