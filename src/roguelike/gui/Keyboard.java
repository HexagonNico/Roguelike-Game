package roguelike.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

	private static boolean[] keys;
	
	private static int delay;
	
	public Keyboard() {
		keys = new boolean[100]; //TODO - Right lenght?
		delay = 96;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		keys[arg0.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		keys[arg0.getKeyCode()] = false;
	}
	
	/**Checks if given key is being pressed on the keyboard*/
	public static boolean isKeyDown(int key) {
		if(keys[key] == true && delay <= 0) {
			delay = 96;
			return true;
		}
		else {
			delay--;
			return false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}
