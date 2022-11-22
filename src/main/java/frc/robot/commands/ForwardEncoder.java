package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Training;

public class ForwardEncoder extends CommandBase {
    /**
     * Bring in Subsystem and Gamepad code
     */
    private static final Training train = RobotContainer.train;

    private double setpointDistance;
    private double setpointYaw;
    private double setpointYawDelta;
    private boolean delta;

    // Create two PID Controllers
    PIDController pidYAxis;

    double[] motors = new double[] { 0, 0, 0 };

    public ForwardEncoder(double setpointDistance, double epsilonDistance, double setpointYaw, double epsilonYaw,
            boolean delta) {
        this.setpointDistance = setpointDistance;
        this.delta = delta;
        addRequirements(train);

        pidYAxis = new PIDController(1.0, 0.5, 1.0);
        pidYAxis.setTolerance(epsilonDistance);

        //pidZAxis = new PIDController(0.1, 0.5, 0.1);
        //pidZAxis.setTolerance(epsilonYaw);
    }

    @Override
    public void initialize() {
        setpointDistance += train.getAverageForwardEncoderDistance();
    }

    @Override
    public void execute() {
            train.holonomicDrive(0.0, MathUtil
                    .clamp(pidYAxis.calculate(train.getAverageForwardEncoderDistance(), setpointDistance), -0.7, 0.7),0.0);
    }

    /**
     * When the comamnd is stopped or interrupted this code is run
     * <p>
     * Good place to stop motors in case of an error
     */
    @Override
    public void end(boolean interrupted) {
        train.setDriveMotorSpeeds(0.9, 0., 0.);
    }

    /**
     * Check to see if command is finished
     */
    @Override
    public boolean isFinished() {
        // return train.isAround(pidYAxis.getPositionError(), 0, 1.0) &&
        // train.isAround(pidZAxis.getPositionError(), 0, 0.5);
        return pidYAxis.atSetpoint();
    }
}