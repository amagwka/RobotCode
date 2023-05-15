package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Training;

public class ForwardEncoder extends CommandBase {
    private static final Training train = RobotContainer.train;

    private double setpointDistanceArgument;
    private double setpointDistance;
    private double smoothLimit;
    private double epsilonDistanceC;
    public PIDController pidYAxis;

    public ForwardEncoder(double setpointDistanceArg, double epsilonDistance, boolean delta) {
        setpointDistanceArgument = setpointDistanceArg;
        epsilonDistanceC = epsilonDistance;
        addRequirements(train);
    }

    @Override
    public void initialize() {
        setpointDistance = train.getAverageForwardEncoderDistance() + setpointDistanceArgument;
        smoothLimit = 0.05;
        pidYAxis = new PIDController(0.01, 10, 0.0);
        pidYAxis.setTolerance(epsilonDistanceC);
        pidYAxis.setIntegratorRange(setpointDistance - 300, setpointDistance + 300);
    }

    @Override
    public void execute() {
        double forwardEncoderDistance = train.getAverageForwardEncoderDistance();
        double yAxisOutput = pidYAxis.calculate(forwardEncoderDistance, setpointDistance);
        double clampedYAxisOutput = MathUtil.clamp(yAxisOutput, -smoothLimit, smoothLimit);
        train.holonomicDrive(0.0, clampedYAxisOutput, 0.0);

        if (smoothLimit < 1) {
            smoothLimit += 0.01;
        }
        if (Math.abs(pidYAxis.getPositionError()) < 800 && smoothLimit > 0.15) {
            smoothLimit -= 0.03;
        }
    }

    @Override
    public void end(boolean interrupted) {
        // train.setDriveMotorSpeeds(0.0, 0.0, 0.0);
    }

    @Override
    public boolean isFinished() {
        return pidYAxis.atSetpoint();
    }
}