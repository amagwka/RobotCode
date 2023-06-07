package frc.robot.commands;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import static edu.wpi.first.wpiutil.math.MathUtil.clamp;

public class SideEncoder extends CommandBase {
    private double setpoint, lastEncoderValue, startTime;
    private boolean debug;
    private PIDController pidYAxis, pidZAxis;
    private int num, atSetpointCounter, encoderStuckCounter;

    public SideEncoder(double setpoint, double epsilon, boolean debug,int num) {
        this.setpoint = setpoint * 20;
        this.debug = debug;
        this.num = num;
        addRequirements(RobotContainer.train);
        pidYAxis = new PIDController(0.012, 0, 0);
        pidYAxis.setTolerance(epsilon);
        pidYAxis.setIntegratorRange(-0.4, 0.4);
        pidZAxis = new PIDController(0.10, 0.0, 0.0);
        pidZAxis.setIntegratorRange(-0.2, 0.2);
    }

    @Override
    public void initialize() {
        pidYAxis.setSetpoint(setpoint);
        pidYAxis.reset();
        pidZAxis.setSetpoint(RobotContainer.train.getSensorSystem().getAngle());
        startTime = Timer.getFPGATimestamp();
        RobotContainer.train.getShuffleboardSystem().getATO().setString(String.format("Side %d", num));
    }

    @Override
    public void execute() {
        double currentTime = Timer.getFPGATimestamp();
        double currentEncoderValue = RobotContainer.train.getMotorSystem().getAverageSideEncoderDistance();

        if ((currentTime - startTime >= 0.1) && debug) {
            pidYAxis.setSetpoint(RobotContainer.train.getShuffleboardSystem().getAdditionalValueOutput().getDouble(0) * 20);
            pidYAxis.setPID(RobotContainer.train.getShuffleboardSystem().getP().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getI().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getD().getDouble(0));
            startTime = currentTime;
        }
        // Cheaking new stuck of motor for adding Integral parameter
        if (currentEncoderValue == lastEncoderValue && !pidYAxis.atSetpoint() && !debug) {
            if (++encoderStuckCounter >= 10) {
                pidYAxis.reset();
                pidYAxis.setI(pidYAxis.getI()+0.15);
                encoderStuckCounter = 0;
            }
        } else {
            encoderStuckCounter = 0;
        }
        lastEncoderValue = currentEncoderValue;

        double outY = pidYAxis.calculate(currentEncoderValue);
        //double outZ = pidZAxis.calculate(RobotContainer.train.getSensorSystem().getAngle());
        double outZ = 0;
        RobotContainer.train.getMotorSystem().holonomicDrive(clamp(outY, -0.6, 0.6),0.0 , 0.0, 0.0,  clamp(outZ, -0.6, 0.6));

        if (debug) {
            RobotContainer.train.getShuffleboardSystem().updateTestString(String.format("S: %.1f SY: %.1f D: %.1f DY: %.1f",
            pidYAxis.getSetpoint(), pidZAxis.getSetpoint(),  currentEncoderValue, RobotContainer.train.getSensorSystem().getAngle()));
        }
    }

    @Override
    public void end(boolean interrupted) {
        RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
        RobotContainer.train.getMotorSystem().resetEncoders();
        RobotContainer.train.getSensorSystem().resetGyro();
        pidYAxis.close();
        pidZAxis.close();
    }

    @Override
    public boolean isFinished() {
        if (pidYAxis.atSetpoint() && !debug && ++atSetpointCounter >= 10) {
            return true;
        } else {
            atSetpointCounter = 0;
        }
        return false;
    }
}
