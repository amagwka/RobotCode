package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.dist.ForwardIR;
import frc.robot.commands.dist.IRAlign;
import frc.robot.commands.dist.SideU;
import frc.robot.commands.dist.SideUMedian;

public class Autonomous extends SequentialCommandGroup
{
  private double p=1.0;
  private double w=364;
  private double h=160;
    public Autonomous()
    {
        addCommands(
          new ResetAll(),
          new IRAlign(20, 1.0, 1),
          new ResetAll(),
          //new IRAlign(12, 0.2, 1),
          //new ResetAll(),

          new IRAlign(12, 0.3, 1),
          new ResetAll(),
          
          new ForwardIR(20, 0.2, false, 1),
          new ResetAll(),
//frc.robot.commands.dist.SideU.SideU(double setpoint, double epsilon, boolean debug, int num)
          new SideUMedian(20),
          /*new ResetAll(),

          new SideEncoder(-160, p, false, 1),
          new ResetAll(),

          new SideEncoder(+80, p, false, 1),*/


          /*
          new ForwardEncoder(250, p, false,2),
          new ResetAll(),
          new SideEncoder(-80, p, false, 1),
          new ResetAll(),

          new ForwardEncoder(65, p, false,2),
          new ResetAll(),

          new SideEncoder(40, p, false, 1),
          new ResetAll(),
          new SideEncoder(-40, p, false, 1),
          new ResetAll(),

          new ForwardEncoder(-65, p, false,2),
          new ResetAll(),

          new SideEncoder(80+70, p, false, 1), //combined
          new ResetAll(),



          new ForwardEncoder(65, p, false,2),
          new ResetAll(),

          new SideEncoder(-40, p, false, 1),
          new ResetAll(),
          new SideEncoder(40, p, false, 1),
          new ResetAll(),

          new ForwardEncoder(-65, p, false,2),
          new ResetAll(),

          new SideEncoder(-70, p, false, 1),
          new ResetAll(),


          new ForwardEncoder(-250, p, false,2),
          new ResetAll(),

          /*
          new ForwardEncoder(h, p, false,2),
          new ResetAll(),
          new ForwardEncoder(-h/2, p, false,2),
          new ResetAll(),
          new RotateEncoder(-90, p, false,1),
          new ResetAll(),
          new ForwardEncoder(w, p, false,1),
          new ResetAll(),

          new RotateEncoder(90, p, false,1),
          new ResetAll(),
          new ForwardEncoder(h/2, p, false,2),
          new ResetAll(),
          new ForwardEncoder(-h, p, false,2),
          new ResetAll(),

          new ForwardEncoder(h/2, p, false,2),
          new ResetAll(),
          new RotateEncoder(-90, p, false,1),
          new ResetAll(),

          new ForwardEncoder(-w, p, false,1),
          new ResetAll(),
          new RotateEncoder(90, p, false,2),
          new ResetAll(),
          new ForwardEncoder(-h/2, p, false,3),
          new ResetAll(),*/
          //new RotateEncoder(180, 1, false,4)
          
          /*new RotateEncoder(90, 1, false,5),
          new ForwardEncoder(50, 5, false,6),
          new RotateEncoder(-90, 1, false,7),
          new ForwardEncoder(45, 5, false,8)*/
        
          //new ServoAuto(0, 0),
          //new ServoAuto(0, 180)
        /*new ForwardEncoder(732, 1,false),
        new RotateEncoder(180, 1,false),
        new ForwardEncoder(732, 1,false),*/
        new StopMotors(),
        new ResetAll());

      }
}