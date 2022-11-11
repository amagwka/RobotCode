package frc.robot.commands;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.Training;

public class Drive extends CommandBase
{
    /**
     * Bring in Subsystem and Gamepad code
     */
    private static final Training train = RobotContainer.train;
    private static final OI oi = RobotContainer.oi;
    
    /**
     * Joystick inputs
     */
    double inputLeftY = 0;
    double inputLeftX = 0;
    double inputRightY = 0;
    double inputRightX = 0;
    double inputLeftB = 0;
    
    /**
     * Ramp Constants
     */
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

    /**
     * Constructor
     */
    public Drive()
    {
        addRequirements(train); //add the traning subsystem as a requirement 
    }

    /**
     * Code here will run once when the command is called for the first time
     */
    @Override
    public void initialize()
    {

    }
    /**
     * Code here will run continously every robot loop until the command is stopped
     */
    @Override
    public void execute()
    {
        /**
         * Ramp
         */
        
        inputLeftX = oi.getLeftDriveX();
        inputLeftY = - oi.getLeftDriveY();
        inputRightY = oi.getRightDriveY();

        /**
         * Ramp
         */
        deltaLeftX = inputLeftX - prevLeftX;
        deltaLeftY = inputLeftY - prevLeftY;
        deltaRightX = inputRightX - prevRightX;
        if(deltaLeftX >= DELTA_LIMIT)
            inputLeftX += RAMP_UP;
        else if (deltaLeftX <= -DELTA_LIMIT)
            inputLeftX -= RAMP_DOWN;
        if(deltaLeftY >= DELTA_LIMIT)
            inputLeftY += RAMP_UP;
        else if (deltaLeftY <= -DELTA_LIMIT)
            inputLeftY -= RAMP_DOWN;
        if(deltaRightX >= DELTA_LIMIT)
            inputRightX += RAMP_UP;
        else if (deltaRightX <= -DELTA_LIMIT)
            inputRightX -= RAMP_DOWN;
        prevLeftY = inputLeftY;
        prevLeftX = inputLeftX;
        prevRightX = inputRightX;

        /**
         * Control Motor
         */
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
    public void end(boolean interrupted)
    {
        train.setDriveMotorSpeeds(0.,0.,0.);
    }

    /**
     * Check to see if command is finished
     */
    @Override
    public boolean isFinished()
    {
        return false;
    }
}