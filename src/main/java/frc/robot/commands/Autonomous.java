package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ForwardEncoder;

public class Autonomous extends SequentialCommandGroup
{
    public Autonomous()
    {
        addCommands(
            
        new ResetEncoder(),
        //new ForwardEncoder(+1000, 1, true),
       // new ForwardEncoder(-1000, 1, true),*/
        //new ForwardEncoder(-500, 1, true),
        //new ForwardEncoder(1000,1,true),
        
        new RotateEncoder(540, 0.1, true, true),
        new RotateEncoder(-540, 0.1, true, true), // two starts with clearing
        new ForwardEncoder(600,1,true), // exiting 1 step
        new RotateEncoder(-90, 0.05, true, true), // exiting 2 step
        
        new ForwardEncoder(2200,1,true), // cubes town view
        new RotateEncoder(90, 0.05, true, true), // cubes 
        new ForwardEncoder(800,1,true), 
        new RotateEncoder(-90, 0.1, true, true),
        new ForwardEncoder(400,1,true), 
        //Traveling
        new RotateEncoder(180, 0.1, true, true), // cubes 
        new ForwardEncoder(500,1,true), 
        new RotateEncoder(-90, 0.1, true, true),
        new ForwardEncoder(2000,1,true), 
        //
        new RotateEncoder(-90, 0.1, true, true),
        new ForwardEncoder(800,1,true),
        new RotateEncoder(180, 0.1, true, true),
        new ForwardEncoder(1500,1,true),

        new RotateEncoder(180, 0.1, true, true),
        new ForwardEncoder(800,1,true),
        new RotateEncoder(-90, 0.1, true, true),
        new ForwardEncoder(4000,1,true),

        new RotateEncoder(180, 0.1, true, true),
        new ForwardEncoder(750,1,true),
        new RotateEncoder(90, 0.1, true, true),
        new ForwardEncoder(950,1,true),
        new RotateEncoder(90, 0.1, true, true),
        new ForwardEncoder(500,1,true),


        new StopMotors());
      }
}