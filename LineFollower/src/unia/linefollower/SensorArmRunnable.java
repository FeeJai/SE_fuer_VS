package unia.linefollower;

import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.robotics.RegulatedMotor;

public class SensorArmRunnable implements Runnable {

	private RegulatedMotor sensorMotor;
	private ColorSensor sensor;
	private int maxAngle;
	
	private float minDistance = 3;
	private float maxDistance = 6;
	private int black = 300;
	private int white = 400;
	
	// Wo der LineFollower hin soll
	private volatile float bestLight = white;
	private volatile int nextAngle = 0;
	private volatile float armLength;
	
	public SensorArmRunnable(RegulatedMotor sensorMotor, float armLength, int maxAngle) {
		this.sensorMotor = sensorMotor;
		this.maxAngle = maxAngle;
		this.armLength = armLength;
		sensor = new ColorSensor(SensorPort.S1);
	}

	@Override
	public void run() {
		init();
		
		while (true) {
			step();
		}
		
	}

	public void init() {
		initSensorMotor();
		sensor.setFloodlight(true);
		sensorMotor.setSpeed(75);
	}

	public void step() {
		searchMinimum(nextAngle);
		
		LCD.drawString("angle: " + nextAngle, 0, 0);
		LCD.drawString(
				"light: " + String.valueOf(sensor.getRawLightValue()), 0, 2);
		LCD.drawString("bestLight: " + bestLight, 0, 3);
	}
	
	private float getBestLightValue() {
		return bestLight;
	}
	
	public float getArmLength() {
		return armLength;
	}

	public float getNextDistance() {
		float _bestLight = getBestLightValue();
		if(_bestLight < white) {
			float distance = maxDistance;
			int absAngle = nextAngle;
			if (absAngle < 0) {
				absAngle = absAngle * -1;
			}
			
//			if (absAngle > maxAngle / 2) {
//				distance = 0f;
//			} else {
//				distance = minDistance +
//						((maxDistance - minDistance) * ((maxAngle/2) - absAngle) / (maxAngle / 2));
//			}
			return distance;
		} else {
			return minDistance;
		}
	}
	
	public int getNextAngle() {
		return nextAngle;
	}

//	private int getRepairedSensorAngle() {
//		int angle = sensorMotor.getTachoCount();
//		while (angle <= -180 || angle > 180) {
//			if (angle <= -180) {
//				angle += 360;
//			} else {
//				angle -= 360;
//			}
//		}
//
//		return angle;
//	}
	
	private void searchMinimum (int startAngle) {
		searchMinimum(startAngle, 2);
	}
	private void searchMinimum (int startAngle, int rotateSteps) {
		final int stepDeg = 6;
		int minAngle = startAngle;
		int curAngle = startAngle;
		
		sensorMotor.rotateTo(curAngle);

		int minValue = sensor.getRawLightValue();
		
		for (int i = 0; i < rotateSteps; i++) {
			curAngle = startAngle + i * stepDeg;
			if (curAngle >= maxAngle / 2) {
				break;
			}
			sensorMotor.rotateTo(curAngle);
			int curValue = readLightValue();
			if (curValue < minValue) {
				minValue = curValue;
				minAngle = curAngle;
			}
		}
		
		sensorMotor.rotateTo(curAngle);
		
		for (int i = 0; i < rotateSteps; i++) {
			curAngle = startAngle - i * stepDeg;
			if (curAngle <= -(maxAngle / 2)) {
				break;
			}
			sensorMotor.rotateTo(curAngle);
			int curValue = readLightValue();
			if (curValue < minValue) {
				minValue = curValue;
				minAngle = curAngle;
			}
		}
		
		if (minValue > black && rotateSteps < 8) {
			searchMinimum(startAngle, rotateSteps * 2);
		} else {
			nextAngle = minAngle;
			bestLight = minValue;
		}
	}
	
	private int readLightValue() {
//		try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return sensor.getRawLightValue();
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	/**
	 * Setzt den Sensor-Arm auf die Mitte.
	 */
	private void initSensorMotor() {
		sensorMotor.setSpeed(100);
		sensorMotor.rotateTo(maxAngle, true);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sensorMotor.stop();
		
		sensorMotor.setSpeed(30);
		sensorMotor.resetTachoCount();
		sensorMotor.rotateTo(-45);
		sensorMotor.resetTachoCount();
	}

}
