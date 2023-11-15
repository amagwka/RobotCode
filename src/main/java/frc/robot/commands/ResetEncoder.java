package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class ResetEncoder extends CommandBase {



    public ResetEncoder() {
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        RobotContainer.train.getMotorSystem().resetEncoders();
    }
    @Override
    public void end(boolean interrupted) {
        RobotContainer.train.getMotorSystem().resetEncoders();
    }
    @Override
    public boolean isFinished() {
        return true;
    }
}