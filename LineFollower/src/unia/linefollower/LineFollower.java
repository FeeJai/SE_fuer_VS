package unia.linefollower;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;

public class LineFollower implements Runnable {

	private DifferentialPilot pilot;
	private SensorArmRunnable sar;

	public LineFollower() {
		this(null);
	}

	public LineFollower(SensorArmRunnable sar) {
		this.sar = sar;
		pilot = new DifferentialPilot(44.2f, 145f, Motor.B, Motor.A, true);
	}

	@Override
	public void run() {
		while (true) {
			step();
		}
	}
	
	// from JRE Math.class
	private static final float _DEG_TO_RAD = 0.0174532925199432957692369076849f; 
	private static float degToRadians(double degree) {
		return (float)(degree * _DEG_TO_RAD);
	}
	
	public void step() {
		pilot.setTravelSpeed(10);

		int nextAngle = sar.getNextAngle();
		int armLength = (int) sar.getArmLength();
		int travDist = (int) sar.getNextDistance();
		int radius = 0, angle = 0;

		pilot.setTravelSpeed(120);
		if (nextAngle == 0) {
			pilot.travel(travDist, true);
		} else {
			// working around missing or buggy Math.class...
			int absAngle = nextAngle;
			if (nextAngle < 0) {
				absAngle = nextAngle * -1;
			}
			
			radius = (int) ((armLength / 2) / Math.cos(degToRadians(90. - absAngle)));
			angle = travDist;
			if (nextAngle < 0) {
				angle = 0 - angle;
				radius = 0 - radius;
			}
			pilot.arc(0.6 * radius, angle);
		}

		LCD.drawString("arcRadius: " + radius, 0, 6);
		LCD.drawString("arcAngle: " + angle, 0, 7);
	}

	public void start() {
		new Thread(this).start();
	}

	public void arcTest() {
		/*
		 * pilot.arc(200, 45); // left /
		 */
		pilot.arc(-200, -45); // right
		// */
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
