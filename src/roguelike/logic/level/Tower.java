package roguelike.logic.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import roguelike.resources.Levels;

public class Tower {

	private List<Floor> floors;
	private int floorAt;
	
	/**Creates a tower with the floors int the Levels class
	 * @param randomizer - Used to shuffle the tower
	 */
	public Tower(Random randomizer) {
		this.floors = new ArrayList<Floor>();
		List<Floor> temporaryTower = new ArrayList<>();
		this.floorAt = 0;
		
		this.floors.add(Levels.BASE_LEVEL);
		temporaryTower.add(Levels.LEVEL_1);
		temporaryTower.add(Levels.LEVEL_2);
		temporaryTower.add(Levels.LEVEL_3);
		temporaryTower.add(Levels.LEVEL_4);
		temporaryTower.add(Levels.LEVEL_5);
		temporaryTower.add(Levels.LEVEL_6);
		temporaryTower.add(Levels.LEVEL_7);
		
		while(!temporaryTower.isEmpty()) {
			int choice = temporaryTower.size() == 1 ? 0 : randomizer.nextInt(temporaryTower.size()-1)+1;
			this.floors.add(temporaryTower.get(choice));
			temporaryTower.remove(choice);
		}
	}
	
	/**Gets a level from the levels ArrayList
	 * @param index - Corresponding index in ArrayList
	 * @return The requested Floor
	 */
	public Floor getFloor(int index) {
		return floors.get(index);
	}
	
	/**Gets the number of floors in the ArrayList*/
	public int getTowerHeight() {
		return floors.size();
	}
	
	/**Gets the index of the floor the player is on*/
	public int getFloorAt() {
		return floorAt;
	}
	
	/**Gets the level above the current one*/
	public Floor getNextFloor() {
		floorAt++;
		
		if(floorAt == floors.size())
			floorAt--;
		
		return floors.get(floorAt);
	}
	
	/**Gets the level below the current one*/
	public Floor getPreviousFloor() {
		if(floorAt != 0)
			floorAt--;
		
		return floors.get(floorAt);
	}
}