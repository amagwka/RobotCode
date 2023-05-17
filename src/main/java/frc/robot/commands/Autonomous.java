package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ForwardEncoder;

public class Autonomous extends SequentialCommandGroup
{
    public Autonomous()
    {
        addCommands(
          new ResetAll(),
        //new RotateEncoder(-90, 1, false,false),
        new ForwardEncoder(732*5, 5,false),
        new RotateEncoder(180, 5,false),
        new ForwardEncoder(732*5, 5,false),
        new StopMotors());
      }
}