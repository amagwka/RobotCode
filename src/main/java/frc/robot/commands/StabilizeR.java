package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;

public class StabilizeR extends CommandBase {

    private double setpointYaw;

    private PIDController pidZAxis;

    private double out;
    private double previousAngle;
    private int counter = 0;
    private int counterAtSetpoint = 0;

    public StabilizeR(double epsilonYaw) {
        addRequirements(RobotContainer.train);
        pidZAxis = new PIDController(0.13, 0.0, 0.00);
        pidZAxis.setTolerance(epsilonYaw);
        pidZAxis.setIntegratorRange(-0.2, 0.2);
        previousAngle = RobotContainer.train.getSensorSystem().getAngle();
    }

    @Override
    public void initialize() {
        setpointYaw=RobotContainer.train.getSensorSystem().getYaw();
        pidZAxis.setSetpoint(setpointYaw);
    }

    @Override
    public void execute() {
        double angle = RobotContainer.train.getSensorSystem().getAngle();
        checkForAngleStability(angle);

        out = pidZAxis.calculate(angle, setpointYaw);
        drive();

        previousAngle = angle;
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
        //RobotContainer.train.getMotorSystem().setMotorSpeeds(leftSpeed, rightSpeed, 0);
        RobotContainer.train.getMotorSystem().holonomicDrive(0.0, 0.0, MathUtil.clamp(out, -0.4, 0.4));
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
        if (pidZAxis.atSetpoint() ) {
            counterAtSetpoint++;
            return counterAtSetpoint >= 10;
        }
        counterAtSetpoint = 0;
        return false;
    }
}
