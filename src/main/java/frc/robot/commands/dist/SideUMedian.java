package frc.robot.commands.dist;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.commands.ResetAll;
import frc.robot.commands.ResetEncoder;

public class SideUMedian extends CommandBase {


    boolean right = true;

    SequentialCommandGroup group;

    private double setpoint;

    public SideUMedian(double setpoint) {
        this.setpoint = setpoint;
    }

    @Override
    public void initialize() {
        group = new SequentialCommandGroup(
            new SideU(20, 0.5, false, 2),
            new ResetEncoder(),
            new SideU(-20, 0.5, false, 2)
            );
        group.schedule();
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {
        System.out.print("Distance: ");
        System.out.println(RobotContainer.train.getMotorSystem().getAverageSideEncoderDistance());
        System.out.print("Scaled distance: ");
        System.out.println(RobotContainer.train.getMotorSystem().getAverageSideEncoderDistance()/20);
        RobotContainer.train.getMotorSystem().resetEncoders();
        RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
        RobotContainer.train.getSensorSystem().resetGyro();
    }


    @Override
    public boolean isFinished() {
        return group.isFinished();
    }
}
