package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.OMS;
import frc.robot.subsystems.Training;

public class Drive extends CommandBase {
    /**
     * Bring in Subsystem and Gamepad code
     */
    private static final Training train = RobotContainer.train;
    private static final OMS oms = RobotContainer.oms;
    private static final OI oi = RobotContainer.oi;

    /**
     * Joystick inputs
     */
    double inputLeftY = 0;
    double inputLeftX = 0;
    double inputRightY = 0;

    double inputLeftBumper = 0;
    double inputRightBumper = 0;

    boolean inputLeftLTButton = false;
    boolean inputRightRTButton = false;

    boolean inputAButton = false;
    boolean inputBButton = false;

    boolean inputXButton = false;
    boolean inputYButton = false;

    double r_gripperDegrees = 150;
    double gripperDegrees = 60;
    double r_LiftSpeed = 0;
    double liftSpeed = 0;

    double deltaLeftY = 0;
    double deltaLeftX = 0;
    double deltaRightY = 0;
    double deltaRightX = 0;
    double prevLeftY = 0;
    double prevLeftX = 0;
    double prevRightY = 0;
    double prevRightX = 0;
    double[] motors = new double[] { 0, 0, 0 };
    /**
     * Ramp up Constant
     */
    private static final double RAMP_UP = 0.05;

    /**
     * Ramp down Constant
     */
    private static final double RAMP_DOWN = 0.05;

    /**
     * Delta Limit
     */
    private static final double DELTA_LIMIT = 0.075;

     private NetworkTableEntry RightBumper = train.tab.add("Right Bumper",
     0).withWidget(BuiltInWidgets.kNumberBar).getEntry();

    /**
     * Constructor
     */
    public Drive() {
        addRequirements(train, oms); // add the traning subsystem as a requirement
    }

    public double toInt(boolean b) {
        return b ? 1.0 : 0.0;
    }

    /**
     * Code here will run once when the command is called for the first time
     */
    @Override
    public void initialize() {
        // oi.getPadNumber();
    }

    /**
     * Code here will run continously every robot loop until the command is stopped
     */
    @Override
    public void execute() {
        inputLeftX = oi.getLeftDriveX();
        inputLeftY = -oi.getLeftDriveY();
        inputRightY = oi.getRightDriveY();

        inputLeftBumper = oi.getDriveLeftBumper();
        inputRightBumper = oi.getDriveRightBumper();

        inputLeftLTButton = oi.getDriveLeftL1Button();
        inputRightRTButton = oi.getDriveRightR1Button();

        inputAButton = oi.getDriveAButton();
        inputBButton = oi.getDriveBButton();

        inputXButton = oi.getDriveXButton();
        inputYButton = oi.getDriveYButton();

        

        deltaLeftX = inputLeftX - prevLeftX;
        deltaLeftY = inputLeftY - prevLeftY;
        deltaRightY = inputRightY - prevRightY;
        if (deltaLeftX >= DELTA_LIMIT)
            inputLeftX += RAMP_UP;
        else if (deltaLeftX <= -DELTA_LIMIT)
            inputLeftX -= RAMP_DOWN;
        if (deltaLeftY >= DELTA_LIMIT)
            inputLeftY += RAMP_UP;
        else if (deltaLeftY <= -DELTA_LIMIT)
            inputLeftY -= RAMP_DOWN;
        if (deltaRightY >= DELTA_LIMIT)
            inputRightY += RAMP_UP;
        else if (deltaRightY <= -DELTA_LIMIT)
            inputRightY -= RAMP_DOWN;
        prevLeftY = inputLeftY;
        prevLeftX = inputLeftX;
        prevRightY = inputRightY;

        gripperDegrees += (-inputLeftBumper - 1) * 3 + (inputRightBumper + 1) * 3;
        r_gripperDegrees += -toInt(inputXButton)*2 + toInt(inputBButton) *2;
        //r_gripperDegrees += -toInt(inputLeftLTButton) + (toInt(inputRightRTButton) );
        liftSpeed += -toInt(inputYButton) * 0.7 + toInt(inputAButton) * 0.7;
        r_LiftSpeed += -toInt(inputLeftLTButton)*0.2 + toInt(inputRightRTButton)*0.2;
        
        gripperDegrees=MathUtil.clamp(gripperDegrees, 0, 180);
        r_gripperDegrees=MathUtil.clamp(r_gripperDegrees, 0, 180);
        liftSpeed=MathUtil.clamp(liftSpeed, -1, 1);

        liftSpeed /= 3;
        r_LiftSpeed /= 2;
        RightBumper.setDouble(r_gripperDegrees);
        oms.setGripperPosition(r_gripperDegrees);
        oms.setLiftSpeed(liftSpeed);
        //oms.setGripper2Position(r_LiftSpeed);
        // getMotorSpeeds(inputLeftX,inputLeftY,inputRightY);

        /*
         * train.setMotor0Speed((-inputLeftX+inputRightY)/(inputLeftB+0.7));
         * train.setMotor1Speed(((0.5*inputLeftX)-(1.2247448714*inputLeftY)+inputRightY)
         * /(inputLeftB+1));
         * train.setMotor2Speed(((0.5*inputLeftX)+(1.2247448714*inputLeftY)+inputRightY)
         * /(inputLeftB+0.7));
         */
        train.holonomicDrive(inputLeftX/3, inputLeftY/3, inputRightY/3);
        /*
         * train.setMotor0Speed(0.5 * inputLeftX - 0.866 * inputLeftY + inputRightY);
         * train.setMotor1Speed(0.5 * inputLeftX + 0.866 * inputLeftY + inputRightY);
         * train.setMotor2Speed(inputLeftX + inputRightY);
         */
    }

    @Override
    public void end(boolean interrupted) {
        train.setDriveMotorSpeeds(0., 0., 0.);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}