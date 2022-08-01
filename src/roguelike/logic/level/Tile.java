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
		worldPosX = posX;
		worldPosY = posY;
		
		if(name.equals("red_potion") || name.equals("gold_bag") || name.equals("key") || name.equals("purple_potion") || name.equals("lime_potion") || name.equals("green_potion") || name.equals("yellow_potion") || name.equals("chest"))
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
