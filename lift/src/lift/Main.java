package lift;

public class Main {

	public static void main(String[] args) {
		LiftView lv = new LiftView();
		Controller monitor = new Controller(lv);
		LiftThread lift = new LiftThread(monitor, lv);
		lift.start();
		PersonThread [] people = new PersonThread[20];
		for (int i = 0; i < 20; i++) {
			people[i] = new PersonThread(monitor);
			people[i].start();
		}
	}

}
