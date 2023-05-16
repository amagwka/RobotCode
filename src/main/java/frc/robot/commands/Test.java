package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.OMS;
import frc.robot.subsystems.Training;

public class Test extends CommandBase
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
    
    
    double deltaLeftY = 0;
    double deltaLeftX = 0;
    double deltaRightY = 0;
    double deltaRightX = 0;
    double prevLeftY = 0;
    double prevLeftX = 0;
    double prevRightY = 0;
    double prevRightX = 0;
    double[] motors = new double[] { 0, 0, 0 };
    

    private static final double RAMP_UP     = 0.05;
    

    private static final double RAMP_DOWN   = 0.05;
    

    private static final double DELTA_LIMIT = 0.075;

    
    

    public Test(){
        addRequirements(train,oms);
    }
    @Override
    public void initialize(){

    }
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
        

        //getMotorSpeeds(inputLeftX,inputLeftY,inputRightY);
        
        /*train.setMotor0Speed((-inputLeftX+inputRightY)/(inputLeftB+0.7));
        train.setMotor1Speed(((0.5*inputLeftX)-(1.2247448714*inputLeftY)+inputRightY)/(inputLeftB+1));
        train.setMotor2Speed(((0.5*inputLeftX)+(1.2247448714*inputLeftY)+inputRightY)/(inputLeftB+0.7));*/
        train.getMotorSystem().holonomicDrive(inputLeftX, inputLeftY, inputRightY);
        /*
        train.setMotor0Speed(0.5 * inputLeftX - 0.866 * inputLeftY + inputRightY);
        train.setMotor1Speed(0.5 * inputLeftX + 0.866 * inputLeftY + inputRightY);
        train.setMotor2Speed(inputLeftX + inputRightY);*/
    }
    

    @Override
    public void end(boolean interrupted){
        train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
    }
    

    @Override
    public boolean isFinished(){
        return false;
    }
}