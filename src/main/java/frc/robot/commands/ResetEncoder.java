package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Training;

public class ResetEncoder extends CommandBase {
    private static final Training train = RobotContainer.train;



    public ResetEncoder() {
    }

    @Override
    public void initialize() {
        train.resetLeftEncoder();
        train.resetBackEncoder();
        train.resetRightEncoder();
        train.resetGyro();
        
    }

    @Override
    public void execute() {
            
    }
    @Override
    public void end(boolean interrupted) {

    }
    @Override
    public boolean isFinished() {
        return true;
    }
}