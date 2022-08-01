package roguelike.logic.level;

import java.util.ArrayList;
import java.util.List;

import roguelike.logic.entities.Monster;

public class Floor {
	private Tile[][] floor;

	/**
	 * Posicion de arranque en el eje x
	 */
	private int startPosX;

	/**
	 * Posicion de arranque en el eje y
	 */
	private int startPosY;
	
	private List<Monster> monsters;
	
	private boolean dark;
	
	public Floor(String[] levelData, int startPosX, int startPosY, Monster... monsters) {
		this(levelData, startPosX, startPosY, false, monsters);
	}
	
	public Floor(String[] levelData, int startPosX, int startPosY, boolean isDark, Monster... monsters) {
		floor = new Tile[levelData.length][];
		for(int y=0;y<levelData.length;y++) {
			floor[y] = new Tile[levelData[y].length()];
			for(int x=0;x<levelData[y].length();x++) {
				switch (levelData[y].charAt(x)) {
					case '#' -> floor[y][x] = new Tile("wall", x, y);
					case '.' -> floor[y][x] = new Tile("floor", x, y);
					case '^' -> floor[y][x] = new Tile("stairs", x, y);
					case ',' -> floor[y][x] = new Tile("trap", x, y);
					case 'p' -> floor[y][x] = new Tile("red_potion", x, y);
					case 't' -> floor[y][x] = new Tile("table", x, y);
					case 'G' -> floor[y][x] = new Tile("gold_bag", x, y);
					case '!' -> floor[y][x] = new Tile("key", x, y);
					case 's' -> floor[y][x] = new Tile("purple_potion", x, y);
					case 'v' -> floor[y][x] = new Tile("green_potion", x, y);
					case 'd' -> floor[y][x] = new Tile("lime_potion", x, y);
					case 'c' -> floor[y][x] = new Tile("drawer", x, y);
					case 'g' -> floor[y][x] = new Tile("yellow_potion", x, y);
					case 'e' -> floor[y][x] = new Tile("table2", x, y);
					case '/' -> floor[y][x] = new Tile("locked_door", x, y);
					case 'T' -> floor[y][x] = new Tile("chest", x, y);
					case 'l' -> floor[y][x] = new Tile("torch", x, y);
				}
			}
		}
		
		this.startPosX = startPosX;
		this.startPosY = startPosY;
		
		this.monsters = new ArrayList<Monster>();
		for(Monster one : monsters) {
			this.monsters.add(one);
		}
		
		this.dark = isDark;
	}
	
	public Floor(Floor copy) {
		floor = new Tile[copy.getSizeY()][];
		for(int y=0;y<copy.getSizeY();y++) {
			floor[y] = new Tile[copy.getSizeX()];
			for(int x=0;x<copy.getSizeX();x++) {
				floor[y][x] = new Tile(copy.getTileAt(x, y).getName(), x, y);
			}
		}
		
		startPosX = copy.getStartPosX();
		startPosY = copy.getStartPosY();
		monsters = copy.getMonstersList();
		dark = copy.isDark();
	}
	
	public int getSizeX() {
		return floor[0].length;
	}
	
	public int getSizeY() {
		return floor.length;
	}
	
	public Tile getTileAt(int x, int y) {
		return floor[y][x];
	}
	
	public int getStartPosX() {
		return startPosX;
	}
	
	public int getStartPosY() {
		return startPosY;
	}
	
	public Monster[] getMonsters() {
		Monster[] other = new Monster[monsters.size()];
		return monsters.toArray(other);
	}
	
	private List<Monster> getMonstersList(){
		return monsters;
	}
	
	public Monster getMonsterAt(int x, int y) {
		for(Monster monster : monsters) {
			if(monster == null)
				return null;
			if(monster.getPosX() == x && monster.getPosY() == y)
				return monster;
		}
		return null;
	}
	
	public boolean isDark() {
		return dark;
	}
	
	/**Turns a trap tile into a floor tile
	 * @param x - X coordinate of trap
	 * @param y - Y coordinate of trap
	 * @return True if a trap at (x;y) was disarmed, false if there was no trap there
	 */
	public boolean disarmTrap(int x, int y) {
		if(floor[y][x].getName().equals("trap")) {
			floor[y][x] = new Tile("floor", x, y);
			return true;
		}
		return false;
	}
	
	/**Turns a collectible tile into a normal tile
	 * @param x - X coordinate of collectible
	 * @param y - Y coordinate of collectible
	 * @return True if a collectible at (x;y) was removed, false if there was no collectible there
	 */
	public boolean removeCollectible(int x, int y) {
		switch (floor[y][x].getName()) {
			case "red_potion", "key", "lime_potion", "green_potion" -> floor[y][x] = new Tile("table", x, y);
			case "gold_bag", "purple_potion" -> floor[y][x] = new Tile("drawer", x, y);
			case "yellow_potion" -> floor[y][x] = new Tile("table2", x, y);
			case "chest" -> floor[y][x] = new Tile("open_chest", x, y);
			default -> {
				return false;
			}
		}
		return true;
	}
	
	/**Turns a door tile into a floor tile
	 * @param x - X coordinate of door
	 * @param y - Y coordinate of door
	 */
	public void openDoor(int x, int y) {
		if(floor[y][x].getName().equals("locked_door"))
			floor[y][x] = new Tile("floor", x, y);
	}
	
	/**
	 * Deletes a monster
	 *
	 * @param x - X coordinate of monster
	 * @param y - Y coordinate of monster
	 */
	public void killMonster(int x, int y) {
		for(int i=0;i<monsters.size();i++) {
			if(monsters.get(i).getPosX() == x && monsters.get(i).getPosY() == y) {
				monsters.remove(i);
				System.out.println("[GameLogic][Floor]: Monster killed");
				return;
			}
		}
	}


	
	public boolean thereIsMonsterHere(int x, int y) {
		for (Monster monster : monsters) {
			if (monster.getPosX() == x && monster.getPosY() == y)
				return true;
		}
		return false;
	}
}
