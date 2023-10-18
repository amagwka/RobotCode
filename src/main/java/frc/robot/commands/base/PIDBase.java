package frc.robot.commands.base;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;

public abstract class PIDBase extends CommandBase {
  protected double setpoint;
  protected PIDController pid;
  protected double lastValue = 0.0;
  protected int counter = 0;
  protected int atSetpointCounter = 0;
  protected double startTime;
  protected boolean firstExecution = true;
  protected boolean debug;

  public PIDBase(double setpoint, double p, double i, double d, double epsilon, boolean debug) {
    this.setpoint = setpoint;
    this.debug = debug;

    addRequirements(RobotContainer.train);

    pid = new PIDController(p, i, d);
    pid.setTolerance(epsilon);
    pid.setSetpoint(setpoint);
  }

  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
    firstExecution = true;
  }

  @Override
  public void execute() {
    double currentTime = Timer.getFPGATimestamp();
    double currentValue = getCurrentValue();

    updateSetpointIfNeeded(currentTime, currentValue);
    checkAndCorrectStuckValue(currentValue);
    executePIDControl(currentValue);
  }

  abstract double getCurrentValue();

  void updateSetpointIfNeeded(double currentTime, double currentValue) {
    if ((currentTime - startTime >= 0.1 || firstExecution) && debug) {
      setpoint = getDebugSetpoint();
      pid.setSetpoint(setpoint);
      updatePID();
      startTime = currentTime;
      firstExecution = false;
    }
  }

  abstract double getDebugSetpoint();

  void checkAndCorrectStuckValue(double currentValue) {
    if (currentValue == lastValue && !pid.atSetpoint()) {
      counter++;
      if (counter >= 10) {
        enableIntegral();
        counter = 0;
      }
    } else {
      counter = 0;
    }
    lastValue = currentValue;
  }

  abstract void executePIDControl(double currentValue);

  void updatePID() {
    pid.setPID(RobotContainer.train.getShuffleboardSystem().getP().getDouble(0),
        RobotContainer.train.getShuffleboardSystem().getI().getDouble(0),
        RobotContainer.train.getShuffleboardSystem().getD().getDouble(0));
  }

  void enableIntegral() {
    pid.reset();
    pid.setI(0.15);
  }

  @Override
  public boolean isFinished() {
    if (pid.atSetpoint() && !debug) {
      atSetpointCounter++;
      if (atSetpointCounter >= 10) {
        return true;
      }
    } else {
      atSetpointCounter = 0;
    }
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    RobotContainer.train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
    pid.close();
  }
}