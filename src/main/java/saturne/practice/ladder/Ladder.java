package saturne.practice.ladder;

import org.bukkit.inventory.ItemStack;
import saturne.practice.Main;

public class Ladder {

	private final Main main = Main.getInstance();
	
	private String displayName;
	private String name;
	private LadderType ladderType;
	private Integer slots;
	private ItemStack icon;
	private ItemStack[] content;
	private ItemStack[] armorContent;
	private boolean editable;
	
	public Ladder(final String name, final ItemStack icon, final ItemStack[] content, final ItemStack[] armorContent,final String displayName, final LadderType ladderType, final Integer slots, final boolean editable) {
		this.name = name;
		this.icon = icon;
		this.content = content;
		this.armorContent = armorContent;
		this.displayName = displayName;
		this.ladderType = ladderType;
		this.slots = slots;
		this.editable = editable;
		this.main.getLadders().add(this);
	}
	
	public ItemStack[] getArmorContent() {
		return armorContent;
	}
	
	public ItemStack[] getContent() {
		return content;
	}
	
	public ItemStack getIcon() {
		return icon;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isEditable() {
		return editable;
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
