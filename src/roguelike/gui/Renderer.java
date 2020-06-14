package roguelike.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import roguelike.logic.entities.EntityTile;
import roguelike.logic.entities.Monster;
import roguelike.logic.entities.Player;
import roguelike.logic.level.Floor;
import roguelike.logic.level.Tile;
import roguelike.logic.text.MessageBox;
import roguelike.resources.Textures;
import roguelike.utils.Direction;

public class Renderer {

	private int zoomLevel;

	public static final Rectangle inventory = new Rectangle(150, 50, 700, 500);
	
	public static final Rectangle inventorySlot1 = new Rectangle(510, 150, 330, 60);
	public static final Rectangle inventorySlot2 = new Rectangle(510, 220, 330, 60);
	public static final Rectangle inventorySlot3 = new Rectangle(510, 290, 330, 60);

	public static final Rectangle weaponSlot = new Rectangle(160, 150, 330, 60);
	public static final Rectangle armorSlot = new Rectangle(160, 220, 330, 60);

	public static final Rectangle messageBox = new Rectangle(200, 480, 600, 50);
	
	public Renderer() {
		this.zoomLevel = 2;
	}
	
	/**Renders player at fixed position on the screen
	 * @param graphics - Grahpics object
	 */
	public void renderPlayer(EntityTile playerData, Graphics graphics) {
		BufferedImage sprite = Textures.getSprite("player");
		
		if(playerData.getFacing() == Direction.RIGHT)
			sprite = mirrorImage(Textures.getSprite("player"));
		
		int drawPosX = (Window.WIDTH/2)-(sprite.getWidth()/2)*zoomLevel;
		int drawPosY = (Window.HEIGHT/2)-(sprite.getHeight()/2)*zoomLevel;
		graphics.drawImage(sprite, drawPosX, drawPosY, sprite.getWidth()*zoomLevel, sprite.getHeight()*zoomLevel, null);
	}
	
	/**Renders a floor at correct position
	 * @param floorData - The floor to render
	 * @param player - Needed to calculate camera offset
	 * @param graphics - Graphics object
	 */
	public void renderLevel(Floor floorData, Player player, Graphics graphics) {
		for(int y=0;y<floorData.getSizeY();y++) {
			for(int x=0;x<floorData.getSizeX();x++) {
				BufferedImage sprite = Textures.getSprite(floorData.getTileAt(x, y).getName());
				
				if(floorData.getTileAt(x, y).getName() == "wall" && y == floorData.getSizeY()-1)
					sprite = Textures.getSprite("wall2");
				else if(floorData.getTileAt(x, y).getName() == "wall" && (floorData.getTileAt(x, y+1).getName() != "wall"))
					sprite = Textures.getSprite("wall2");
				
				int drawPosX = calculateOffsetX(sprite, floorData.getTileAt(x, y), player);
				int drawPosY = calculateOffsetY(sprite, floorData.getTileAt(x, y), player);
				graphics.drawImage(sprite, drawPosX, drawPosY, sprite.getWidth()*zoomLevel, sprite.getHeight()*zoomLevel, null);
			}
		}
	}
	
	/**Renders all given monsters on the floor
	 * @param monsters - Monsters to render
	 * @param player - Needed to calculate the camera offset
	 * @param graphics - Graphics object
	 */
	public void renderMonsters(Monster[] monsters, Player player, Graphics graphics) {
		if(monsters == null) return;
		
		for(Monster monster : monsters) {
			BufferedImage sprite = Textures.getSprite(monster.getName());
			int drawPosX = calculateOffsetX(sprite, monster, player) - monster.getMotionOffsetX()*zoomLevel;
			int drawPosY = calculateOffsetY(sprite, monster, player) - monster.getMotionOffsetY()*zoomLevel;
			if(monster.getHealth() > 0)
				graphics.drawImage(sprite, drawPosX, drawPosY, sprite.getWidth()*zoomLevel, sprite.getHeight()*zoomLevel, null);
		}
	}
	
	private int calculateOffsetX(BufferedImage sprite, Tile tile, Player player) {
		return tile.getPosX()*sprite.getWidth()*zoomLevel + ((Window.WIDTH/2)-player.getPosX()*sprite.getWidth()*zoomLevel-(sprite.getWidth()/2)*zoomLevel)+player.getMotionOffsetX()*zoomLevel;
	}

