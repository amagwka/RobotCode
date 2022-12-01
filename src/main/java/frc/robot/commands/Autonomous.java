package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Training;
import frc.robot.commands.ForwardEncoder;

public class Autonomous extends SequentialCommandGroup
{
    public Autonomous(double setpointDistance, double epsilonDistance, double setpointYaw, double epsilonYaw)
    {
        addCommands(
            new ForwardEncoder(100,1,true)
            );
      }
}