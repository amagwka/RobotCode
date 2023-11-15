package frc.robot.utils;

import edu.wpi.first.wpilibj.Ultrasonic;

public class USensorAverageUpdater implements Runnable {
    private USensorAverage sensorAverage1;
    private USensorAverage sensorAverage2;
    private Ultrasonic sensor1;
    private Ultrasonic sensor2;

    double sensorValue=0;
    private volatile boolean running = true;

    public USensorAverageUpdater(USensorAverage sensorAverage1,USensorAverage sensorAverage2, Ultrasonic sensor1, Ultrasonic sensor2) {
        this.sensorAverage1 = sensorAverage1;
        this.sensorAverage2 = sensorAverage2;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
    }

    public void run() {
        while (running) {

            sensor1.ping();
            
            sensorValue = sensor1.getRangeMM();
            sensorAverage1.addSample(sensorValue);

            try {
                Thread.sleep(4); // Sleep for 30 milliseconds
            } catch (InterruptedException e) {
                running = false;
            }

            sensor2.ping();
            /*try {
                Thread.sleep(4); // Sleep for 30 milliseconds
            } catch (InterruptedException e) {
                running = false;
            }*/
            sensorValue = sensor2.getRangeMM();
            sensorAverage2.addSample(sensorValue);
            try {
                Thread.sleep(4); // Sleep for 30 milliseconds
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }

    public void stop() {
        running = false;
    }
    public void start() {
        running = true;
    }

}
