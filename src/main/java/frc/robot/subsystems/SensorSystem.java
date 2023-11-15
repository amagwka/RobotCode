package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Ultrasonic;
import frc.robot.C;
import frc.robot.utils.SensorAverage;
import frc.robot.utils.SensorAverageUpdater;
import frc.robot.utils.USensorAverage;
import frc.robot.utils.USensorAverageUpdater;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SPI;

import java.util.ArrayDeque;
import java.util.Queue;

import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.Cobra;

public class SensorSystem {
    private Cobra cobra;
    private Ultrasonic sonic1;
    private Ultrasonic sonic2;
    public AnalogInput sharp1;
    private AnalogInput sharp2;
    public AHRS gyro;

    private SensorAverage IR1sensorAverage;
    private SensorAverage IR2sensorAverage;
    private USensorAverage U1sensorAverage;
    private USensorAverage U2sensorAverage;

    private Thread IR1sensorThread;
    private Thread IR2sensorThread;
    private SensorAverageUpdater IR1sensorUpdater;
    private SensorAverageUpdater IR2sensorUpdater;
    private USensorAverageUpdater U1sensorUpdater;
    private Thread U1sensorThread;


    public SensorSystem() {
        this.cobra = new Cobra();
        this.sharp1 = new AnalogInput(0);
        this.sharp2= new AnalogInput(1);

        this.sonic1 = new Ultrasonic(8, 9);
        //this.sonic1.setAutomaticMode(true);

        this.sonic2 = new Ultrasonic(10, 11);
        //this.sonic2.setAutomaticMode(true);
        
        IR1sensorAverage = new SensorAverage();
        IR2sensorAverage = new SensorAverage();

        // Assuming the exponent and multiplier are the same for both sensors
        // If they are different, adjust accordingly
        double exponent = -1.2045;
        double multiplier = 27.726;

        IR1sensorUpdater = new SensorAverageUpdater(IR1sensorAverage, sharp1, exponent, multiplier);
        IR2sensorUpdater = new SensorAverageUpdater(IR2sensorAverage, sharp2, exponent, multiplier);

        IR1sensorThread = new Thread(IR1sensorUpdater);
        IR2sensorThread = new Thread(IR2sensorUpdater);

        IR1sensorThread.start();
        IR2sensorThread.start();


        U1sensorAverage = new USensorAverage();
        U2sensorAverage = new USensorAverage();

        U1sensorUpdater = new USensorAverageUpdater(U1sensorAverage, U2sensorAverage, sonic1, sonic2);

        U1sensorThread = new Thread(U1sensorUpdater);

        U1sensorThread.start();

        this.gyro = new AHRS(SPI.Port.kMXP);
    }

    public int getCobraRawValue(int channel) {
        return cobra.getRawValue(channel);
    }

    public double getCobraVoltage(int channel) {
        return cobra.getVoltage(channel);
    }

    public double getIR1Distance() {
        return (IR1sensorAverage != null) ? IR1sensorAverage.getAverage() : 0.0;
    }
    
    public double getIR2Distance() {
        return (IR1sensorAverage != null) ? IR2sensorAverage.getAverage() : 0.0;
    }
    
    
    public double getXAccel() {
        return gyro.getDisplacementX();
    }

    public double getSonic1Distance() {
        //sonic.ping();
        //Timer.delay(0.003);
        return U1sensorAverage.getMedian()/10;
    }
    public double getSonic2Distance() {
        //sonic.ping();
        //Timer.delay(0.003);
        return U2sensorAverage.getMedian()/10;
    }

    public double getYaw() {
        return gyro.getYaw()+180;
    }

    public double getAngle() {
        return gyro.getAngle();
    }

    public double getRoll() {
        return gyro.getRoll()+180;
    }

    public void resetGyro() {
        gyro.zeroYaw();
    }

    public void stopSensorThreads() {
        IR1sensorUpdater.stop();
        IR2sensorUpdater.stop();
        U1sensorUpdater.stop();

        try {
            IR1sensorThread.join();
            IR2sensorThread.join();
            U1sensorThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isAround(double a, double b, double c) {
        return Math.abs(a - b) <= c;
    }
    public void FilterUpdate(){
        IR1sensorAverage.addSample(Math.pow(sharp1.getAverageVoltage(), -1.2045) * 27.726);
        IR2sensorAverage.addSample(Math.pow(sharp2.getAverageVoltage(), -1.2045) * 27.726);
        U1sensorAverage.addSample(sonic1.getRangeMM());
        U2sensorAverage.addSample(sonic2.getRangeMM());
    }
}
