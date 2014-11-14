package unia.linefollower;

import lejos.nxt.Button;

public class MainRoutine {
	public static void main(String[] args) {
		LineFollower lf = new LineFollower();
		lf.run();
	}
	
	public static void debugInfo(String msg) {
	    System.out.println(msg);
	    Button.waitForAnyPress();
	}
}
