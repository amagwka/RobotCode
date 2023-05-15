/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.Autonomous;
import frc.robot.commands.Drive;
//import frc.robot.commands.ForwardEncoder;
//import frc.robot.commands.RotateEncoder;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private RobotContainer m_robotContainer;
  Drive drive;
  Autonomous auto;

  @Override
  public void robotInit() {
    // Instantiate our RobotContainer. 
    m_robotContainer = new RobotContainer();
    drive = new Drive();
    auto = new Autonomous();
    
    new Thread(()->{
      UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
      camera.setResolution(640, 480);
      camera.setFPS(30);

      CvSink cvSink = CameraServer.getInstance().getVideo();
      CvSource outputStream = CameraServer.getInstance().putVideo("GrayScale", 640, 480);

      Mat source = new Mat();
      //Mat output = new Mat();

      while(!Thread.interrupted()){
        if(cvSink.grabFrame(source) == 0)
          continue;
        //Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
        //outputStream.putFrame(output);
      }
    }).start();
  }
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }
  
  @Override
  public void disabledInit() {
    
  }

  @Override
  public void disabledPeriodic() {
    
  }
  
  @Override
  public void autonomousInit() {
    CommandScheduler.getInstance()
    .schedule(auto);
  }
  
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    CommandScheduler.getInstance().schedule(drive);
  }
  
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }
  @Override
  public void testPeriodic() {
  }
}
