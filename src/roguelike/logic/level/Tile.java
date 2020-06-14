package roguelike.logic.level;

public class Tile {

	private String name;
	
	protected int worldPosX;
	protected int worldPosY;
	
	private boolean collectible;
	
	/**Creates a Tile
	 * @param name - The registry name of the tile
	 * @param posX - X coordinate in the floor
	 * @param posY - Y coordinate in the floor
	 */
	public Tile(String name, int posX, int posY) {
		this.name = name;
		this.worldPosX = posX;
		this.worldPosY = posY;
		
		if(name == "red_potion" || name == "gold_bag" || name == "key" || name == "purple_potion" || name == "lime_potion" || name == "green_potion" || name == "yellow_potion" || name == "chest")
			this.collectible = true;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPosX() {
		return worldPosX;
	}
	
	public int getPosY() {
		return worldPosY;
	}
	
	public boolean isCollectible() {
		return collectible;
	}
}
