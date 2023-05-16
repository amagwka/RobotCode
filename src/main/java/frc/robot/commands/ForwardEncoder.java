package frc.robot.commands;

import java.text.MessageFormat;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.pid.AutoPIDController;
import frc.robot.pid.PIDOutput;
import frc.robot.subsystems.Training;

public class ForwardEncoder extends CommandBase {

    private double setpointDistanceArgument;
    private double setpointDistance;
    public AutoPIDController pidYAxis;

    public ForwardEncoder(double setpointDistanceArg, double epsilonDistance, boolean delta) {
        setpointDistanceArgument = setpointDistanceArg;
        addRequirements(RobotContainer.train);
    }

    @Override
    public void initialize() {
        setpointDistance = RobotContainer.train.getMotorSystem().getAverageForwardEncoderDistance() + setpointDistanceArgument;
        pidYAxis = new AutoPIDController(10, setpointDistance, 300);
    }

    @Override
    public void execute() {
        double forwardEncoderDistance = RobotContainer.train.getMotorSystem().getAverageForwardEncoderDistance();
        PIDOutput arr = pidYAxis.calculate(forwardEncoderDistance, setpointDistance);
        RobotContainer.train.getShuffleboardSystem().updateTestString(String.format("F: %.2f S: %.2f O: %.2f",arr.getClampedOutput(),setpointDistance,arr.getOutput()));
        RobotContainer.train.getMotorSystem().holonomicDrive(0.0, arr.getClampedOutput(), 0.0);
    }

    @Override
    public void end(boolean interrupted) {
        // train.setDriveMotorSpeeds(0.0, 0.0, 0.0);
        RobotContainer.train.getShuffleboardSystem().updateTestString(String.format("P: %.5f I: %.5f D: %.5f",pidYAxis.pidController.getP(),pidYAxis.pidController.getI(),pidYAxis.pidController.getD()));
        Timer.delay(0.1);
    }

    @Override
    public boolean isFinished() {
        return pidYAxis.atSetpoint();
    }
}