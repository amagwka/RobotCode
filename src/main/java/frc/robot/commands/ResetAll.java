package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Training;

public class ResetAll extends CommandBase {
    public ResetAll() {
        
    }

    @Override
    public void initialize() {
        RobotContainer.train.getMotorSystem().resetEncoders();
        RobotContainer.train.getSensorSystem().resetGyro();
        RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
    }

    @Override
    public void execute() {
        RobotContainer.train.getMotorSystem().resetEncoders();
        RobotContainer.train.getSensorSystem().resetGyro();
        RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
    }
    @Override
    public void end(boolean interrupted) {
        RobotContainer.train.getMotorSystem().resetEncoders();
        RobotContainer.train.getSensorSystem().resetGyro();
        RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
    }
    @Override
    public boolean isFinished() {
        return true;
    }
}