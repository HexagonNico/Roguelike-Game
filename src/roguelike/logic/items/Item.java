package roguelike.logic.items;

public class Item {

	protected String name;
	protected String displayName;
	protected String description;
	
	public Item(String name, String displayName, String description) {
		this.name = name;
		this.displayName = displayName;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getDescription() {
		return description;
	}
}
