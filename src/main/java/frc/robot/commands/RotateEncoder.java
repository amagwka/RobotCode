package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Training;

public class RotateEncoder extends CommandBase
{
    /**
     * Bring in Subsystem and Gamepad code
     */
    private static final Training train = RobotContainer.train;

    private double setpointDistance;
    private double setpointYaw; 

    //Create two PID Controllers
    PIDController pidM0Axis,pidM1Axis,pidM2Axis;
    PIDController pidZAxis;

    double[] motors = new double[] { 0, 0, 0 };

    public RotateEncoder(double setpointYaw, double epsilonYaw)
    {
        this.setpointDistance = setpointDistance;
        this.setpointYaw = setpointYaw;
        addRequirements(train);

        pidM0Axis = new PIDController(0.2, 0., 0.);
        pidM0Axis.setTolerance(epsilonDistance);

        pidM1Axis = new PIDController(0.2, 0., 0.);
        pidM1Axis.setTolerance(epsilonDistance);

        pidM2Axis = new PIDController(0.2, 0., 0.);
        pidM2Axis.setTolerance(epsilonDistance);

        pidZAxis = new PIDController(0.2, 0., 0.);
        pidZAxis.setTolerance(epsilonYaw);
    }

    @Override
    public void initialize()
    {
        motors[0]=train.getRightEncoderDistance()+train.getBackEncoderDistance()-train.getLeftEncoderDistance()*2;
        motors[1]=train.getLeftEncoderDistance()+train.getBackEncoderDistance()-train.getRightEncoderDistance()*2;
        motors[2]=train.getLeftEncoderDistance()+train.getRightEncoderDistance()-train.getBackEncoderDistance()*2;
    }

    @Override
    public void execute()
    {
        train.holonomicDrive(0.0,0.0,
         MathUtil.clamp(pidZAxis.calculate(train.getYaw(), setpointYaw), -0.2, 0.2));
        
        train.setDriveMotorSpeeds(
            MathUtil.clamp(pidZAxis.calculate(train.getYaw(), setpointYaw), -0.2, 0.2),
            MathUtil.clamp(pidZAxis.calculate(train.getYaw(), setpointYaw), -0.2, 0.2),
            MathUtil.clamp(pidZAxis.calculate(train.getYaw(), setpointYaw), -0.2, 0.2)
            );
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
    public boolean isFinished(){
        return train.isAround(train.getYaw(), setpointYaw, 0.5);
    }
}