package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Ultrasonic;
import frc.robot.C;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SPI;
import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.Cobra;

public class SensorSystem {
    private Cobra cobra;
    private Ultrasonic sonic;
    private AnalogInput aInput;
    //private AnalogInput sharp;
    private AHRS gyro;

    public SensorSystem() {
        this.cobra = new Cobra();
        //this.sharp = new AnalogInput(C.SHARP);
        this.sonic = new Ultrasonic(10, 11);
        this.sonic.setAutomaticMode(true);
        this.aInput = new AnalogInput(0);
        this.gyro = new AHRS(SPI.Port.kMXP);
    }

    public int getCobraRawValue(int channel) {
        return cobra.getRawValue(channel);
    }

    public double getCobraVoltage(int channel) {
        return cobra.getVoltage(channel);
    }
/*
    public double getIRDistance() {
        return (Math.pow(sharp.getAverageVoltage(), -1.2045) * 27.726);
    }
*/
    public double getSonicDistance(boolean metric) {
        //sonic.ping();
        //Timer.delay(0.003);
        if (metric)
            return sonic.getRangeMM();
        else
            return sonic.getRangeInches();
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

    public AnalogInput getaInput() {
        return aInput;
    }

    public void resetGyro() {
        gyro.zeroYaw();
    }

    public boolean isAround(double a, double b, double c) {
        return Math.abs(a - b) <= c;
    }
}
