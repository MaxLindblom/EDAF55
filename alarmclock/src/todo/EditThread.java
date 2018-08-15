package todo;
import done.*;
import se.lth.cs.realtime.semaphore.Semaphore;

public class EditThread extends Thread{
	private AlarmClock clock;
	private ClockInput input;
	private Semaphore sem;
	
	public EditThread(ClockInput input, AlarmClock clock) {
		this.clock = clock;
		this.input = input;
		this.sem = input.getSemaphoreInstance();
	}
	
	public void run() {
		while (true) {
			sem.take();
			if (input.getChoice() == input.SET_ALARM) {
				clock.setAlarm(input.getValue());
			}
			if (input.getChoice() == input.SET_TIME) {
				clock.setTime(input.getValue());
			}
		}
	}

}
