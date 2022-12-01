package frc.robot.commands;

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
     * Ramp C
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
    public Drive(){
        addRequirements(train);
    }

    
    @Override
    public void initialize(){

    }
    

    @Override
    public void execute(){
        inputLeftX = oi.getLeftDriveX();
        inputLeftY = - oi.getLeftDriveY();
        inputRightY = oi.getRightDriveY();

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
    
    @Override
    public void end(boolean interrupted)
    {
        train.setDriveMotorSpeeds(0.,0.,0.);
    }

    
    @Override
    public boolean isFinished()
    {
        return false;
    }
}