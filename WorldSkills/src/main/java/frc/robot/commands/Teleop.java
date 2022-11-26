package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveBase;
import frc.robot.gamepad.OI;

public class Teleop extends CommandBase 
{

    /**
     * Bring in DriveBase and OI
     */
    private static final DriveBase driveBase = RobotContainer.driveBase;
    private static final OI oi = RobotContainer.oi;

    /**
     * Joystick inputs
     */
    double inputLeftY = 0;
    double inputRightY = 0;
    double CameraDegree = 50;

    boolean open;
    boolean close;
    boolean up;
    boolean down;

    /**
     * Constructor
     */
    public Teleop ()
    {
        addRequirements(driveBase);
    }

    /**
     * Code here will run once when the command is called for the first time
     */
    @Override
    public void initialize()
    {
        driveBase.resetEncoder();
    }

    /**
     * Code here will run continously every robot loop until the command is stopped
     */
    @Override
    public void execute()
    {
        if(oi.getDriveAButton()){
            driveBase.setDriverMotorSpeed(oi.getLeftDriveY() * 0.5, oi.getLeftDriveY() * 0.5);
        }
        else if(oi.getDriveBButton()){
            driveBase.setDriverMotorSpeed(oi.getLeftDriveY() * 0.5, oi.getRightDriveY() * 0.5);
        }
        else{
            driveBase.setDriverMotorSpeed(oi.getLeftDriveY(), oi.getRightDriveY());
        }
        
        if(oi.getDriveXButton()){
            driveBase.setCameraServo(60);
        }
        else if(oi.getDriveYButton()){
            driveBase.setCameraServo(40);
        }

        if(oi.getDriveDPad() == 90){
            driveBase.setCameraServo(60);
        }
        else if(oi.getDriveDPad() == 270){
            driveBase.setCameraServo(40);
        }

        //Lift
        up = oi.getDriveR1();
        if(oi.getDriveR2() >= 0.05) {
            down = true;
        }
        else {
            down = false;
        }

        if(up)
        {
            driveBase.setElevatorMotorSpeed(-0.4);
        }
        else if(down)
        {
            driveBase.setElevatorMotorSpeed(0.35);
        }
        else 
        {
            driveBase.setElevatorMotorSpeed(0.0);
        }

        //Claw
        close = oi.getDriveL1();
        if(oi.getDriveL2() >= 0.05) {
            open = true;
        }
        else{
            open = false;
        }

        if(open)
        {
            driveBase.setClawServoPosition(100, 220);
            //driveBase.setClawLeftServoPosition(145);
            //driveBase.setClawRightServoPosition(165);
        }
        else if(close) {
            driveBase.setClawServoPosition(165, 145);
            //driveBase.setClawLeftServoPosition(160);
            //driveBase.setClawRightServoPosition(150);
        }
    }

    @Override
    public void end(boolean interrupted)
    {
        driveBase.setDriverMotorSpeed(0.0, 0.0);
        driveBase.setElevatorMotorSpeed(0.0);
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