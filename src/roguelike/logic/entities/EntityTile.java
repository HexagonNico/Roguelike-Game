package roguelike.logic.entities;

import roguelike.logic.level.Tile;
import roguelike.utils.Direction;

public class EntityTile extends Tile {

	private Direction facing;
	
	protected int health;
	protected int maxHealth;
	protected int strength;
	protected int defence;
	
	private int motionOffsetX;
	private int motionOffsetY;
	
	public EntityTile(String name, int posX, int posY, int health) {
		super(name, posX, posY);
		this.facing = Direction.LEFT;
		this.maxHealth = health;
		this.health = health;
	}
	
	public void setPosition(int dirX, int dirY, boolean animated) {
		if(animated) {
			if(dirX > worldPosX) motionOffsetX = 32;
			else if(dirX < worldPosX) motionOffsetX = -32;
			else if(dirY > worldPosY) motionOffsetY = 32;
			else if(dirY < worldPosY) motionOffsetY = -32;
		}
		
		this.worldPosX = dirX;
		this.worldPosY = dirY;
	}
	
	public Direction getFacing() {
		return facing;
	}
	
	public void setFacing(Direction facing) {
		this.facing = facing;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public void heal(int amount) {
		this.health += amount;
		if(health>maxHealth)
			this.health = this.maxHealth;
	}
	
	public void damage(int amount) {
		if(amount <= 0) amount = 1;
		this.health -= amount;
	}
	
	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getDefence() {
		return defence;
	}

	public void setDefence(int defence) {
		this.defence = defence;
	}

	public int getMotionOffsetX() {
		return motionOffsetX;
	}
	
	public int getMotionOffsetY() {
		return motionOffsetY;
	}
	
	public void setMotionOffset(int motionOffsetX, int motionOffsetY) {
		this.motionOffsetX = motionOffsetX;
		this.motionOffsetY = motionOffsetY;
	}
	
	public void decreaseMotionOffset() {
		if(motionOffsetX > 0)
			motionOffsetX-=2;
		else if(motionOffsetX < 0)
			motionOffsetX+=2;
		
		if(motionOffsetY > 0)
			motionOffsetY-=2;
		else if(motionOffsetY < 0)
			motionOffsetY+=2;
	}
}
