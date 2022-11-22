package frc.robot.subsystems;

//WPI imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.gamepad.OI;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;

//Java imports
import java.util.Map;

//Vendor imports
import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;
import com.studica.frc.Cobra;
import com.studica.frc.Servo;
import com.studica.frc.ServoContinuous;

public class Training extends SubsystemBase {
    /**
     * Motors
     */
    private TitanQuad leftMotor;
    private TitanQuad rightMotor;
    private TitanQuad backMotor;
    private Servo servo;
    private ServoContinuous servoC;
    private TitanQuadEncoder leftEncoder;
    private TitanQuadEncoder rightEncoder;
    private TitanQuadEncoder backEncoder;

    /**
     * Sensors
     */
    private Cobra cobra;
    private Ultrasonic sonic;
    private AnalogInput sharp;
    private AHRS gyro;

    /**
     * Shuffleboard
     */
    private ShuffleboardTab tab = Shuffleboard.getTab("Training");
    private NetworkTableEntry servoPos = tab.add("Servo Position", 0).withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0, "max", 300)).getEntry();
    private NetworkTableEntry servoSpeed = tab.add("Servo Speed", 0).withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", -1, "max", 1)).getEntry();
    private NetworkTableEntry sharpIR = tab.add("Sharp IR", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
    private NetworkTableEntry ultraSonic = tab.add("Ultrasonic", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
    private NetworkTableEntry cobraRaw = tab.add("Cobra Raw", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
    private NetworkTableEntry cobraVoltage = tab.add("Cobra Voltage", 0).withWidget(BuiltInWidgets.kNumberBar)
            .getEntry();
    private NetworkTableEntry LeftEncoder = tab.add("Left Encoder", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
    private NetworkTableEntry RightEncoder = tab.add("Right Encoder", 0).withWidget(BuiltInWidgets.kNumberBar)
            .getEntry();
    private NetworkTableEntry BackEncoder = tab.add("Back Encoder", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
    private NetworkTableEntry ForwardForce = tab.add("Resulted Forward", 0).withWidget(BuiltInWidgets.kNumberBar)
            .getEntry();
    private NetworkTableEntry navX = tab.add("NavX Yaw", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
    OI oi;
    int x=0;
    public Training() {
        oi = new OI();
        // Motors
        leftMotor = new TitanQuad(Constants.TITAN_ID, 3);
        rightMotor = new TitanQuad(Constants.TITAN_ID, 0);
        backMotor = new TitanQuad(Constants.TITAN_ID, 1);

        servo = new Servo(Constants.SERVO);
        servoC = new ServoContinuous(Constants.SERVO_C);

        leftEncoder = new TitanQuadEncoder(leftMotor, 3, 0.429179324);
        rightEncoder = new TitanQuadEncoder(rightMotor, 0, 0.429179324);
        backEncoder = new TitanQuadEncoder(backMotor, 1, 0.429179324);

        // Sensors
        cobra = new Cobra();
        sharp = new AnalogInput(Constants.SHARP);
        sonic = new Ultrasonic(Constants.SONIC_TRIGG, Constants.SONIC_ECHO);
        gyro = new AHRS(SPI.Port.kMXP);
    }

    /**
     * Call for the raw ADC value
     * <p>
     * 
     * @param channel range 0 - 3 (matches what is on the adc)
     * @return value between 0 and 2047 (11-bit)
     */
    public int getCobraRawValue(int channel) {
        return cobra.getRawValue(channel);
    }

    /**
     * Call for the voltage from the ADC
     * <p>
     * 
     * @param channel range 0 - 3 (matches what is on the adc)
     * @return voltage between 0 - 5V (0 - 3.3V if the constructor Cobra(3.3F) is
     *         used)
     */
    public double getCobraVoltage(int channel) {
        return cobra.getVoltage(channel);
    }

    public boolean isAround(double a, double b, double c) {
        if (Math.abs(a - b) <= c) {
            return true;
        }
        return false;
    }

    /**
     * Call for the distance measured by the Sharp IR Sensor
     * <p>
     * 
     * @return value between 0 - 100 (valid data range is 10cm - 80cm)
     */
    public double getIRDistance() {
        return (Math.pow(sharp.getAverageVoltage(), -1.2045) * 27.726);
    }

    /**
     * Call for the distance measured by the Ultrasonic Sensor
     * <p>
     * 
     * @param metric true or false for metric output
     * @return distance in mm when metric is true, and inches when metric is false
     */
    public double getSonicDistance(boolean metric) {
        sonic.ping();
        Timer.delay(0.005);
        if (metric)
            return sonic.getRangeMM();
        else
            return sonic.getRangeInches();
    }

    /**
     * Call for the current angle from the internal NavX
     * <p>
     * 
     * @return yaw angle in degrees range -180° to 180°
     */
    public double getYaw() {
        return gyro.getYaw()+180;
    }

    public double getRoll() {
        return gyro.getRoll()+180;
    }

    /**
     * Resets the yaw angle back to zero
     */
    public void resetGyro() {
        gyro.zeroYaw();
    }

    public void holonomicDrive(double x, double y, double z) {
        double rightSpeed = ((((x / 3) - (y / Math.sqrt(3))) * Math.sqrt(3)) + z) / 2;
        double leftSpeed = ((((x / 3) + (y / Math.sqrt(3))) * Math.sqrt(3)) + z) / 2;
        double backSpeed = ((-x) + z) / 2;
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

    /**
     * Sets the servo angle based on the input from the shuffleboard widget
     */
    public double getLeftEncoderDistance() {
        return leftEncoder.getEncoderDistance();
    }

    /**
     * Gets the encoder distance for the right drive motor
     * <p>
     * 
     * @return distance traveled in mm
     */
    public double getRightEncoderDistance() {
        return rightEncoder.getEncoderDistance() * -1;
    }

    public double getAverageForwardEncoderDistance() {
        return (getLeftEncoderDistance() + getRightEncoderDistance()) / 2;
    }

    /**
     * Gets the encoder distance for the back drive motor
     * <p>
     * 
     * @return distance traveled in mm
     */
    public double getBackEncoderDistance() {
        return backEncoder.getEncoderDistance();
    }

    public void setServoAngle() {
        servo.setAngle(servoPos.getDouble(0.0));
    }

    /**
     * Sets the servo angle
     * <p>
     * 
     * @param degrees degree to set the servo to, range 0° - 300°
     */
    public void setServoAngle(double degrees) {
        servo.setAngle(degrees);
    }

    /**
     * Sets the servo speed based on the input from the shuffleboard widget
     */
    public void setServoSpeed() {
        servoC.set(servoSpeed.getDouble(0.0));
    }

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
            setServoSpeed();
            // sharpIR.setDouble(getIRDistance());
            // ultraSonic.setDouble(getSonicDistance(true));
            // cobraRaw.setDouble(getCobraRawValue(0)); //Just going to use channel 0 for
            // cobraVoltage.setDouble(getCobraVoltage(0));
            LeftEncoder.setDouble(getLeftEncoderDistance());
            RightEncoder.setDouble(getRightEncoderDistance());
            BackEncoder.setDouble(getBackEncoderDistance());
            ForwardForce.setDouble(getAverageForwardEncoderDistance());
            navX.setDouble(getYaw());
        }
        x++;
    }
}