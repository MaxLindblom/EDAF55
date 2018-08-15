package todo;
import done.*;

public class TimeThread extends Thread {
	AlarmClock clock;
	
	public TimeThread(AlarmClock clock) {
		this.clock = clock;
	}
	
	public void run() {
		long time = System.currentTimeMillis();
		long diff;
		while (true) {
			time += 1000;
			diff = time - System.currentTimeMillis();
			if (diff > 0) {
				try {
					sleep(diff);
				} catch (InterruptedException e) {
					
				}
				// One second has passed
				clock.tick();
			}
		}
	}
}
