package frc.robot.utils;

import edu.wpi.first.wpilibj.Ultrasonic;

public class USensorAverageUpdater implements Runnable {
    private USensorAverage sensorAverage;
    private Ultrasonic sensor;
    private volatile boolean running = true;

    public USensorAverageUpdater(USensorAverage sensorAverage, Ultrasonic sensor) {
        this.sensorAverage = sensorAverage;
        this.sensor = sensor;
    }

    public void run() {
        while (running) {
            double sensorValue = sensor.getRangeMM();
            sensorAverage.addSample(sensorValue);
            try {
                Thread.sleep(0,1); // Sleep for 30 milliseconds
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
