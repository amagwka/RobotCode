package frc.robot;

import frc.robot.commands.Drive;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.Training;

public class RobotContainer {
  public static OI oi;

  public static Training train;

  public RobotContainer() {
    train = new Training();
    oi = new OI();
  }
}