	private int calculateOffsetY(BufferedImage sprite, Tile tile, Player player) {
		return tile.getPosY()*sprite.getHeight()*zoomLevel + ((Window.HEIGHT/2)-player.getPosY()*sprite.getHeight()*zoomLevel-(sprite.getHeight()/2)*zoomLevel)+player.getMotionOffsetY()*zoomLevel;
	}
	
	/**Renders the UI
	 * @param player - Used to know hp and inventory
	 * @param graphics - Graphics object
	 */
	public void renderUI(Player player, Floor floorData, Graphics2D graphics, Point mousePosition) {
		graphics.setColor(Color.BLACK);
		graphics.fillRoundRect(5, 5, 100, 150, 10, 10);
		graphics.setColor(Color.WHITE);
		graphics.drawRoundRect(5, 5, 100, 150, 10, 10);
		
		graphics.setFont(new Font("Dialog", Font.PLAIN, 20));
		graphics.drawString("- Player -", 10, 25);
		graphics.setFont(new Font("Dialog", Font.PLAIN, 12));
		graphics.drawString("HP: "+player.getHealth()+"/"+player.getMaxHealth(), 10, 45);
		graphics.drawString("STR: "+player.getStrength(), 10, 65);
		graphics.drawString("DEF: "+player.getDefence(), 10, 80);
		graphics.drawString("Gold: "+player.getGold(), 10, 100);
		graphics.drawString("Floors: "+player.getFloorsCleared(), 10, 120);
		
		for(int y=0;y<floorData.getSizeY();y++) {
			for(int x=0;x<floorData.getSizeX();x++) {
				if(floorData.getTileAt(x, y).isCollectible()) {
					int drawPosX = floorData.getTileAt(x, y).getPosX()*32*zoomLevel + ((Window.WIDTH/2)-player.getPosX()*32*zoomLevel-(32/2)*zoomLevel);
					int drawPosY = floorData.getTileAt(x, y).getPosY()*32*zoomLevel + ((Window.HEIGHT/2)-player.getPosY()*32*zoomLevel-(32/2)*zoomLevel);
					
					if((player.getPosX() == x-1 && player.getPosY() == y) || (player.getPosX() == x+1 && player.getPosY() == y) || (player.getPosX() == x && player.getPosY() == y-1) || (player.getPosX() == x && player.getPosY() == y+1)) {
						BufferedImage sprite = Textures.getSprite("E");
						graphics.drawImage(sprite, drawPosX+8*zoomLevel+player.getMotionOffsetX()*zoomLevel, drawPosY-8*zoomLevel+player.getMotionOffsetY()*zoomLevel, sprite.getWidth()*zoomLevel, sprite.getHeight()*zoomLevel, null);
					}
				}
			}
		}
		
		if(player.isInventoryOpen()) {
			//Inventory
			graphics.setColor(Color.BLACK);
			graphics.fillRoundRect(inventory.x, inventory.y, inventory.width, inventory.height, 10, 10);
			graphics.setColor(Color.WHITE);
			graphics.drawRoundRect(inventory.x, inventory.y, inventory.width, inventory.height, 10, 10);
			
			graphics.setFont(new Font("Dialog", Font.PLAIN, 40));
			graphics.drawString("- Inventory -", 160, 90);
			graphics.setFont(new Font("Dialog", Font.PLAIN, 20));
			graphics.drawString("HP: "+player.getHealth()+"/"+player.getMaxHealth()+"     STR: "+player.getStrength()+"   DEF: "+player.getDefence()+"     Gold: "+player.getGold(), 160, 120);
			
			//Inventory slots
			if(inventorySlot1.contains(mousePosition))
				graphics.setStroke(new BasicStroke(3));
			
			graphics.drawRoundRect(inventorySlot1.x, inventorySlot1.y, inventorySlot1.width, inventorySlot1.height, 10, 10);
			graphics.drawRoundRect(inventorySlot1.x, inventorySlot1.y, 60, inventorySlot1.height, 10, 10);
			
			graphics.setStroke(new BasicStroke(1));
			
			if(inventorySlot2.contains(mousePosition))
				graphics.setStroke(new BasicStroke(3));
			
			graphics.drawRoundRect(inventorySlot2.x, inventorySlot2.y, inventorySlot2.width, inventorySlot2.height, 10, 10);
			graphics.drawRoundRect(inventorySlot2.x, inventorySlot2.y, 60, inventorySlot2.height, 10, 10);
			
			graphics.setStroke(new BasicStroke(1));
			
			if(inventorySlot3.contains(mousePosition))
				graphics.setStroke(new BasicStroke(3));
			
			graphics.drawRoundRect(inventorySlot3.x, inventorySlot3.y, inventorySlot3.width, inventorySlot3.height, 10, 10);
			graphics.drawRoundRect(inventorySlot3.x, inventorySlot3.y, 60, inventorySlot3.height, 10, 10);
			
			graphics.setStroke(new BasicStroke(1));
			
			graphics.drawRoundRect(weaponSlot.x, weaponSlot.y, weaponSlot.width, weaponSlot.height, 10, 10);
			graphics.drawRoundRect(weaponSlot.x, weaponSlot.y, 60, weaponSlot.height, 10, 10);
			
			if(player.getWeapon() != null) {
				BufferedImage sprite = Textures.getSprite(player.getWeapon().getName());
				graphics.drawImage(sprite, weaponSlot.x+7, weaponSlot.y+7, sprite.getWidth()*3, sprite.getHeight()*3, null);
				graphics.setFont(new Font("Dialog", Font.PLAIN, 20));
				graphics.drawString(player.getWeapon().getDisplayName(), weaponSlot.x+65, weaponSlot.y+22);
				graphics.setFont(new Font("Dialog", Font.PLAIN, 15));
				graphics.drawString(player.getWeapon().getDescription(), weaponSlot.x+65, weaponSlot.y+48);
			}
			
			graphics.drawRoundRect(armorSlot.x, armorSlot.y, armorSlot.width, armorSlot.height, 10, 10);
			graphics.drawRoundRect(armorSlot.x, armorSlot.y, 60, armorSlot.height, 10, 10);
			
			if(player.getArmor() != null) {
				BufferedImage sprite = Textures.getSprite(player.getArmor().getName());
				graphics.drawImage(sprite, armorSlot.x+7, armorSlot.y+7, sprite.getWidth()*3, sprite.getHeight()*3, null);
				graphics.setFont(new Font("Dialog", Font.PLAIN, 20));
				graphics.drawString(player.getArmor().getDisplayName(), armorSlot.x+65, armorSlot.y+22);
				graphics.setFont(new Font("Dialog", Font.PLAIN, 15));
				graphics.drawString(player.getArmor().getDescription(), armorSlot.x+65, armorSlot.y+48);
			}
			
			//Inventory items
			for(int i=0;i<Player.INVENTORY_SIZE;i++) {
				if(player.getInventoryItem(i) != null) {
					BufferedImage sprite = Textures.getSprite(player.getInventoryItem(i).getName());
					graphics.drawImage(sprite, inventorySlot1.x+7, 157+i*70, sprite.getWidth()*3, sprite.getHeight()*3, null);
					
					graphics.setFont(new Font("Dialog", Font.PLAIN, 20));
					graphics.drawString(player.getInventoryItem(i).getDisplayName(), inventorySlot1.x+65, 170+i*70);
					graphics.setFont(new Font("Dialog", Font.PLAIN, 15));
					graphics.drawString(player.getInventoryItem(i).getDescription(), inventorySlot1.x+65, 195+i*70);
				}
			}
		}
		
		//Death screen
		if(player.getHealth() <= 0) {
			graphics.setColor(Color.BLACK);
			graphics.fillRoundRect(inventory.x, inventory.y, inventory.width, inventory.height, 10, 10);
			graphics.setColor(Color.WHITE);
			graphics.drawRoundRect(inventory.x, inventory.y, inventory.width, inventory.height, 10, 10);

			graphics.setFont(new Font("Dialog", Font.PLAIN, 40));
			graphics.drawString("You perished in the dungeon...", 200, 130);
			
			graphics.setFont(new Font("Dialog", Font.PLAIN, 30));
			graphics.drawString("Floors cleared: "+player.getFloorsCleared(), 200, 200);
			graphics.drawString("Gold obtained: "+player.getGold(), 200, 240);
			graphics.drawString("Click to restart game", 200, 350);
		}
	}
	
