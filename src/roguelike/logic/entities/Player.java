package roguelike.logic.entities;

import roguelike.logic.items.Armor;
import roguelike.logic.items.Item;
import roguelike.logic.items.Weapon;
import roguelike.resources.Items;

public class Player extends EntityTile {

	public static final int INVENTORY_SIZE = 3;
	
	private Item[] inventory;
	private boolean inventoryOpen;
	
	private Weapon weaponEquipped;
	private Armor armorEquipped;

	private int gold;
	private int floors;
	
	private int strengthBuff;
	private int defenceBuff;
	
	public Player(String name, int posX, int posY) {
		super(name, posX, posY, 20);
		this.inventory = new Item[INVENTORY_SIZE];
		this.inventoryOpen = false;
		this.weaponEquipped = Items.SHORT_SWORD;
		this.armorEquipped = Items.LIGHT_ARMOR;
		this.strength = 1;
		this.defence = 0;
		this.gold = 0;
		this.floors = 0;
		this.strengthBuff = 0;
		this.defenceBuff = 0;
	}
	
	@Override
	public void setPosition(int dirX, int dirY, boolean animated) {
		super.setPosition(dirX, dirY, animated);
		if(animated) {
			this.strengthBuff--;
			this.defenceBuff--;
		}
	}
	
	/**Adds an items to the first empty slot
	 * @param item - The item to add
	 * @return True if the item was added, false if inventory is full
	 */
	public boolean giveItem(Item item) {
		for(int i=0;i<INVENTORY_SIZE;i++) {
			if(this.inventory[i] == null) {
				this.inventory[i] = item;
				return true;
			}
		}
		return false;
	}
	
	/**Removes an item from the player's inventory
	 * @param index - Inventory slot
	 */
	public void removeItem(int index) {
		try {
			this.inventory[index] = null;
		} catch(ArrayIndexOutOfBoundsException e) {
			return;
		}
	}
	
	public Item getInventoryItem(int index) {
		try {
			return inventory[index];
		} catch(ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public void setInventoryOpen(boolean inventoryOpen) {
		this.inventoryOpen = inventoryOpen;
	}
	
	public boolean isInventoryOpen() {
		return inventoryOpen;
	}
	
	public void giveGold(int amount) {
		this.gold += amount;
	}
	
	public int getGold() {
		return gold;
	}
	
	public void addFloorCleared() {
		this.floors++;
	}
	
	public void subtractFloorCleared() {
		this.floors--;
	}
	
	public int getFloorsCleared() {
		return floors;
	}
	
	public void increaseHealth(int amount) {
		this.maxHealth += amount;
		this.health += amount;
	}
	
	public void addStrengthBuff() {
		this.strengthBuff = 50;
	}
	
	@Override
	public int getStrength() {
		int str = super.getStrength();
		if(this.weaponEquipped != null) str += this.weaponEquipped.getDamage();
		return this.strengthBuff > 0 ? str + 5 : str;
	}
	
	public void addDefenceBuff() {
		this.defenceBuff = 50;
	}
	
	@Override
	public int getDefence() {
		int def = super.getDefence();
		if(this.armorEquipped != null) def += this.armorEquipped.getDefence();
		return this.defenceBuff > 0 ? def + 5 : def;
	}
	
	public void equipWeapon(Weapon weapon) {
		this.weaponEquipped = new Weapon(weapon.getName(), weapon.getDisplayName(), weapon.getDamage(), weapon.getTotalDcy());
	}
	
	public void equipArmor(Armor armor) {
		this.armorEquipped = new Armor(armor.getName(), armor.getDisplayName(), armor.getDefence(), armor.getTotalDcy());
	}

	public Weapon getWeapon() {
		return this.weaponEquipped;
	}

	public Armor getArmor() {
		return this.armorEquipped;
	}

	public boolean damageArmor() {
		if(this.armorEquipped == null) {
			return false;
		}
		else { 
			this.armorEquipped.reduceDcy();
		}
		if(this.armorEquipped.getDcy() <= 0) {
			this.armorEquipped = null;
			return true;
		}
		return false;
	}
	
	public boolean damageWeapon() {
		if(this.weaponEquipped == null) {
			return false;
		}
		else { 
			this.weaponEquipped.reduceDcy();
		}
		if(this.weaponEquipped.getDcy() <= 0) {
			this.weaponEquipped = null;
			return true;
		}
		return false;
	}
}
