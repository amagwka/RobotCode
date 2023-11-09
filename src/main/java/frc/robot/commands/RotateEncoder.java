package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;

public class RotateEncoder extends CommandBase {

    private static final double PID_KP = 0.11;
    private static final double PID_KI = 0.001;
    private static final double PID_KD = 0.0;
    private static final double INTEGRAL_ENABLED_I = 0.01;
    private static final double INTEGRATOR_RANGE_MIN = -0.4;
    private static final double INTEGRATOR_RANGE_MAX = 0.4;
    private static final int ANGLE_STABILITY_THRESHOLD = 50;
    private static final int AT_SETPOINT_THRESHOLD = 10;
    private static final double DRIVE_MIN = 0.0;
    private static final double DRIVE_MAX = 0.4;

    private boolean debug;
    private PIDController pidZAxis;
    private double out;
    private double previousAngle;
    private int counter = 0;
    int num=0;
    private int counterAtSetpoint = 0;

    public RotateEncoder(double setpointYawArg, double epsilonYaw, boolean debug,int num) {
        this.debug = debug;
        addRequirements(RobotContainer.train);
        this.num = num;
        pidZAxis = new PIDController(PID_KP, PID_KI, PID_KD);
        pidZAxis.setSetpoint(setpointYawArg);
        pidZAxis.setTolerance(epsilonYaw);
        pidZAxis.setIntegratorRange(INTEGRATOR_RANGE_MIN, INTEGRATOR_RANGE_MAX);
    }

    @Override
    public void initialize() {
        RobotContainer.train.getShuffleboardSystem().getATO().setString(String.format("Rotate %d", num));
        previousAngle = RobotContainer.train.getSensorSystem().getAngle();
    }

    @Override
    public void execute() {
        double currentSetpointYaw = getCurrentSetpointYaw();
        updateYawAndPIDIfNecessary(currentSetpointYaw);

        double angle = RobotContainer.train.getSensorSystem().getAngle();
        checkForAngleStability(angle);

        out = pidZAxis.calculate(angle);
        drive();

        updateDebugInfoIfNecessary(angle);
        previousAngle = angle;
    }

    private double getCurrentSetpointYaw() {
        return debug ? RobotContainer.train.getShuffleboardSystem().getAdditionalValueOutput().getDouble(0) : pidZAxis.getSetpoint();
    }

    private void updateYawAndPIDIfNecessary(double currentSetpointYaw) {
        if (debug) {
            pidZAxis.setSetpoint(currentSetpointYaw);
            updatePID();
        }
    }

    private void checkForAngleStability(double angle) {
        if(pidZAxis.getI() != INTEGRAL_ENABLED_I){
            if (Math.abs(angle - previousAngle) <= 1) {
                counter++;
            } else {
                counter = 0;
            }
            if (counter >= ANGLE_STABILITY_THRESHOLD) {
                enableIntegral();
                counter = 0;
            }
        }
    }

    private void drive() {
        RobotContainer.train.getMotorSystem().holonomicDrive(DRIVE_MIN, DRIVE_MIN, MathUtil.clamp(out, - DRIVE_MAX, DRIVE_MAX));
    }

    private void updateDebugInfoIfNecessary(double angle) {
        if (debug)
            RobotContainer.train.getShuffleboardSystem()
                .updateTestString(String.format("S: %.2f O: %.2f A: %.2f", pidZAxis.getSetpoint(), out, angle));
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
        System.out.println("Rotate Integral enabled " + INTEGRAL_ENABLED_I);
        pidZAxis.setI(INTEGRAL_ENABLED_I);
    }

    @Override
    public boolean isFinished() {
        if (pidZAxis.atSetpoint() && !debug) {
            counterAtSetpoint++;
            return counterAtSetpoint >= AT_SETPOINT_THRESHOLD;
        }else{
            counterAtSetpoint = 0;
        }
        
        return false;
    }
}