	/**Renders the messagebox at the bottom of the screen
	 * @param text - The message box test 
	 * @param time - Message box time. If time<=0 message box isn't shown
	 * @param graphics - Graphics object
	 */
	public void renderMessageBox(MessageBox message, Graphics graphics) {
		if(message.getMessage() == null || message.getTime() <= 0)
			return;
		
		graphics.setColor(Color.BLACK);
		graphics.fillRoundRect(messageBox.x, messageBox.y, messageBox.width, messageBox.height, 10, 10);
		graphics.setColor(Color.WHITE);
		graphics.drawRoundRect(messageBox.x, messageBox.y, messageBox.width, messageBox.height, 10, 10);

		graphics.setFont(new Font("Dialog", Font.PLAIN, 20));
		
		//Center text
		try {
			int textPosX = messageBox.x + (messageBox.width - graphics.getFontMetrics().stringWidth(message.getMessage())) / 2;
			int textPosY = messageBox.y + ((messageBox.height - graphics.getFontMetrics().getHeight()) / 2) + graphics.getFontMetrics().getAscent();		
			graphics.drawString(message.getMessage(), textPosX, textPosY);
		} catch(NullPointerException e) {
			return;
		}
	}
	
	/**If the level is applies lights effects
	 * @param floorData - The floor to render
	 * @param player - Needed to calculate camera offset
	 * @param graphics - Graphics object
	 */
	public void renderLight(Floor floorData, Player player, Graphics2D graphics) {
		if(floorData.isDark()) {
			for(int y=0;y<floorData.getSizeY();y++) {
				for(int x=0;x<floorData.getSizeX();x++) {
					BufferedImage sprite = null;
					try {
						if(floorData.getTileAt(x, y).getName() == "torch")
							continue;
						else if(floorData.getTileAt(x-1, y).getName() == "torch")
							sprite = Textures.getSprite("dark_right");
						else if(floorData.getTileAt(x+1, y).getName() == "torch")
							sprite = Textures.getSprite("dark_left");
						else if(floorData.getTileAt(x, y-1).getName() == "torch")
							sprite = Textures.getSprite("dark_down");
						else if(floorData.getTileAt(x, y+1).getName() == "torch")
							sprite = Textures.getSprite("dark_up");
						else if(floorData.getTileAt(x-1, y-1).getName() == "torch")
							sprite = Textures.getSprite("dark_down_right");
						else if(floorData.getTileAt(x+1, y+1).getName() == "torch")
							sprite = Textures.getSprite("dark_up_left");
						else if(floorData.getTileAt(x+1, y-1).getName() == "torch")
							sprite = Textures.getSprite("dark_down_left");
						else if(floorData.getTileAt(x-1, y+1).getName() == "torch")
							sprite = Textures.getSprite("dark_up_right");
						else
							sprite = Textures.getSprite("dark");
					} catch(ArrayIndexOutOfBoundsException e) {
						sprite = Textures.getSprite("dark");
					}
					
					int drawPosX = calculateOffsetX(sprite, floorData.getTileAt(x, y), player);
					int drawPosY = calculateOffsetY(sprite, floorData.getTileAt(x, y), player);
					
					AlphaComposite original = (AlphaComposite) graphics.getComposite();
					graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
					graphics.drawImage(sprite, drawPosX, drawPosY, sprite.getWidth()*zoomLevel, sprite.getHeight()*zoomLevel, null);
					graphics.setComposite(original);
				}
			}
		}
	}
	
