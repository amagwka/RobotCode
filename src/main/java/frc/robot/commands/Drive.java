package frc.robot.commands;

import java.io.Console;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.OMS;
import frc.robot.subsystems.Training;

public class Drive extends CommandBase
{
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

    double gripperDegrees=150;
    double r_LiftDegrees=0;
    double liftSpeed=0;
    
    
    
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
    private static final double RAMP_UP     = 0.05;

    /**
     * Ramp down Constant
     */
    private static final double RAMP_DOWN   = 0.05;

    /**
     * Delta Limit
     */
    private static final double DELTA_LIMIT = 0.075;

    // private NetworkTableEntry LeftBumper = train.tab.add("Left Bumper", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();
    // private NetworkTableEntry RightBumper = train.tab.add("Right Bumper", 0).withWidget(BuiltInWidgets.kNumberBar).getEntry();

    /**
     * Constructor
     */
    public Drive(){
        addRequirements(train,oms); //add the traning subsystem as a requirement 
    }

    /**
     * Code here will run once when the command is called for the first time
     */
    @Override
    public void initialize(){
        //oi.getPadNumber();
    }
    /**
     * Code here will run continously every robot loop until the command is stopped
     */
    @Override
    public void execute(){
        inputLeftX = oi.getLeftDriveX();
        inputLeftY = - oi.getLeftDriveY();
        inputRightY = oi.getRightDriveY();

        inputLeftBumper = oi.getDriveLeftBumper();
        inputRightBumper = oi.getDriveRightBumper();

        inputLeftLTButton = oi.getDriveLeftL1Button();
        inputRightRTButton = oi.getDriveRightR1Button();

        inputAButton = oi.getDriveAButton();
        inputBButton = oi.getDriveBButton();

        inputXButton = oi.getDriveXButton();
        inputYButton = oi.getDriveYButton();

        //LeftBumper.setDouble(inputLeftBumper);
        //RightBumper.setDouble(inputRightBumper);
        
        deltaLeftX = inputLeftX - prevLeftX;
        deltaLeftY = inputLeftY - prevLeftY;
        deltaRightY = inputRightY - prevRightY;
        if(deltaLeftX >= DELTA_LIMIT)
            inputLeftX += RAMP_UP;
        else if (deltaLeftX <= -DELTA_LIMIT)
            inputLeftX -= RAMP_DOWN;
        if(deltaLeftY >= DELTA_LIMIT)
            inputLeftY += RAMP_UP;
        else if (deltaLeftY <= -DELTA_LIMIT)
            inputLeftY -= RAMP_DOWN;
        if(deltaRightY >= DELTA_LIMIT)
            inputRightY += RAMP_UP;
        else if (deltaRightY <= -DELTA_LIMIT)
            inputRightY -= RAMP_DOWN;
        prevLeftY = inputLeftY;
        prevLeftX = inputLeftX;
        prevRightY = inputRightY;

        
        gripperDegrees+= (-inputLeftBumper-1)*10 + (inputRightBumper+1)*10;

        if(gripperDegrees>300)gripperDegrees=300;
        if(gripperDegrees<0)gripperDegrees=0;
        if(r_LiftDegrees>300)r_LiftDegrees=300;
        if(r_LiftDegrees<0)r_LiftDegrees=0;
        if(liftSpeed<0.0)liftSpeed=0;
        if(liftSpeed>1.0)liftSpeed=1;


        oms.setGripperPosition(gripperDegrees);
        oms.setR_liftMotorSpeed();
        //getMotorSpeeds(inputLeftX,inputLeftY,inputRightY);
        
        /*train.setMotor0Speed((-inputLeftX+inputRightY)/(inputLeftB+0.7));
        train.setMotor1Speed(((0.5*inputLeftX)-(1.2247448714*inputLeftY)+inputRightY)/(inputLeftB+1));
        train.setMotor2Speed(((0.5*inputLeftX)+(1.2247448714*inputLeftY)+inputRightY)/(inputLeftB+0.7));*/
        train.holonomicDrive(inputLeftX, inputLeftY, inputRightY);
        /*
        train.setMotor0Speed(0.5 * inputLeftX - 0.866 * inputLeftY + inputRightY);
        train.setMotor1Speed(0.5 * inputLeftX + 0.866 * inputLeftY + inputRightY);
        train.setMotor2Speed(inputLeftX + inputRightY);*/
    }

    /**
     * When the comamnd is stopped or interrupted this code is run
     * <p>
     * Good place to stop motors in case of an error
     */
    @Override
    public void end(boolean interrupted){
        train.setDriveMotorSpeeds(0.,0.,0.);
    }

    /**
     * Check to see if command is finished
     */
    @Override
    public boolean isFinished(){
        return false;
    }
}