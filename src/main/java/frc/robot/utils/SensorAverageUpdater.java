package frc.robot.utils;

import edu.wpi.first.wpilibj.AnalogInput;

public class SensorAverageUpdater implements Runnable {
    private SensorAverage sensorAverage;
    private AnalogInput sensorInput;
    private volatile boolean running = true;
    private double exponent;
    private double multiplier;

    public SensorAverageUpdater(SensorAverage sensorAverage, AnalogInput sensorInput, double exponent, double multiplier) {
        this.sensorAverage = sensorAverage;
        this.sensorInput = sensorInput;
        this.exponent = exponent;
        this.multiplier = multiplier;
    }

    public void run() {
        while (running) {
            double sensorValue = Math.pow(sensorInput.getAverageVoltage(), exponent) * multiplier;
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
}
