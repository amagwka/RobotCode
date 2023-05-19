package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;

public class RotateEncoder extends CommandBase {

    private double setpointYaw;
    private boolean debug;

    private PIDController pidZAxis;

    private double out;
    private double previousAngle;
    private int counter = 0;
    private int counterAtSetpoint = 0;

    public RotateEncoder(double setpointYawArg, double epsilonYaw, boolean debug) {
        setpointYaw = setpointYawArg;
        this.debug = debug;
        addRequirements(RobotContainer.train);
        pidZAxis = new PIDController(0.13, 0.0, 0.00);
        pidZAxis.setTolerance(epsilonYaw);
        pidZAxis.setIntegratorRange(-0.2, 0.2);
        previousAngle = RobotContainer.train.getSensorSystem().getAngle();
    }

    @Override
    public void initialize() {
        pidZAxis.setSetpoint(setpointYaw);
    }

    @Override
    public void execute() {
        double currentSetpointYaw = getCurrentSetpointYaw();
        updateYawAndPIDIfNecessary(currentSetpointYaw);

        double angle = RobotContainer.train.getSensorSystem().getAngle();
        checkForAngleStability(angle);

        out = pidZAxis.calculate(angle, setpointYaw);
        drive();

        updateDebugInfoIfNecessary(angle);
        previousAngle = angle;
    }

    private double getCurrentSetpointYaw() {
        return debug ? RobotContainer.train.getShuffleboardSystem().getAdditionalValueOutput().getDouble(0) : setpointYaw;
    }

    private void updateYawAndPIDIfNecessary(double currentSetpointYaw) {
        if (debug && setpointYaw != currentSetpointYaw) {
            setpointYaw = currentSetpointYaw;
            pidZAxis.setSetpoint(setpointYaw);
        }
        updatePID();
    }

    private void checkForAngleStability(double angle) {
        if (Math.abs(angle - previousAngle) <= 1) {
            counter++;
        } else {
            counter = 0;
        }
        if (counter >= 10) {
            enableIntegral();
            counter = 0;
        }
    }

    private void drive() {
        RobotContainer.train.getMotorSystem().holonomicDrive(0.0, 0.0, MathUtil.clamp(out, -0.3, 0.3));
    }

    private void updateDebugInfoIfNecessary(double angle) {
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

    private void enableIntegral() {
        pidZAxis.reset();
        pidZAxis.setI(0.01);
    }

    @Override
    public boolean isFinished() {
        if (pidZAxis.atSetpoint() && !debug) {
            counterAtSetpoint++;
            return counterAtSetpoint >= 10;
        }
        counterAtSetpoint = 0;
        return false;
    }
}
