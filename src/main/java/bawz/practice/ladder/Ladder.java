package bawz.practice.ladder;

import org.bukkit.inventory.ItemStack;

import bawz.practice.Main;

public class Ladder {

	private final Main main = Main.getInstance();
	
	private String displayName;
	private String name;
	private LadderType ladderType;
	private Integer slots;
	private ItemStack icon;
	private ItemStack[] content;
	private ItemStack[] armorContent;
	private boolean ranked;
	private boolean editable;
	private boolean cooldownPearl;
	private boolean knockbackProfile;
	private String knockbackTypeProfile;
	private Integer id;
	
	public Ladder(final String name, final ItemStack icon, final ItemStack[] content, final ItemStack[] armorContent,final String displayName, final LadderType ladderType, final Integer slots, final boolean editable, final boolean ranked, final boolean cooldownPearl, final boolean knockbackProfile, final String knockbackTypeProfile) {
		this.name = name;
		this.icon = icon;
		this.content = content;
		this.armorContent = armorContent;
		this.displayName = displayName;
		this.ladderType = ladderType;
		this.slots = slots;
		this.editable = editable;
		this.ranked = ranked;
		this.cooldownPearl = cooldownPearl;
		this.knockbackProfile = knockbackProfile;
		this.knockbackTypeProfile = knockbackTypeProfile == null ? "default" : knockbackTypeProfile;
		this.id = this.main.getLadders().size() == 0 ? 0 : this.main.getLadders().size() + 1;
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
	
	public boolean isRanked() {
		return ranked;
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
	
	public Integer getId() {
		return id;
	}
	
	public boolean isCooldownPearl() {
		return cooldownPearl;
	}
	
	public String getKnockbackTypeProfile() {
		return knockbackTypeProfile;
	}
	
	public boolean isKnockbackProfile() {
		return knockbackProfile;
	}
	
    public static Ladder getLadderBySlots(Integer slots) {
        return Main.getInstance().getLadders().stream().filter(ladder -> ladder.getSlots().equals(slots)).findFirst().orElse(null);
    }
}
