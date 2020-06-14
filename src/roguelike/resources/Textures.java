package roguelike.resources;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;

import javax.imageio.ImageIO;

public class Textures {

	private static HashMap<String, BufferedImage> sprites;
	
	/**Initializes texture HashMap and reads all files in res folder<br>
	 * Call in GameLogic#initializeGame*/
	public static void init() {
		sprites = new HashMap<String, BufferedImage>();
		
		File folder = new File("res/textures");
		
		for(File file : folder.listFiles()) {
			try {
				sprites.put(file.getName().replaceAll(".png", ""), ImageIO.read(file));
			} catch (IOException e) {
				System.err.println("[Utils][Textures]: Exception reading "+file.getName());
			}
		}
		
		System.out.println("[Utils][Textures]: Finished reading sprite files");
	}
	
	/**Gets a sprite from the sprite HashMap
	 * @param name - Corresponding name in HashMap
	 * @return - A BufferedImage representing the requested sprite
	 */
	public static BufferedImage getSprite(String name) {
		BufferedImage sprite = sprites.get(name);
		if(sprite != null) return sprite;
		else return sprites.get("error");
	}
}