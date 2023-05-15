package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ForwardEncoder;

public class Autonomous extends SequentialCommandGroup
{
    public Autonomous()
    {
        addCommands(
            
        //new ResetEncoder(),
        new ForwardEncoder(3000, 1, true),
        new ForwardEncoder(-3000, 1, true),
        new StopMotors());
      }
}