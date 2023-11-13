package frc.robot.commands.dist;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;

public class IRAlign extends CommandBase {

    private double setpoint = 0;

    private boolean debug = false;

    PIDController pidYAxis;
    PIDController pidYAxis2;
    int num=0;

    private int atSetpointCounter = 0;
    private int encoderStuckCounter = 0;

    private double INTEGRAL_ENABLED_I = 0.05;


    private double lastIRValue1 = 0.0;
    private double lastIRValue2 = 0.0;

    private double acceleration = 0.0;
    private double ACC_SPEED = 0.04;

    private boolean firstExecution = true;
    private double startTime;


    public IRAlign(double setpoint, double epsilon, int num) {
        this.setpoint = setpoint;
        this.num = num;

        addRequirements(RobotContainer.train);

        pidYAxis = new PIDController(0.1, 0.0, 0.0);
        pidYAxis.setTolerance(epsilon);
        pidYAxis.setIntegratorRange(-0.5, 0.5);

        pidYAxis2 = new PIDController(0.1, 0.0, 0.0);
        pidYAxis2.setTolerance(epsilon);
        pidYAxis2.setIntegratorRange(-0.5, 0.5);
    }

    @Override
    public void initialize() {
        pidYAxis.setSetpoint(setpoint);
        pidYAxis.reset();
        pidYAxis2.setSetpoint(setpoint);
        pidYAxis2.reset();

        startTime = Timer.getFPGATimestamp();
        firstExecution = true;
        RobotContainer.train.getShuffleboardSystem().getATO().setString(String.format("Forward %d", num));
    }

    @Override
    public void execute() {
        double currentTime = Timer.getFPGATimestamp();
        double currentIRValue1 = RobotContainer.train.getSensorSystem().getIR1Distance();
        double currentIRValue2 = RobotContainer.train.getSensorSystem().getIR2Distance();

        updateSetpointIfNeeded(currentTime, currentIRValue1, currentIRValue2);
        checkAndCorrectStuckEncoder(currentIRValue1, currentIRValue2);
        executePIDControl(currentIRValue1, currentIRValue2);
    }


    // Called once the command ends or is interrupted
    @Override
    public void end(boolean interrupted) {
        RobotContainer.train.getMotorSystem().resetEncoders();
        RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
        RobotContainer.train.getSensorSystem().resetGyro();
        pidYAxis.close();
        pidYAxis2.close();
    }
    public boolean areWithinRange(double value1, double value2) {
        return Math.abs(value1 - value2) <= 0.5;
    }
    

    void updateSetpointIfNeeded(double currentTime, double currentIRValue1, double currentIRValue2) {
        if ((currentTime - startTime >= 0.1 || firstExecution) && debug) {
            setpoint = RobotContainer.train.getShuffleboardSystem().getAdditionalValueOutput().getDouble(0);
                pidYAxis.setSetpoint(setpoint);
            updatePID();
            startTime = currentTime;
            firstExecution = false;
        }
    }

    void checkAndCorrectStuckEncoder(double currentIRValue1, double currentIRValue2) {
        if (areWithinRange(currentIRValue1, lastIRValue1)
            && areWithinRange(currentIRValue2, lastIRValue2)
            && !pidYAxis.atSetpoint()
            && !pidYAxis2.atSetpoint()
            && pidYAxis.getI() != INTEGRAL_ENABLED_I
            && pidYAxis2.getI() != INTEGRAL_ENABLED_I) {
            encoderStuckCounter++;
            if (encoderStuckCounter >= 100) {
                enableIntegral();
                encoderStuckCounter = 0;
            }
        } else {
            encoderStuckCounter = 0;
        }
        lastIRValue1 = currentIRValue1;
        lastIRValue2 = currentIRValue2;
    }

    void executePIDControl(double currentIRValue1, double currentIRValue2) {
        acceleration=MathUtil.clamp(acceleration + ACC_SPEED, 0.0, 0.4);
        double out1 = pidYAxis.calculate(currentIRValue1, setpoint);
        double out2 = pidYAxis2.calculate(currentIRValue2,setpoint);
        RobotContainer.train.getMotorSystem().setMotorSpeeds(- MathUtil.clamp(out1, -acceleration, acceleration),
          MathUtil.clamp(out2, -acceleration, acceleration), 0.0);

        if (debug) {
            RobotContainer.train.getShuffleboardSystem().updateTestString(String.format("S: %.1f D: %.1f DY: %.1f",
                    setpoint, setpoint - lastIRValue1, setpoint - lastIRValue2));
        }
    }

    void updatePID() {
        pidYAxis.setPID(RobotContainer.train.getShuffleboardSystem().getP().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getI().getDouble(0),
                RobotContainer.train.getShuffleboardSystem().getD().getDouble(0));
    }

    void enableIntegral() {
            pidYAxis.reset();
            pidYAxis2.reset();
            pidYAxis.setI(INTEGRAL_ENABLED_I);
            pidYAxis2.setI(INTEGRAL_ENABLED_I);
            System.out.println("F Enabled I " + INTEGRAL_ENABLED_I);
    }

    @Override
    public boolean isFinished() {
            if (pidYAxis.atSetpoint()) {
                if (pidYAxis2.atSetpoint()) {
                    atSetpointCounter++;
                    if (atSetpointCounter >= 10) {
                        return true;
                    }
                }
                else {
                    atSetpointCounter = 0;
                }
            } else {
                atSetpointCounter = 0;
            }
            return false;
    }
}
