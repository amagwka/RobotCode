package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;

public class SideEncoder extends CommandBase {

    private double setpoint = 0;

    private double setpointYaw = 0;
    private boolean debug;

    PIDController pidXAxis;
    PIDController pidZAxis;

    int num=0;

    private int atSetpointCounter = 0;
    private int encoderStuckCounter = 0;

    private double INTEGRAL_ENABLED_I = 0.01;


    private double lastEncoderValue = 0.0;

    private boolean firstExecution = true;
    private double startTime;

    public SideEncoder(double setpoint, double epsilon, boolean debug,int num) {
        this.setpoint = -setpoint * 20;
        this.debug = debug;

        this.num = num;

        addRequirements(RobotContainer.train);

        pidXAxis = new PIDController(0.012, 0.0, 0.0);
        pidXAxis.setTolerance(epsilon);
        pidXAxis.setIntegratorRange(-0.5, 0.5);

        pidZAxis = new PIDController(0.05, 0.0, 0.0);
        pidZAxis.setIntegratorRange(-0.2, 0.2);
    }

    @Override
    public void initialize() {
        pidXAxis.setSetpoint(setpoint);
        pidXAxis.reset();
        setpointYaw = RobotContainer.train.getSensorSystem().getAngle();
        pidZAxis.setSetpoint(setpointYaw);
        startTime = Timer.getFPGATimestamp();
        firstExecution = true;
        RobotContainer.train.getShuffleboardSystem().getATO().setString(String.format("Side %d", num));
    }

    @Override
    public void execute() {
        double currentTime = Timer.getFPGATimestamp();
        double currentEncoderValue = RobotContainer.train.getMotorSystem().getAverageSideEncoderDistance();

        updateSetpointIfNeeded(currentTime, currentEncoderValue);
        checkAndCorrectStuckEncoder(currentEncoderValue);
        executePIDControl(currentEncoderValue);
    }

    // Called once the command ends or is interrupted
    @Override
    public void end(boolean interrupted) {
        RobotContainer.train.getMotorSystem().resetEncoders();
        RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
        RobotContainer.train.getSensorSystem().resetGyro();
        pidXAxis.close();
        pidZAxis.close();
    }

    void updateSetpointIfNeeded(double currentTime, double currentEncoderValue) {
        if ((currentTime - startTime >= 0.1 || firstExecution) && debug) {
            setpoint = RobotContainer.train.getShuffleboardSystem().getAdditionalValueOutput().getDouble(0) * 40;
            pidXAxis.setSetpoint(setpoint * 40);
            updatePID();
            startTime = currentTime;
            firstExecution = false;
        }
    }

    void checkAndCorrectStuckEncoder(double currentEncoderValue) {
        if (currentEncoderValue == lastEncoderValue && !pidXAxis.atSetpoint() && pidXAxis.getI() != INTEGRAL_ENABLED_I) {
            encoderStuckCounter++;
            if (encoderStuckCounter >= 5) {
                enableIntegral();
                encoderStuckCounter = 0;
            }
        } else {
            encoderStuckCounter = 0;
        }
        lastEncoderValue = currentEncoderValue;
    }

    void executePIDControl(double currentEncoderValue) {
        double outX = pidXAxis.calculate(currentEncoderValue, setpoint);
        double outZ = pidZAxis.calculate(RobotContainer.train.getSensorSystem().getAngle(), pidZAxis.getSetpoint());

        RobotContainer.train.getMotorSystem().holonomicDrive(- MathUtil.clamp(outX, -0.4, 0.4), 0.0, 0.0,
                MathUtil.clamp(outZ, -0.4, 0.4), 0.0);

        if (debug) {
            RobotContainer.train.getShuffleboardSystem().updateTestString(String.format("S: %.1f SY: %.1f D: %.1f DY: %.1f",
                    setpoint, setpointYaw, setpoint - currentEncoderValue, setpointYaw - RobotContainer.train.getSensorSystem().getAngle()));
        }
    }

    void updatePID() {
        pidXAxis.setPID(RobotContainer.train.getShuffleboardSystem().getP().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getI().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getD().getDouble(0));
    }

    void enableIntegral() {
        pidXAxis.reset();
        pidXAxis.setI(INTEGRAL_ENABLED_I);
        System.out.println("S Enabled I " + INTEGRAL_ENABLED_I);
    }

    @Override
    public boolean isFinished() {
        if (pidXAxis.atSetpoint() && !debug) {
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
