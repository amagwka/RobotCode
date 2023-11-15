/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.Autonomous;
import frc.robot.commands.Drive;
import frc.robot.commands.StopMotors;
//import frc.robot.commands.ForwardEncoder;
//import frc.robot.commands.RotateEncoder;
import frc.robot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private RobotContainer m_robotContainer;
  DigitalInput b_start;
  DigitalInput b_stop;

  Drive drive;
  Autonomous auto;
  StopMotors sMotors;

  //private final Notifier notifier;

  public Robot() {
    //super(0.02);
    //notifier = new Notifier(this::customPeriodic);
    //notifier.startPeriodic(0.05);  // Set the period to 50ms
  }

  private boolean lastStartState = true;
  private boolean lastStopState = true;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer(new MotorSystem(2, 0, 3), new SensorSystem(), new ShuffleboardSystem());
    drive = new Drive();
    auto = new Autonomous();
    sMotors = new StopMotors();
    b_stop = new DigitalInput(0);
    b_start = new DigitalInput(1);

    /*
    new Thread(() -> {
      UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
      camera.setResolution(640, 480);
      camera.setFPS(30);

      CvSink cvSink = CameraServer.getInstance().getVideo();
      CvSource outputStream = CameraServer.getInstance().putVideo("MK", 640, 480);

      Mat source = new Mat();
      // Mat output = new Mat();

      while (!Thread.interrupted()) {
        if (cvSink.grabFrame(source) == 0)
          continue;
        // Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
        outputStream.putFrame(source);
      }
    }).start();*/
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    CommandScheduler.getInstance().schedule(auto);

  }

  @Override
  public void autonomousPeriodic() {
    boolean currentStartState = b_start.get();
    boolean currentStopState = b_stop.get();
  
    if (!currentStartState && lastStartState) {
        CommandScheduler.getInstance().schedule(auto);
        System.out.println("Scheduled");
    }
  
    if (!currentStopState && lastStopState) {
      sMotors.initialize();
      CommandScheduler.getInstance().cancelAll();
      System.out.println("Cancel");
    }
  
    lastStartState = currentStartState;
    lastStopState = currentStopState;
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
//     CommandScheduler.getInstance().cancelAll();
//     try {
//       BufferedWriter writer = new BufferedWriter(new FileWriter("f.txt"));

//       writer.write("This is a test line.");
//       //writer.newLine();

//       writer.close();
//   } catch (IOException e) {
//       e.printStackTrace();
//   }

//   try {
//     BufferedReader reader = new BufferedReader(new FileReader("f.txt"));
//     String line;
//     line = reader.readLine();
//     DriverStation.reportWarning(line,false );
//     System.out.println(line);
//     reader.close();
// } catch (IOException e) {
//     e.printStackTrace();
// }
  
    //CommandScheduler.getInstance().cancelAll();
  }
  @Override
  public void testPeriodic() {
    boolean currentStartState = b_start.get();
    boolean currentStopState = b_stop.get();
  
    if (!currentStartState && lastStartState) {
        //CommandScheduler.getInstance().schedule(auto);
        System.out.println("Scheduled");
    }
  
    if (!currentStopState && lastStopState) {
      //sMotors.initialize();
      //CommandScheduler.getInstance().cancelAll();
      System.out.println("Cancel");
    }
  
    lastStartState = currentStartState;
    lastStopState = currentStopState;

  }
  @Override
  public void disabledInit() {
    super.disabledInit();
  }

  @Override
  public void disabledPeriodic() {
  }
}
