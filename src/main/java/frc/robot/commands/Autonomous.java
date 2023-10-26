package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Autonomous extends SequentialCommandGroup
{
    public Autonomous()
    {
        addCommands(
          new ResetAll(),
          new ForwardEncoder(135, 1, false,2),
          new ResetAll(),
          new ForwardEncoder(-40, 1, false,2),
          new ResetAll(),
          new RotateEncoder(-90, 1, false,1),
          new ResetAll(),
          new ForwardEncoder(300, 1, false,1),
          new ResetAll(),

          new RotateEncoder(90, 1, false,1),
          new ResetAll(),
          new ForwardEncoder(70, 1, false,2),
          new ResetAll(),
          new ForwardEncoder(-140, 1, false,2),
          new ResetAll(),
          
          new ForwardEncoder(70, 1, false,2),
          new ResetAll(),
          new RotateEncoder(-90, 1, false,1),
          new ResetAll(),

          new ForwardEncoder(-300, 1, false,1),
          new ResetAll(),
          new RotateEncoder(90, 1, false,2),
          new ResetAll(),
          new ForwardEncoder(-95, 1, false,3),
          new ResetAll(),
          //new RotateEncoder(180, 1, false,4)
          
          /*new RotateEncoder(90, 1, false,5),
          new ForwardEncoder(50, 5, false,6),
          new RotateEncoder(-90, 1, false,7),
          new ForwardEncoder(45, 5, false,8)*/

          //new ServoAuto(0, 0),
          //new ServoAuto(0, 180)
        /*new ForwardEncoder(732, 1,false),
        new RotateEncoder(180, 1,false),
        new ForwardEncoder(732, 1,false),
        new StopMotors()*/
        new ResetAll());
      }
}