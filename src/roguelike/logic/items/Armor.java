package roguelike.logic.items;

public class Armor extends Item {

	private int defence;
	private int totalDcy;
	private int dcy;
	
	public Armor(String name, String displayName, int defence, int dcy) {
		super(name, displayName, "Protection: "+defence+"     DCY: "+dcy+"/"+dcy);
		this.defence = defence;
		this.totalDcy = dcy;
		this.dcy = dcy;
	}

	public int getDefence() {
		return defence;
	}
	
	public int getDcy() {
		return dcy;
	}
	
	public int getTotalDcy() {
		return totalDcy;
	}
	
	public void reduceDcy() {
		dcy--;
		description = "DMG: "+defence+" DCY: "+dcy+"/"+totalDcy;
	}
}
