package roguelike;

import roguelike.gui.Window;
import roguelike.logic.GameLogic;

public class Main {

	public static void main(String[] args) {
		try {
			
			System.out.println("[Main]: Starting...");
			
			Window.create();
			GameLogic.startGame();
			Window.setVisible();
			
			System.out.println("[Main]: Started!");
			
		} catch(Exception e) {
			System.err.println("\n[Main]: Uncaught exception in initialization!\n");
			e.printStackTrace();
			System.exit(-1);
		}
	}
}