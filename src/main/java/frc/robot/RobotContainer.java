/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.gamepad.OI;
import frc.robot.subsystems.OMS;
import frc.robot.subsystems.Training;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.Autonomous;
import frc.robot.commands.Drive;

public class RobotContainer {

  /**
   * Create the subsystems and gamepad objects
   */
  
  public static Training train;
  public static OI oi;
  public static OMS oms;

  public RobotContainer(){
      //Create new instances
      //autonomousCommand = new Autonomous(-1000,1,-80,0.1);
      //drive=new Drive();
      train = new Training();
      oms = new OMS();
      oi = new OI();
      //train.setDefaultCommand(new Autonomous(-1000,1,-80,0.1));
      
  }/*
  public CommandBase getTeleopCommand() {
    return drive;
  }
  public CommandBase getAutonomousCommand() {
    return autonomousCommand;
  }*/
}
