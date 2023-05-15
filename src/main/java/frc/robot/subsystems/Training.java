package frc.robot.subsystems;

//WPI imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.C;
import frc.robot.gamepad.OI;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;

import java.util.Map;

//Java imports

//Vendor imports
import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;
import com.studica.frc.Cobra;
import com.studica.frc.Servo;
import com.studica.frc.ServoContinuous;



public class Training extends SubsystemBase {
    private MotorSystem motorSystem;
    private SensorSystem sensorSystem;
    private ShuffleboardSystem shuffleboardSystem;
    private int x = 0;

    public Training(MotorSystem motorSystem, SensorSystem sensorSystem, ShuffleboardSystem shuffleboardSystem) {
        this.motorSystem = motorSystem;
        this.sensorSystem = sensorSystem;
        this.shuffleboardSystem = shuffleboardSystem;
    }
    
    public MotorSystem getMotorSystem() {
        return this.motorSystem;
    }
    
    OI oi;
    int x=0;
    public Training() {
        oi = new OI();
        
        backMotor = new TitanQuad(C.TITAN_ID, 0);
        leftMotor = new TitanQuad(C.TITAN_ID, 1);
        rightMotor = new TitanQuad(C.TITAN_ID, 2);
        

        leftEncoder = new TitanQuadEncoder(leftMotor, 1, 1);
        rightEncoder = new TitanQuadEncoder(rightMotor, 2, 1);
        backEncoder = new TitanQuadEncoder(backMotor, 0, 1);

        

        // Sensors
        //cobra = new Cobra();
        //sharp = new AnalogInput(C.SHARP);
        //sonic = new Ultrasonic(C.SONIC_TRIGG, C.SONIC_ECHO);
        gyro = new AHRS(SPI.Port.kMXP);
    }
    public int getCobraRawValue(int channel) {
        return cobra.getRawValue(channel);
    }
    public double getCobraVoltage(int channel) {
        return cobra.getVoltage(channel);
    }

    public boolean isAround(double a, double b, double c) {
        if (Math.abs(a - b) <= c) {
            return true;
        }
        return false;
    }

    public double getIRDistance() {
        return (Math.pow(sharp.getAverageVoltage(), -1.2045) * 27.726);
    }
    public double getSonicDistance(boolean metric) {
        sonic.ping();
        Timer.delay(0.005);
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
    public void resetGyro() {
        gyro.zeroYaw();
    }

    public void holonomicDriveOLD(double x, double y, double z) {
        double rightSpeed = ((((x / 3) - (y / Math.sqrt(3))) * Math.sqrt(3)) + z) ;
        double leftSpeed = ((((x / 3) + (y / Math.sqrt(3))) * Math.sqrt(3)) + z) ;
        double backSpeed = ((-x) + z);
        /*
         * double max = Math.abs(rightSpeed); if (Math.abs(leftSpeed) > max) max =
         * Math.abs(leftSpeed); if (Math.abs(backSpeed) > max) max =
         * Math.abs(backSpeed);
         * 
         * if (max > 1) { rightSpeed /= max; leftSpeed /= max; backSpeed /= max; }
         */
        // LeftSpeed.setDouble(leftSpeed);
        // RightSpeed.setDouble(rightSpeed);
        // BackSpeed.setDouble(backSpeed);
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
        backMotor.set(backSpeed);
    }
    public void holonomicDrive(double x, double y, double z) {
        double rightSpeed = x*0.5 - Math.sqrt(3)/2*y + z;
        double leftSpeed = x*0.5 + Math.sqrt(3)/2*y + z;
        double backSpeed = ((-x) + z);
        
        double max = Math.abs(rightSpeed); if (Math.abs(leftSpeed) > max) max =
        Math.abs(leftSpeed); if (Math.abs(backSpeed) > max) max =
        Math.abs(backSpeed);
        if (max > 1) { rightSpeed /= max; leftSpeed /= max; backSpeed /= max; }
        
        // LeftSpeed.setDouble(leftSpeed);
        // RightSpeed.setDouble(rightSpeed);
        // BackSpeed.setDouble(backSpeed);
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
        backMotor.set(backSpeed);
    }

    public void AutoForward(double y) {
        double rightSpeed = -y ;
        double leftSpeed = y;

        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);//BuiltInWidgets.kGraph
    }



    public double getLeftEncoderDistance() {
        return leftEncoder.getEncoderDistance();
    }
    public double getRightEncoderDistance() {
        return rightEncoder.getEncoderDistance() * -1;
    }
    public double getAverageForwardEncoderDistance() {
        return (getLeftEncoderDistance() + getRightEncoderDistance()) * Math.cos(Math.toRadians(30));
    }
    public double getBackEncoderDistance() {
        return backEncoder.getEncoderDistance();
    }

    public void resetLeftEncoder() {
        leftEncoder.reset();
    }
    public void resetRightEncoder() {
        rightEncoder.reset();
    }
    public void resetBackEncoder() {
        backEncoder.reset();
    }


    /*public void setServoAngle() {
        servo.setAngle(servoPos.getDouble(0.0));
    }
    public void setServoAngle(double degrees) {
        servo.setAngle(degrees);
    }

    public void setServoSpeed() {
        servoC.set(servoSpeed.getDouble(0.0));
    }*/

    
    public void setLeftMotorSpeed(double speed) {
        leftMotor.set(speed);
    }
    public void setRightMotorSpeed(double speed) {
        rightMotor.set(speed);
    }
    public void setBackMotorSpeed(double speed) {
        backMotor.set(speed);
    }
    public void setDriveMotorSpeeds(double leftSpeed, double rightSpeed, double backSpeed) {
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
        backMotor.set(backSpeed);
    }

    @Override
    public void periodic() {
        if (x % 6==0) {
            // setServoAngle();
            //setServoSpeed();
            // sharpIR.setDouble(getIRDistance());
            // ultraSonic.setDouble(getSonicDistance(true));
            // cobraRaw.setDouble(getCobraRawValue(0)); //Just going to use channel 0 for
            // cobraVoltage.setDouble(getCobraVoltage(0));
            //setLeftMotorSpeed(LeftMotor.getDouble(0));
            //setRightMotorSpeed(RightMotor.getDouble(0));
            //setBackMotorSpeed(BackMotor.getDouble(0));
            LeftEncoder.setDouble(getLeftEncoderDistance());
            RightEncoder.setDouble(getRightEncoderDistance());
            BackEncoder.setDouble(getBackEncoderDistance());
            ForwardForce.setDouble(getAverageForwardEncoderDistance());
            //navX.setDouble(getAngle());
        }
        x++;
    }
}