	public void renderTitleScreen(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		graphics.drawRoundRect(50, 50, Window.WIDTH-150, Window.HEIGHT-150, 10, 10);
		graphics.setFont(new Font("Dialog", Font.PLAIN, 40));
		graphics.drawString("Roguelike Tower", 100, 100);
		graphics.setFont(new Font("Dialog", Font.PLAIN, 20));
		graphics.drawString("Greetings chosen,", 100, 150);
		graphics.drawString("I've been waiting here for you since the beginning of this universe...", 100, 180);
		graphics.drawString("You know the world is fading...", 100, 210);
		graphics.drawString("We need you to find the misterious artifact on the top of this tower,", 100, 240);
		graphics.drawString("Then you will be able to use its power to save us", 100, 260);
		graphics.drawString("We all hope you will succeed in your venture", 100, 290);
		graphics.drawString("Press any button...", 200, 350);
	}
	
	private BufferedImage mirrorImage(BufferedImage image) {
		int h = image.getHeight();
		int w = image.getWidth();
		BufferedImage rotated = new BufferedImage(h, w, image.getType());
		
		for(int x=0;x<w;x++) {
			for(int y=0;y<h;y++) {
				rotated.setRGB(x, y, image.getRGB(w-1-x, y));
			}
		}
		return rotated;
	}
}
