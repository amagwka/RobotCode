package frc.robot.commands;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.subsystems.OMS;
import frc.robot.subsystems.Training;

public class Gripping extends CommandBase {
    private static final OMS oms = RobotContainer.oms;

    private boolean grip;
    PIDController pidYAxis;


    public Gripping(boolean grip) {
        this.grip = grip;
        addRequirements(oms);

        pidYAxis = new PIDController(1.0, 0.5, 1.0);
        //pidYAxis.setTolerance(epsilonDistance);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
            //train.holonomicDrive(0.0, MathUtil.clamp(pidYAxis.calculate(train.getAverageForwardEncoderDistance(), setpointDistance), -0.7, 0.7),0.0);
    }
    @Override
    public void end(boolean interrupted) {
        //train.setDriveMotorSpeeds(0.9, 0., 0.);
    }
    @Override
    public boolean isFinished() {
        return pidYAxis.atSetpoint();
    }
}