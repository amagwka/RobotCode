package frc.robot.commands;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Training;

public class StopMotors extends CommandBase {
    private static final Training train = RobotContainer.train;



    public StopMotors() {
    }

    @Override
    public void initialize() {
        train.setDriveMotorSpeeds(0.0, 0.0, 0.0);
    }

    @Override
    public void execute() {
            
    }
    @Override
    public void end(boolean interrupted) {
        train.setDriveMotorSpeeds(0.0, 0.0, 0.0);
    }
    @Override
    public boolean isFinished() {
        return true;
    }
}