package frc.robot;

import frc.robot.gamepad.OI;
import frc.robot.subsystems.*;

public class RobotContainer {

  public static Training train;
  public static SensorSystem sensorSystem;
  public static ShuffleboardSystem shuffleboardSystem;
  public static MotorSystem motorSystem;
  public static OI oi;
  //public static OMS oms;

  public RobotContainer(MotorSystem motorSystem,SensorSystem sensorSystem,ShuffleboardSystem shuffleboardSystem) {
    /*sensorSystem = new SensorSystem();
    motorSystem = new MotorSystem(1, 2, 0);
    shuffleboardSystem = new ShuffleboardSystem();*/
    train = new Training(motorSystem, sensorSystem, shuffleboardSystem);
    //oms = new OMS();
    oi = new OI();
    // train.setDefaultCommand(new Autonomous(-1000,1,-80,0.1));

  }

public RobotContainer() {
}
}
