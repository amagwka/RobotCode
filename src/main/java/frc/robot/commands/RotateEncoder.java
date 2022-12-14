package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Training;

public class RotateEncoder extends CommandBase {

    private static final Training train = RobotContainer.train;

    private double setpointYaw;

    PIDController pidLeftAxis, pidRightAxis, pidBackAxis;
    PIDController pidZAxis;

    double[] motors = new double[] { 0, 0, 0 };

    public RotateEncoder(double setpointYawArg, double epsilonYaw, boolean enablePIDForEachMotor,boolean delta) {
        setpointYaw = setpointYawArg;
        addRequirements(train);
        /*
        pidLeftAxis = new PIDController(0.2, 0., 0.);
        pidLeftAxis.setTolerance(1);

        pidRightAxis = new PIDController(0.2, 0., 0.);
        pidRightAxis.setTolerance(1);

        pidBackAxis = new PIDController(0.2, 0., 0.);
        pidBackAxis.setTolerance(1);
        */
        pidZAxis = new PIDController(0.1, 0.0, 0.00);
        pidZAxis.setTolerance(epsilonYaw);
    }

    @Override
    public void initialize() {
        /*motors[0] = (train.getRightEncoderDistance() + train.getBackEncoderDistance())
                - (train.getLeftEncoderDistance() * 2);
        motors[1] = (train.getLeftEncoderDistance() + train.getBackEncoderDistance())
                - (train.getRightEncoderDistance() * 2);
        motors[2] = (train.getLeftEncoderDistance() + train.getRightEncoderDistance())
                - (train.getBackEncoderDistance() * 2);*/
        setpointYaw = train.getAngle()+setpointYaw;
    }

    @Override
    public void execute() {
        //for (int i = 0; i < 3; i++) {
            train.holonomicDrive(0.0, 0.0, MathUtil.clamp(pidZAxis.calculate(train.getAngle(), setpointYaw), -1.0, 1.0));
        //}
        /*
        train.setDriveMotorSpeeds(
                MathUtil.clamp(pidLeftAxis.calculate(train.getRightEncoderDistance() + train.getBackEncoderDistance()
                        - train.getLeftEncoderDistance() * 2, motors[0]), -0.2, 0.2),
                MathUtil.clamp(pidRightAxis.calculate(train.getLeftEncoderDistance() + train.getBackEncoderDistance()
                        - train.getRightEncoderDistance() * 2, motors[1]), -0.2, 0.2),
                MathUtil.clamp(pidBackAxis.calculate(train.getLeftEncoderDistance() + train.getRightEncoderDistance()
                        - train.getBackEncoderDistance() * 2, motors[2]), -0.2, 0.2));*/
    }

    @Override
    public void end(boolean interrupted) {
        train.setDriveMotorSpeeds(0., 0., 0.);
    }

    @Override
    public boolean isFinished() {
        return pidZAxis.atSetpoint();
    }
}