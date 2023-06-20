package frc.robot.commands.dist;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;

public class ForwardIR extends CommandBase {

    private double setpoint = 0;

    private double setpointYaw = 0;
    private boolean debug;

    PIDController pidYAxis;
    PIDController pidZAxis;

    int num=0;

    private int atSetpointCounter = 0;
    private int encoderStuckCounter = 0;

    private double lastEncoderValue = 0.0;

    private boolean firstExecution = true;
    private double startTime;

    public ForwardIR(double setpoint, double epsilon, boolean debug,int num) {
        this.setpoint = setpoint * 20;
        this.debug = debug;
        
        this.num = num;

        addRequirements(RobotContainer.train);

        pidYAxis = new PIDController(0.012, 0.0, 0.0);
        pidYAxis.setTolerance(epsilon);
        pidYAxis.setIntegratorRange(-0.4, 0.4);

        pidZAxis = new PIDController(0.10, 0.0, 0.0);
        pidZAxis.setIntegratorRange(-0.2, 0.2);
    }

    @Override
    public void initialize() {
        pidYAxis.setSetpoint(setpoint);
        pidYAxis.reset();
        setpointYaw = RobotContainer.train.getSensorSystem().getAngle();
        pidZAxis.setSetpoint(setpointYaw);
        startTime = Timer.getFPGATimestamp();
        firstExecution = true;
        RobotContainer.train.getShuffleboardSystem().getATO().setString(String.format("Forward %d", num));
    }

    @Override
    public void execute() {
        double currentTime = Timer.getFPGATimestamp();
        double currentEncoderValue = RobotContainer.train.getMotorSystem().getAverageForwardEncoderDistance();

        updateSetpointIfNeeded(currentTime, currentEncoderValue);
        checkAndCorrectStuckEncoder(currentEncoderValue);
        executePIDControl(currentEncoderValue);
    }

    // Called once the command ends or is interrupted
    @Override
    public void end(boolean interrupted) {
        RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
        RobotContainer.train.getMotorSystem().resetEncoders();
        RobotContainer.train.getSensorSystem().resetGyro();
        pidYAxis.close();
        pidZAxis.close();
    }

    void updateSetpointIfNeeded(double currentTime, double currentEncoderValue) {
        if ((currentTime - startTime >= 0.1 || firstExecution) && debug) {
            setpoint = RobotContainer.train.getShuffleboardSystem().getAdditionalValueOutput().getDouble(0) * 40;
            pidYAxis.setSetpoint(setpoint * 40);
            updatePID();
            startTime = currentTime;
            firstExecution = false;
        }
    }

    void checkAndCorrectStuckEncoder(double currentEncoderValue) {
        if (currentEncoderValue == lastEncoderValue && !pidYAxis.atSetpoint()) {
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
        double outY = pidYAxis.calculate(currentEncoderValue, setpoint);
        double outZ = pidZAxis.calculate(RobotContainer.train.getSensorSystem().getAngle(), pidZAxis.getSetpoint());

        RobotContainer.train.getMotorSystem().holonomicDrive(0.0, MathUtil.clamp(outY, -0.6, 0.6), 0.0,
                MathUtil.clamp(outZ, -0.6, 0.6), 0.0);

        if (debug) {
            RobotContainer.train.getShuffleboardSystem().updateTestString(String.format("S: %.1f SY: %.1f D: %.1f DY: %.1f",
                    setpoint, setpointYaw, setpoint - currentEncoderValue, setpointYaw - RobotContainer.train.getSensorSystem().getAngle()));
        }
    }

    void updatePID() {
        pidYAxis.setPID(RobotContainer.train.getShuffleboardSystem().getP().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getI().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getD().getDouble(0));
    }

    void enableIntegral() {
        pidYAxis.reset();
        
        pidYAxis.setI(0.15);
    }

    @Override
    public boolean isFinished() {
        if (pidYAxis.atSetpoint() && !debug) {
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
