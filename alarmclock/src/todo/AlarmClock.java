package todo;
import done.*;
import se.lth.cs.realtime.semaphore.MutexSem;
import se.lth.cs.realtime.semaphore.Semaphore;

/**
 * Main class of alarm-clock application.
 * Constructor providing access to IO.
 * Method start corresponding to main,
 * with closing down done in terminate.
 */
public class AlarmClock {

	private ClockInput	input;
	private ClockOutput	output;
	private Semaphore	signal;
	private int time;
	private Thread edit;
	private int alarmTime;
	private MutexSem mutex;
	private int alarmCounter;
	private boolean alarmOn;
	// Declare thread objects here..

	/**
	 * Create main application and bind attributes to device drivers.
	 * @param i The input from simulator/emulator/hardware.
	 * @param o Dito for output.
	 */
	public AlarmClock(ClockInput i, ClockOutput o) {
		input = i;
		output = o;
		signal = input.getSemaphoreInstance();
		mutex = new MutexSem();
		time = 1000000;
		alarmTime = 0;
		alarmCounter = 0;
		alarmOn = false;
	}

	/**
	 * Tell threads to terminate and wait until they are dead.
	 */
	public void terminate() {
		// Do something more clever here...
		output.console("AlarmClock exit.");
	}
	
	/**
	 * Create thread objects, and start threads
	 */
	public void start() {
		// Delete/replace the following test/demo code;
		// make something happen by exercising the IO:

		// Create thread objects here...
		Thread time = new Thread(new TimeThread(this));
		Thread edit = new Thread(new EditThread(input, this));
		
		// Create threads of execution by calling start...
		time.start();
		edit.start();
	}
	
	public void tick() {
		// Control what happens each tick
		mutex.take();
		time++;
		int hhmmss = time % 1000000;
		int h = (hhmmss / 10000) - 100;
		int m = ((hhmmss / 100) - 10000) - h * 100;
		int s = (hhmmss % 100);
		if (s >= 60) {
			s = 0;
			m++;
		}
		if (m >= 60) {
			m = 0;
			h++;
		}
		if (h >= 24) {
			h = 0;
		}
		hhmmss = (h * 10000) + (m * 100) + s;
		time = 1000000 + hhmmss;
		output.showTime(time);
		output.console(String.valueOf(time));
		if (time == alarmTime && input.getAlarmFlag()) {
			alarmOn = true;
		}
		if (alarmOn && alarmCounter < 20) {
			output.doAlarm();
			alarmCounter++;
		} else {
			alarmOn = false;
			alarmCounter = 0;
		}
		mutex.give();
	}
	
	public void setAlarm(int hhmmss) {
		mutex.take();
		alarmTime = hhmmss;
		alarmOn = false;
		mutex.give();
	}
	
	public void setTime(int hhmmss) {
		mutex.take();
		time = 1000000 + hhmmss;
		alarmOn = false;
		mutex.give();
	}
	
	class InputOutputTest implements Runnable {
		public void run() {
			long curr; int time, mode; boolean flag;
			output.console("Click on GUI to obtain key presses!");
			while (!Thread.currentThread().isInterrupted()) {
				curr = System.currentTimeMillis();
				time = input.getValue();
				flag = input.getAlarmFlag();
				mode = input.getChoice();
				output.doAlarm();
				output.console(curr, time, flag, mode);
				if (time == 120000) break; // Swe: Bryter för middag
				signal.take();
			}
			output.console("IO-test terminated #");
		}

	}
	
}
