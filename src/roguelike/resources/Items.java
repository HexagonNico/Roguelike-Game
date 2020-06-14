package roguelike.resources;

import roguelike.logic.items.Armor;
import roguelike.logic.items.Item;
import roguelike.logic.items.Weapon;

public class Items {

	public static final Item HP_POTION = new Item("hp_potion", "Health potion", "Restores 10 HP");
	public static final Item KEY = new Item("small_key", "Small key", "Can be used once to open a locked door");
	public static final Item STR_FRUIT = new Item("str_fruit", "Fruit of Strength", "Temporary increases strenght");
	public static final Item POISON = new Item("psn_potion", "Mysterious potion", "Its effect is unknown");
	public static final Item DEF_POTION = new Item("def_potion", "Mysterious potion", "Its effect is unknown");
	public static final Item MAX_POTION = new Item("max_potion", "Vitality potion", "Increases max HP");
	
	public static final Weapon SHORT_SWORD = new Weapon("short_sword", "Short Sword", 5, 10);
	public static final Weapon STICK = new Weapon("stick", "Stick", 3, 7);
	public static final Weapon AXE = new Weapon("axe", "Axe", 7, 8);
	public static final Weapon GREAT_SWORD = new Weapon("great_sword", "Great Sword", 9, 12);
	
	public static final Armor LIGHT_ARMOR = new Armor("light_armor", "Light Armor", 4, 10);
	public static final Armor BRONZE_ARMOR = new Armor("bronze_armor", "Bronze Armor", 5, 9);
	public static final Armor MEDIEVAL_ARMOR = new Armor("medieval_armor", "Medieval Armor", 7, 12);
	public static final Armor MISTERIOUS_ARMOR = new Armor("misterious_armor", "Misterious Armor", 9, 15);
}
