package roguelike.logic.text;

import java.util.ArrayList;
import java.util.List;

public class MessageBox {
	private List<String> messageQueue;
	private List<Integer> times;
	
	public MessageBox() {
		messageQueue = new ArrayList<>();
		times = new ArrayList<>();
	}
	
	public void addMessage(String text, int time) {
		messageQueue.add(text);
		times.add(time*10);
	}
	
	public String getMessage() {
		try {
			times.set(0, this.times.get(0)-1);
			if(times.get(0) <= 0) {
				times.remove(0);
				messageQueue.remove(0);
			}
			return messageQueue.get(0);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public int getTime() {
		return times.get(0);
	}
}
