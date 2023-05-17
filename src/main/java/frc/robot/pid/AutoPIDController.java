package frc.robot.pid;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;

public class AutoPIDController {
    public PIDController pidController;
    private double smoothLimit;
    private boolean tuning = false;
    private double ultimateGain;  // Ultimate gain
    private double oscillationPeriod;  // Period of oscillation
    private double prevError = 0;
    private Timer timer;

    public AutoPIDController(double epsilonDistance, double setpoint, double integratorRange) {
        pidController = new PIDController(0.03, 0.003, 0.0003);
        pidController.setTolerance(epsilonDistance);
        pidController.setIntegratorRange(setpoint - integratorRange, setpoint + integratorRange);
        smoothLimit = 0.2;
        timer = new Timer();
    }

    public void startTuning() {
        tuning = true;
        ultimateGain = 0;
        oscillationPeriod = 0;
        timer.start();
    }

    public void stopTuning() {
        tuning = false;
        timer.stop();
        pidController.setP(0.60*ultimateGain);  // According to Ziegler-Nichols method
        pidController.setI(1.20*ultimateGain/oscillationPeriod);
        pidController.setD(0.075*ultimateGain*oscillationPeriod);
    }

    public PIDOutput calculate(double currentPosition, double setpoint) {
        double output = pidController.calculate(currentPosition, setpoint);
        updateShuffleboard(output);
        double clampedOutput = MathUtil.clamp(output, -smoothLimit, smoothLimit);
        handleTuning(currentPosition, setpoint, output);
        adjustSmoothLimit();
        return new PIDOutput(clampedOutput,output);
    }

    private void updateShuffleboard(double output){
        RobotContainer.train.getShuffleboardSystem().updateTestValue(output);
    }

    private void handleTuning(double currentPosition, double setpoint, double output){
        if (tuning) {
            double error = setpoint - currentPosition;
            if (prevError < 0 && error >= 0) {  // Zero-crossing detected
                if (ultimateGain == 0) {  // First zero-crossing, start the timer
                    timer.reset();
                } else {  // Not the first zero-crossing, calculate the period
                    oscillationPeriod = timer.get();
                    timer.reset();
                }
                ultimateGain = Math.abs(output);
            }
            prevError = error;
        }
    }

    private void adjustSmoothLimit(){
        if (!tuning) {
            if (smoothLimit < 1) {
                smoothLimit += 0.01;
            }
            if (Math.abs(pidController.getPositionError()) < 800 && smoothLimit > 0.3) {
                smoothLimit -= 0.01;
            }
        }
    }

    public boolean atSetpoint() {
        return pidController.atSetpoint();
    }
}