package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Autonomous extends SequentialCommandGroup
{
    public Autonomous()
    {
        addCommands(
          new ResetAll(),
        new ForwardEncoder(0, 1,true)
        /*new ForwardEncoder(732, 1,false),
        new RotateEncoder(180, 1,false),
        new ForwardEncoder(732, 1,false),
        new StopMotors()*/);
      }
}