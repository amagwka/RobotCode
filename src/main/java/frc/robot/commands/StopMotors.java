package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Training;

public class StopMotors extends CommandBase {
    private static final Training train = RobotContainer.train;



    public StopMotors() {
    }

    @Override
    public void initialize() {
        train.getMotorSystem().setMotorSpeeds(0.0, 0.0, 0.0);
    }

    @Override
    public void execute() {
        train.getMotorSystem().setMotorSpeeds(0.0, 0.0, 0.0);
    }

    @Override
    public void end(boolean interrupted) {
        train.getMotorSystem().setMotorSpeeds(0.0, 0.0, 0.0);
    }
    @Override
    public boolean isFinished() {
        return true;
    }
}