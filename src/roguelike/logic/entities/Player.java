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
		inventory = new Item[INVENTORY_SIZE];
		inventoryOpen = false;
		weaponEquipped = Items.SHORT_SWORD;
		armorEquipped = Items.LIGHT_ARMOR;
		strength = 1;
		defence = gold = floors = strengthBuff = defenceBuff = 0;
	}
	
	@Override
	public void setPosition(int dirX, int dirY, boolean animated) {
		super.setPosition(dirX, dirY, animated);
		if(animated) {
			strengthBuff--;
			defenceBuff--;
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
		gold += amount;
	}
	
	public int getGold() {
		return gold;
	}
	
	public void addFloorCleared() {
		floors++;
	}
	
	public void subtractFloorCleared() {
		floors--;
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
		if(weaponEquipped != null) str += weaponEquipped.getDamage();
		return strengthBuff > 0 ? str + 5 : str;
	}
	
	public void addDefenceBuff() {
		this.defenceBuff = 50;
	}
	
	@Override
	public int getDefence() {
		int def = super.getDefence();
		if(armorEquipped != null) def += armorEquipped.getDefence();
		return defenceBuff > 0 ? def + 5 : def;
	}
	
	public void equipWeapon(Weapon weapon) {
		weaponEquipped = new Weapon(weapon.getName(), weapon.getDisplayName(), weapon.getDamage(), weapon.getTotalDcy());
	}
	
	public void equipArmor(Armor armor) {
		armorEquipped = new Armor(armor.getName(), armor.getDisplayName(), armor.getDefence(), armor.getTotalDcy());
	}

	public Weapon getWeapon() {
		return weaponEquipped;
	}

	public Armor getArmor() {
		return armorEquipped;
	}

	public boolean damageArmor() {
		if (armorEquipped != null) {
			armorEquipped.reduceDcy();
			if (armorEquipped.getDcy() <= 0) {
				armorEquipped = null;
				return true;
			}
		}
		return false;
	}
	
	public boolean damageWeapon() {
		if (weaponEquipped != null) {
			weaponEquipped.reduceDcy();
			if (weaponEquipped.getDcy() <= 0) {
				weaponEquipped = null;
				return true;
			}
		}
		return false;
	}
}
