package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.Teleop;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.DriveBase;

public class RobotContainer {
  public static DriveBase driveBase;
  public static OI oi;

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    driveBase = new DriveBase();
    oi = new OI();
    driveBase.setDefaultCommand(new Teleop());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return null;
  }
}
