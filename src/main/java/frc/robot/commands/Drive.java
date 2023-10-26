package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.RobotContainer;
import frc.robot.gamepad.OI;
import frc.robot.subsystems.OMS;
import frc.robot.subsystems.Training;

public class Drive extends CommandBase {
    private static final Training train = RobotContainer.train;
    private static final OMS oms = RobotContainer.oms;
    private static final OI oi = RobotContainer.oi;
    
    private static final double RAMP_UP = 0.05;
    private static final double RAMP_DOWN = 0.05;
    private static final double DELTA_LIMIT = 0.075;

    private double inputLeftY, inputLeftX, inputRightY;
    private double inputLeftBumper, inputRightBumper;
    private boolean inputLeftLTButton, inputRightRTButton, inputAButton, inputBButton, inputXButton, inputYButton;
    private double prevLeftY, prevLeftX, prevRightY;
    private double gripperDegrees = 60, r_gripperDegrees = 150;
    private double liftSpeed = 0, r_LiftSpeed = 0;

    public Drive() {
        addRequirements(train);
    }

    private double toInt(boolean b) {
        return b ? 1.0 : 0.0;
    }

    @Override
    public void execute() {
        readInputs();
        processInputs();
        setOutputs();
    }

    private void readInputs() {
        inputLeftX = oi.getLeftDriveX();
        inputLeftY = -oi.getLeftDriveY();
        inputRightY = oi.getRightDriveY();
        inputLeftBumper = oi.getDriveLeftBumper();
        inputRightBumper = oi.getDriveRightBumper();
        inputLeftLTButton = oi.getDriveLeftL1Button();
        inputRightRTButton = oi.getDriveRightR1Button();
        inputAButton = oi.getDriveAButton();
        inputBButton = oi.getDriveBButton();
        inputXButton = oi.getDriveXButton();
        inputYButton = oi.getDriveYButton();
    }

    private void processInputs() {
        updateDeltaAndSmoothInputs();
        updateGripperAndLift();
    }

    private void updateDeltaAndSmoothInputs() {
        double deltaLeftX = inputLeftX - prevLeftX;
        double deltaLeftY = inputLeftY - prevLeftY;
        double deltaRightY = inputRightY - prevRightY;

        inputLeftX = applyRamp(inputLeftX, deltaLeftX);
        inputLeftY = applyRamp(inputLeftY, deltaLeftY);
        inputRightY = applyRamp(inputRightY, deltaRightY);

        prevLeftY = inputLeftY;
        prevLeftX = inputLeftX;
        prevRightY = inputRightY;
    }

    private double applyRamp(double input, double delta) {
        if (delta >= DELTA_LIMIT) {
            return input + RAMP_UP;
        } else if (delta <= -DELTA_LIMIT) {
            return input - RAMP_DOWN;
        }
        return input;
    }

    private void updateGripperAndLift() {
        gripperDegrees += (-inputLeftBumper - 1) * 3 + (inputRightBumper + 1) * 3;
        r_gripperDegrees += -toInt(inputXButton) * 2 + toInt(inputBButton) * 2;
        liftSpeed += -toInt(inputYButton) * 0.7 + toInt(inputAButton) * 0.7;
        r_LiftSpeed += -toInt(inputLeftLTButton) * 0.2 + toInt(inputRightRTButton) * 0.2;

        gripperDegrees = MathUtil.clamp(gripperDegrees, 0, 180);
        r_gripperDegrees = MathUtil.clamp(r_gripperDegrees, 0, 180);
        liftSpeed = MathUtil.clamp(liftSpeed, -1, 1) / 1.5;
        r_LiftSpeed /= 2;
    }

    private void setOutputs() {
        oms.setLiftSpeed(liftSpeed);
        train.getMotorSystem().holonomicDrive(inputLeftX, inputLeftY, inputRightY);
    }

    @Override
    public void end(boolean interrupted) {
        train.getMotorSystem().setMotorSpeeds(0., 0., 0.);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
