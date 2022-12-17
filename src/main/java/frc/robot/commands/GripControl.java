package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.subsystems.OMS;
import frc.robot.subsystems.Training;

public class GripControl extends CommandBase {
    private static final OMS oms = RobotContainer.oms;
    private double degree=0;
    public GripControl(double deg) {
        degree=deg;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
            oms.setGripperPosition(degree);
    }
    @Override
    public void end(boolean interrupted) {
        
    }
    @Override
    public boolean isFinished() {
        return true;
    }
}