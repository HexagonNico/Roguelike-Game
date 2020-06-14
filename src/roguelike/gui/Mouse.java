package roguelike.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import roguelike.logic.GameLogic;

public class Mouse implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		GameLogic.handleLeftClick(arg0.getX(), arg0.getY());
	}
	
}
