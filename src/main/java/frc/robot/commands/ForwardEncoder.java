package frc.robot.commands;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Training;

public class ForwardEncoder extends CommandBase {
    private static final Training train = RobotContainer.train;

    private double setpointDistance=0;
    PIDController pidYAxis;


    public ForwardEncoder(double setpointDistance, double epsilonDistance, double setpointYaw, double epsilonYaw,
            boolean delta) {
        this.setpointDistance = setpointDistance;
        this.setpointDistance += train.getAverageForwardEncoderDistance();
        addRequirements(train);

        pidYAxis = new PIDController(1.0, 0.5, 1.0);
        pidYAxis.setTolerance(epsilonDistance);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
            train.holonomicDrive(0.0, MathUtil
                    .clamp(pidYAxis.calculate(train.getAverageForwardEncoderDistance(), setpointDistance), -0.7, 0.7),0.0);
    }
    @Override
    public void end(boolean interrupted) {
        train.setDriveMotorSpeeds(0.9, 0., 0.);
    }
    @Override
    public boolean isFinished() {
        return pidYAxis.atSetpoint();
    }
}