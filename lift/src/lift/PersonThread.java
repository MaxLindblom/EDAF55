package lift;

public class PersonThread extends Thread {
	private int start;
	private int target;
	Controller monitor;
	
	public PersonThread(Controller monitor) {
		this.monitor = monitor;
	}

	public void run() {
		while(true) {
			int delay = 1000*(( int )( Math.random ()*46.0));
			start = (int) (Math.random() * 7);
			target = (int) (Math.random() * 7);
			while(target == start) {
				target = (int) (Math.random() * 7);
			}
			try {
				sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			monitor.getIn(start, target);
			monitor.getOut(target);
		}
	}
}
