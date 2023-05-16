package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ForwardEncoder;

public class Autonomous extends SequentialCommandGroup
{
    public Autonomous()
    {
        addCommands(
        new ForwardEncoder(732*1, 5, true),
        new ForwardEncoder(-732*2, 5, true),
        new StopMotors());
      }
}