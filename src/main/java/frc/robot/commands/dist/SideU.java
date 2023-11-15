package frc.robot.commands.dist;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;

public class SideU extends CommandBase {

    private double setpoint = 0;
    private double setpointYaw = 0;
    private boolean debug;
    private int num = 0;

    private PIDController pidXAxis;
    private PIDController pidZAxis;

    private double INTEGRAL_ENABLED_I = 0.05;

    private double lastUValue = 0.0;

    boolean right = true;

    private boolean firstExecution = true;
    private double startTime;
	private int atSetpointCounter;
    private double ACC_SPEED = 0.04;
    private double acceleration = 0.0;
    private int encoderStuckCounter = 0;

    public SideU(double setpoint, double epsilon, boolean debug, int num) {
        if(setpoint<0)
            right=false;
        this.setpoint = Math.abs(setpoint); // Set this according to how you measure side distance
        this.debug = debug;
        this.num = num;
        
        addRequirements(RobotContainer.train);

        pidXAxis = new PIDController(0.12, 0.0, 0.0);
        pidXAxis.setTolerance(epsilon);
        pidXAxis.setIntegratorRange(-0.5, 0.5);

        pidZAxis = new PIDController(0.1, 0.0, 0.0);
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
        double currentUValue = getCurrentUValue();

        updateSetpointIfNeeded(currentTime, currentUValue);
        executePIDControl(currentUValue);
        checkAndCorrectStuckEncoder(currentUValue);
    }

    @Override
    public void end(boolean interrupted) {
        RobotContainer.train.getMotorSystem().resetEncoders();
        RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
        RobotContainer.train.getSensorSystem().resetGyro();
        pidXAxis.close();
        pidZAxis.close();
    }

    public boolean areWithinRange(double value1, double value2) {
        return Math.abs(value1 - value2) <= 0.5;
    }

    private double getCurrentUValue() {
        if(!right)
            return RobotContainer.train.getSensorSystem().getSonic1Distance();
        else
            return RobotContainer.train.getSensorSystem().getSonic2Distance();
    }

    void updateSetpointIfNeeded(double currentTime, double currentIRValue) {
        if ((currentTime - startTime >= 0.1 || firstExecution) && debug) {
            setpoint = RobotContainer.train.getShuffleboardSystem().getAdditionalValueOutput().getDouble(0);
            pidXAxis.setSetpoint(setpoint);
            updatePID();
            startTime = currentTime;
            firstExecution = false;
        }
    }

    void executePIDControl(double currentIRValue) {
        acceleration=MathUtil.clamp(acceleration + ACC_SPEED, 0.0, 0.4);
        double outX = pidXAxis.calculate(currentIRValue, setpoint);
        double outZ = pidZAxis.calculate(RobotContainer.train.getSensorSystem().getAngle());
        if(!right)
            RobotContainer.train.getMotorSystem().holonomicDrive(-MathUtil.clamp(outX, -0.4, 0.4), 0.0, 0.0,
                    0.0, MathUtil.clamp(outZ, -0.4, 0.4));
        else
            RobotContainer.train.getMotorSystem().holonomicDrive(MathUtil.clamp(outX, -0.4, 0.4), 0.0, 0.0,
            0.0, MathUtil.clamp(outZ, -0.4, 0.4));

        if (debug) {
            RobotContainer.train.getShuffleboardSystem().updateTestString(String.format("S: %.1f SY: %.1f D: %.1f DY: %.1f",
                    setpoint, setpointYaw, setpoint - currentIRValue, setpointYaw - RobotContainer.train.getSensorSystem().getAngle()));
        }
    }
    
    void checkAndCorrectStuckEncoder(double currentEncoderValue) {
        if (areWithinRange(currentEncoderValue, lastUValue) && !pidXAxis.atSetpoint() && pidXAxis.getI() != INTEGRAL_ENABLED_I) {
            encoderStuckCounter++;
            if (encoderStuckCounter >= 50) {
                enableIntegral();
                encoderStuckCounter = 0;
            }
        } else {
            encoderStuckCounter = 0;
        }
        lastUValue = currentEncoderValue;
    }

    void enableIntegral() {
        pidXAxis.reset();
        pidXAxis.setI(INTEGRAL_ENABLED_I);
        System.out.println("S_U Enabled I " + INTEGRAL_ENABLED_I);
}

    void updatePID() {
        pidXAxis.setPID(RobotContainer.train.getShuffleboardSystem().getP().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getI().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getD().getDouble(0));
    }

    @Override
    public boolean isFinished() {
        if (pidXAxis.atSetpoint()) {
            atSetpointCounter++;
            if (atSetpointCounter >= 100) {
                return true;
            }
        } else {
            atSetpointCounter = 0;
        }
        return false;
    }
}
