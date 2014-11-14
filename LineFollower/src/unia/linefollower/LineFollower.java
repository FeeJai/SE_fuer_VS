package unia.linefollower;

import lejos.nxt.*;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import static unia.linefollower.MainRoutine.*;

public class LineFollower {
	
	private TouchSensor touchS;
	private DifferentialPilot pilot;
	private ColorSensor sensor;

	private RegulatedMotor sensorMotor;
	
	private int barrier = 350;
	private float minSpeed = 0;
	private float maxSpeed = 1000;
	private int black = 250;
	private int white = 400;
	
	private int counter = 0;
	
	public LineFollower(){
		
//		touchS = new TouchSensor(SensorPort.S1);
		pilot= new DifferentialPilot(2.25f, 5.5f, Motor.A, Motor.B);
		sensor = new ColorSensor(SensorPort.S1);
		sensorMotor = Motor.C;
	}
	
	public void run(){
		initSensorMotor();
		sensor.setFloodlight(true);

		while(true){
			double travSpeed = 0;
			if(sensor.getRawLightValue() <= black)
				travSpeed = maxSpeed;
			else if(sensor.getRawLightValue() >= white)
				travSpeed = minSpeed;
			else
				travSpeed = maxSpeed * ((sensor.getRawLightValue() - black)/ (white- black));	

			LCD.drawString(String.valueOf(sensor.getRawLightValue()) + " /n " + counter++, 2, 2);

			// pilot.setTravelSpeed(travSpeed);
			
			//debugInfo("" + sensorMotor.getTachoCount());
			// pilot.arc(15, sensorMotor.getTachoCount());
//			//pilot.forward();
		}
	}
	
	/**
	 * Setzt den Sensor-Arm auf die Mitte.
	 */
	private void initSensorMotor() {
		sensorMotor.setSpeed(100);
		sensorMotor.rotateTo(85, true);
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

