package roguelike.logic.text;

import java.util.ArrayList;
import java.util.List;

public class MessageBox {

	private List<String> messageQueue;
	private List<Integer> times;
	
	public MessageBox() {
		this.messageQueue = new ArrayList<>();
		this.times = new ArrayList<>();
	}
	
	public void addMessage(String text, int time) {
		this.messageQueue.add(text);
		this.times.add(time*10);
	}
	
	public String getMessage() {
		try {
			this.times.set(0, this.times.get(0)-1);
			
			if(this.times.get(0) <= 0) {
				this.times.remove(0);
				this.messageQueue.remove(0);
			}
			
			return this.messageQueue.get(0);
			
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public int getTime() {
		return this.times.get(0);
	}
}
