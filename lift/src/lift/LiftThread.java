package lift;

public class LiftThread extends Thread {
	Controller monitor;
	LiftView lv;
	private int floor;
	
	public LiftThread(Controller monitor, LiftView lv) {
		this.lv = lv;
		this.monitor = monitor;
		floor = 0;
	}
	
	public void run() {
		while (true) {
			for (int i = 0; i < monitor.FLOORS - 1; i++) {
				monitor.leave(i, i+1);
				lv.moveLift(i, i+1);
				monitor.arrive(i+1);
			}
			for (int i = monitor.FLOORS - 1; i > 0; i--) {
				monitor.leave(i, i-1);
				lv.moveLift(i, i-1);
				monitor.arrive(i-1);
			}
		}
	}
}
