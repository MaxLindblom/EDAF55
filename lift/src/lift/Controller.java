package lift;

public class Controller {
	int here; // If here !=next , here (floor number) tells from which floor
	// the lift is moving and next to which floor it is moving.
	int next; // If here ==next , the lift is standing still on the floor
	// given by here.
	int [] waitEntry ;// The number of persons waiting to enter the lift at the
	// various floors.
	int [] waitExit; // The number of persons (inside the lift) waiting to leave
	// the lift at the various floors.
	int load; // The number of people currently occupying the lift.
	public static final int MAX_CAPACITY = 4;
	public static final int FLOORS = 7;
	LiftView lv;

	public Controller(LiftView lv) {
		this.lv = lv;
		waitEntry = new int[FLOORS];
		waitExit = new int[FLOORS];
		here = 0;
		load = 0;
		next = 0;
	}
	
	synchronized void getIn(int start, int target) {
		waitEntry[start]++;
		lv.drawLevel(start, waitEntry[start]);
		while (!(here == next && here == start && load < MAX_CAPACITY) ) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		waitExit[target]++;
		waitEntry[start]--;
		load++;
		lv.drawLevel(start, waitEntry[start]);
		lv.drawLift(start, load);
		notifyAll();
	}
	
	synchronized void getOut(int target) {
		while (!(here == next && here == target)) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		waitExit[target]--;
		load--;
		lv.drawLift(target, load);
		notifyAll();
	}
	
	synchronized void arrive(int floor) {
		here = floor;
		notifyAll();
		while (waitExit[here] != 0 || (waitEntry[here] != 0 && load < MAX_CAPACITY)) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		notifyAll();
	}
	
	synchronized void leave(int start, int target) {
		here = start;
		next = target;
		notifyAll();
	}
}
