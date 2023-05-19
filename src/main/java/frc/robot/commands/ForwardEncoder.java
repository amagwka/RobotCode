package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;

public class ForwardEncoder extends CommandBase {

    private double setpoint;
    private boolean debug;

    PIDController pidZAxis;

    private int atSetpointCounter = 0;
    private int encoderStuckCounter = 0;
    private double lastEncoderValue = 0.0;

    private boolean firstExecution = true;
    private double startTime;

    public ForwardEncoder(double setpoint, double epsilon, boolean debug) {
        this.setpoint = setpoint*40;
        this.debug = debug;

        addRequirements(RobotContainer.train);

        pidZAxis = new PIDController(0.012, 0.0, 0.0);
        pidZAxis.setTolerance(epsilon);
        pidZAxis.setIntegratorRange(-0.2, 0.2);
    }

    @Override
    public void initialize() {
        pidZAxis.setSetpoint(setpoint);
        startTime = Timer.getFPGATimestamp();
        firstExecution = true;
    }

    @Override
    public void execute() {
        double currentTime = Timer.getFPGATimestamp();
        double currentEncoderValue = RobotContainer.train.getMotorSystem().getAverageForwardEncoderDistance();

        updateSetpointIfNeeded(currentTime, currentEncoderValue);
        checkAndCorrectStuckEncoder(currentEncoderValue);
        executePIDControl(currentEncoderValue);
        // RobotContainer.train.getShuffleboardSystem().getUltrasonic().setDouble(RobotContainer.train.getSensorSystem().getSonicDistance(true));
    }

    @Override
    public void end(boolean interrupted) {
        RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
        RobotContainer.train.getMotorSystem().resetEncoders();
        pidZAxis.close();
    }

    void updateSetpointIfNeeded(double currentTime, double currentEncoderValue) {
        if ((currentTime - startTime >= 0.1 || firstExecution) && debug) {
            setpoint = RobotContainer.train.getShuffleboardSystem().getAdditionalValueOutput().getDouble(0);
            pidZAxis.setSetpoint(setpoint*40);
            updatePID();
            startTime = currentTime;
            firstExecution = false;
        }
    }

    void checkAndCorrectStuckEncoder(double currentEncoderValue) {
        if (currentEncoderValue == lastEncoderValue && !pidZAxis.atSetpoint()) {
            encoderStuckCounter++;
            if (encoderStuckCounter >= 10) {
                enableIntegral();
                encoderStuckCounter = 0;
            }
        } else {
            encoderStuckCounter = 0;
        }

        lastEncoderValue = currentEncoderValue;
    }

    void executePIDControl(double currentEncoderValue) {
        double out = pidZAxis.calculate(currentEncoderValue, setpoint);
        RobotContainer.train.getMotorSystem().holonomicDrive(0.0, MathUtil.clamp(out, -0.6, 0.6), 0.0);

        if (debug) {
            RobotContainer.train.getShuffleboardSystem().updateTestString(String.format("S: %.2f O: %.2f A: %.2f",
                    setpoint, out, RobotContainer.train.getSensorSystem().getAngle()));
        }
    }

    void updatePID() {
        pidZAxis.setPID(RobotContainer.train.getShuffleboardSystem().getP().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getI().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getD().getDouble(0));
    }

    void enableIntegral() {
        pidZAxis.reset();
        pidZAxis.setI(0.002);
    }

    @Override
    public boolean isFinished() {
        if (pidZAxis.atSetpoint() && !debug) {
            atSetpointCounter++;
            if (atSetpointCounter >= 10) {
                return true;
            }
        } else {
            atSetpointCounter = 0;
        }
        return false;
    }
}
