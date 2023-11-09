package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;

public class ForwardEncoder extends CommandBase {

    private double setpoint = 0;

    private double setpointYaw = 0;
    private boolean debug = false;

    PIDController pidYAxis;
    PIDController pid1;
    PIDController pid2;


    PIDController pidZAxis;

    int num=0;

    private int atSetpointCounter = 0;
    private int encoderStuckCounter = 0;

    private double INTEGRAL_ENABLED_I = 0.01;


    private double lastEncoderValue = 0.0;

    private double acceleration = 0.0;
    private double ACC_SPEED = 0.0;

    private boolean firstExecution = true;
    private double startTime;

    private boolean use_Zpid;

    public ForwardEncoder(double setpoint, double epsilon, boolean use_Zpid,int num) {
        this.setpoint = setpoint * 40;
        this.use_Zpid = true;

        this.num = num;

        addRequirements(RobotContainer.train);

        pidYAxis = new PIDController(0.012, 0.0, 0.0);
        pidYAxis.setTolerance(epsilon);
        pidYAxis.setIntegratorRange(-0.5, 0.5);


        pid1 = new PIDController(0.012, 0.0, 0.0);
        pid1.setTolerance(epsilon);
        pid1.setIntegratorRange(-0.5, 0.5);

        pid2 = new PIDController(0.012, 0.0, 0.0);
        pid2.setTolerance(epsilon);
        pid2.setIntegratorRange(-0.5, 0.5);


        pidZAxis = new PIDController(0.05, 0.0, 0.0);
        pidZAxis.setIntegratorRange(-0.2, 0.2);
    }

    @Override
    public void initialize() {
        if (use_Zpid){
            pidYAxis.setSetpoint(setpoint);
            pidYAxis.reset();
            setpointYaw = RobotContainer.train.getSensorSystem().getAngle();
            pidZAxis.setSetpoint(setpointYaw);
        }else{
            pid1.setSetpoint(setpoint);
            pid1.reset();
            pid2.setSetpoint(-setpoint);
            pid2.reset();

        }
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
        RobotContainer.train.getMotorSystem().resetEncoders();
        RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
        RobotContainer.train.getSensorSystem().resetGyro();
        pidYAxis.close();
        pidZAxis.close();
        pid1.close();
        pid2.close();
    }

    void updateSetpointIfNeeded(double currentTime, double currentEncoderValue) {
        if ((currentTime - startTime >= 0.1 || firstExecution) && debug) {
            setpoint = RobotContainer.train.getShuffleboardSystem().getAdditionalValueOutput().getDouble(0) * 40;
            if(use_Zpid){
                pidYAxis.setSetpoint(setpoint * 40);
            }else{
                pid1.setSetpoint(setpoint * 40);
                pid2.setSetpoint(-setpoint * 40);
            }
            updatePID();
            startTime = currentTime;
            firstExecution = false;
        }
    }

    void checkAndCorrectStuckEncoder(double currentEncoderValue) {
        if (currentEncoderValue == lastEncoderValue && !pidYAxis.atSetpoint() && pidYAxis.getI() != INTEGRAL_ENABLED_I) {
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
        acceleration=MathUtil.clamp(acceleration+0.05, 0.0, 0.4);
        if(use_Zpid){
            double outY = pidYAxis.calculate(currentEncoderValue, setpoint);
            double outZ = pidZAxis.calculate(RobotContainer.train.getSensorSystem().getAngle(), pidZAxis.getSetpoint());
            RobotContainer.train.getMotorSystem().holonomicDrive(0.0, MathUtil.clamp(outY, -acceleration, acceleration), 0.0,
            MathUtil.clamp(outZ, -0.4, 0.4), 0.0);
        }else{
            double left = pid1.calculate(RobotContainer.train.getMotorSystem().getLeftEncoderDistance());
            double right = pid2.calculate(RobotContainer.train.getMotorSystem().getRightEncoderDistance());
            RobotContainer.train.getMotorSystem().setLeftMotorSpeed(MathUtil.clamp(left,-acceleration,acceleration));
            RobotContainer.train.getMotorSystem().setRightMotorSpeed(MathUtil.clamp(right,-acceleration,acceleration));
        }

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
        if(use_Zpid){
            pidYAxis.reset();
            pidYAxis.setI(INTEGRAL_ENABLED_I);
            System.out.println("F Enabled I " + INTEGRAL_ENABLED_I);
        }else{
            pid1.reset();
            pid2.reset();
            pid1.setI(INTEGRAL_ENABLED_I);
            pid2.setI(INTEGRAL_ENABLED_I);
            System.out.println("F Enabled I " + INTEGRAL_ENABLED_I);
        }
    }

    @Override
    public boolean isFinished() {
        if(use_Zpid){
            if (pidYAxis.atSetpoint() && !debug) {
                atSetpointCounter++;
                if (atSetpointCounter >= 10) {
                    return true;
                }
            } else {
                atSetpointCounter = 0;
            }
            return false;
        }else{
            if (pid1.atSetpoint() && pid2.atSetpoint()) {
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
}
