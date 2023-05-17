package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Training;

public class RotateEncoder extends CommandBase {

    private double setpointYaw;
    private boolean debug;

    private PIDController pidZAxis;

    private double out;

    public RotateEncoder(double setpointYawArg, double epsilonYaw, boolean debug) {
        setpointYaw = setpointYawArg;
        this.debug = debug;
        addRequirements(RobotContainer.train);
        pidZAxis = new PIDController(0.1, 0.0001, 0.00);
        pidZAxis.setTolerance(epsilonYaw);
        pidZAxis.setIntegratorRange(-3, 3);
    }

    @Override
    public void initialize() {
        pidZAxis.setSetpoint(setpointYaw);
    }

    @Override
    public void execute() {
        double currentSetpointYaw = debug
                ? RobotContainer.train.getShuffleboardSystem().getAdditionalValueOutput().getDouble(0)
                : setpointYaw;
        if ((setpointYaw != currentSetpointYaw) && debug) {
            setpointYaw = currentSetpointYaw;
            pidZAxis.setSetpoint(setpointYaw);
            updatePID();
        }
        double angle = RobotContainer.train.getSensorSystem().getAngle();
        out = pidZAxis.calculate(angle, setpointYaw);
        RobotContainer.train.getMotorSystem().holonomicDrive(0.0, 0.0, MathUtil.clamp(out, -0.3, 0.3));
        if (debug)
            RobotContainer.train.getShuffleboardSystem()
                    .updateTestString(String.format("S: %.2f O: %.2f A: %.2f", setpointYaw, out, angle));
    }

    @Override
    public void end(boolean interrupted) {
        RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
        pidZAxis.close();
    }

    private void updatePID() {
        pidZAxis.setPID(RobotContainer.train.getShuffleboardSystem().getP().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getI().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getD().getDouble(0));
    }

    @Override
    public boolean isFinished() {
        return pidZAxis.atSetpoint();
    }
}
