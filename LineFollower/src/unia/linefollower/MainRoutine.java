package unia.linefollower;

import lejos.nxt.Button;
import lejos.nxt.Motor;

public class MainRoutine {
	enum Mode {
		ARC, NORMAL, SLICED
	}
	
	public static void main(String[] args) {
		Mode mode = Mode.SLICED;
		switch (mode) {
		case ARC:
			runArcTest();
			break;
		case NORMAL:
			runNormal();
		case SLICED:
			runSliced();
		default:
			runNormal();
			break;
		}
	}
	
	private static void runSliced() {
		SensorArmRunnable sar = new SensorArmRunnable(Motor.C, 175, 85);
		sar.init();
		
		LineFollower lf = new LineFollower(sar);
		while (true) {
			sar.step();
			lf.step();
		}
	}

	private static void runNormal() {
		SensorArmRunnable sar = new SensorArmRunnable(Motor.C, 175, 85);
		sar.start();

		LineFollower lf = new LineFollower(sar);
		lf.start();
	}
	
	private static void runArcTest() {
		new LineFollower().arcTest();
	}
	
	public static void debugInfo(String msg) {
	    System.out.println(msg);
	    Button.waitForAnyPress();
	}
}
